package dev.typr.foundations.pg;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.connect.DatabaseConfig;
import dev.typr.foundations.connect.DatabaseKind;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

/**
 * Tests PgPipelinePool connection recovery behavior using a fake PostgreSQL server that speaks
 * enough of the wire protocol to complete authentication and respond to queries.
 */
public class PgConnectionRecoveryTest {

  // ========== FakePgServer ==========

  static final class FakePgServer implements AutoCloseable {
    private final ServerSocket serverSocket;
    private final CopyOnWriteArrayList<Socket> clientSockets = new CopyOnWriteArrayList<>();
    private final AtomicBoolean stopped = new AtomicBoolean(false);
    private final AtomicInteger queryCount = new AtomicInteger(0);

    FakePgServer() throws IOException {
      this.serverSocket = new ServerSocket(0);
    }

    int port() {
      return serverSocket.getLocalPort();
    }

    int queryCount() {
      return queryCount.get();
    }

    /**
     * Accept a single client, complete the PG startup/auth handshake, then continuously read
     * messages and respond to Sync with ReadyForQuery (plus minimal query responses). Runs on a
     * virtual thread so the caller does not block.
     */
    Thread acceptAndServe() {
      return Thread.ofVirtual()
          .name("fake-pg-server")
          .start(
              () -> {
                try {
                  Socket client = serverSocket.accept();
                  clientSockets.add(client);
                  handleClient(client);
                } catch (IOException e) {
                  if (!stopped.get()) throw new RuntimeException(e);
                }
              });
    }

    /**
     * Accept multiple clients (up to count), each on its own virtual thread. Returns the list of
     * server threads.
     */
    List<Thread> acceptAndServeMany(int count) {
      List<Thread> threads = new ArrayList<>();
      for (int i = 0; i < count; i++) {
        threads.add(acceptAndServe());
      }
      return threads;
    }

    private void handleClient(Socket client) throws IOException {
      InputStream in = client.getInputStream();
      OutputStream out = client.getOutputStream();

      readStartupMessage(in);
      sendAuthenticationOk(out);
      sendParameterStatus(out, "server_version", "16.0");
      sendParameterStatus(out, "server_encoding", "UTF8");
      sendParameterStatus(out, "client_encoding", "UTF8");
      sendParameterStatus(out, "integer_datetimes", "on");
      sendBackendKeyData(out, 1234, 5678);
      sendReadyForQuery(out, 'I');
      out.flush();

      serveLoop(in, out);
    }

    private void serveLoop(InputStream rawIn, OutputStream out) throws IOException {
      DataInputStream in = new DataInputStream(rawIn);
      boolean inTransaction = false;

      while (!stopped.get()) {
        int type;
        try {
          type = in.read();
        } catch (SocketException e) {
          return;
        }
        if (type == -1) return;

        int length = in.readInt();
        int bodyLen = length - 4;
        if (bodyLen > 0) {
          byte[] body = new byte[bodyLen];
          in.readFully(body);
        }

        switch ((char) type) {
          case 'P' -> {
            // Parse — respond with ParseComplete
            sendParseComplete(out);
          }
          case 'D' -> {
            // Describe — respond with NoData (simplest)
            sendNoData(out);
          }
          case 'B' -> {
            // Bind — respond with BindComplete
            sendBindComplete(out);
          }
          case 'E' -> {
            // Execute — respond with CommandComplete
            queryCount.incrementAndGet();
            sendCommandComplete(out, "SELECT 1");
          }
          case 'Q' -> {
            // Simple query — respond with CommandComplete + ReadyForQuery
            queryCount.incrementAndGet();
            sendCommandComplete(out, "SELECT 1");
            char status = inTransaction ? 'T' : 'I';
            sendReadyForQuery(out, status);
            out.flush();
          }
          case 'S' -> {
            // Sync — respond with ReadyForQuery
            char status = inTransaction ? 'T' : 'I';
            sendReadyForQuery(out, status);
            out.flush();
          }
          case 'X' -> {
            // Terminate
            return;
          }
          case 'H' -> {
            // Flush
            out.flush();
          }
          default -> {
            // Unknown message — ignore body (already consumed above)
          }
        }
      }
    }

    void killAllConnections() {
      for (Socket s : clientSockets) {
        try {
          s.close();
        } catch (IOException ignored) {
        }
      }
      clientSockets.clear();
    }

    void killConnection(int index) {
      if (index < clientSockets.size()) {
        Socket s = clientSockets.get(index);
        try {
          s.close();
        } catch (IOException ignored) {
        }
      }
    }

    int connectionCount() {
      return clientSockets.size();
    }

    @Override
    public void close() {
      stopped.set(true);
      killAllConnections();
      try {
        serverSocket.close();
      } catch (IOException ignored) {
      }
    }

    // ========== PG wire protocol message writers ==========

    private static void readStartupMessage(InputStream in) throws IOException {
      DataInputStream din = new DataInputStream(in);
      int length = din.readInt();
      int remaining = length - 4;
      if (remaining > 0) {
        byte[] body = new byte[remaining];
        din.readFully(body);
      }
    }

    private static void sendAuthenticationOk(OutputStream out) throws IOException {
      out.write('R');
      writeInt4(out, 8);
      writeInt4(out, 0);
    }

    private static void sendParameterStatus(OutputStream out, String name, String value)
        throws IOException {
      byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
      byte[] valueBytes = value.getBytes(StandardCharsets.UTF_8);
      out.write('S');
      writeInt4(out, 4 + nameBytes.length + 1 + valueBytes.length + 1);
      out.write(nameBytes);
      out.write(0);
      out.write(valueBytes);
      out.write(0);
    }

    private static void sendBackendKeyData(OutputStream out, int pid, int secretKey)
        throws IOException {
      out.write('K');
      writeInt4(out, 12);
      writeInt4(out, pid);
      writeInt4(out, secretKey);
    }

    private static void sendReadyForQuery(OutputStream out, char status) throws IOException {
      out.write('Z');
      writeInt4(out, 5);
      out.write(status);
    }

    private static void sendParseComplete(OutputStream out) throws IOException {
      out.write('1');
      writeInt4(out, 4);
    }

    private static void sendBindComplete(OutputStream out) throws IOException {
      out.write('2');
      writeInt4(out, 4);
    }

    private static void sendNoData(OutputStream out) throws IOException {
      out.write('n');
      writeInt4(out, 4);
    }

    private static void sendCommandComplete(OutputStream out, String tag) throws IOException {
      byte[] tagBytes = tag.getBytes(StandardCharsets.UTF_8);
      out.write('C');
      writeInt4(out, 4 + tagBytes.length + 1);
      out.write(tagBytes);
      out.write(0);
    }

    private static void sendErrorResponse(
        OutputStream out, String severity, String message, String sqlState) throws IOException {
      ByteArrayOutputStream body = new ByteArrayOutputStream();
      body.write('S');
      body.write(severity.getBytes(StandardCharsets.UTF_8));
      body.write(0);
      body.write('M');
      body.write(message.getBytes(StandardCharsets.UTF_8));
      body.write(0);
      body.write('C');
      body.write(sqlState.getBytes(StandardCharsets.UTF_8));
      body.write(0);
      body.write(0); // terminator

      byte[] bodyBytes = body.toByteArray();
      out.write('E');
      writeInt4(out, 4 + bodyBytes.length);
      out.write(bodyBytes);
    }

    private static void writeInt4(OutputStream out, int value) throws IOException {
      out.write((value >> 24) & 0xFF);
      out.write((value >> 16) & 0xFF);
      out.write((value >> 8) & 0xFF);
      out.write(value & 0xFF);
    }
  }

  // ========== Helper: DatabaseConfig for fake server ==========

  private static DatabaseConfig fakeConfig(int port) {
    return new DatabaseConfig() {
      @Override
      public String jdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:" + port + "/testdb";
      }

      @Override
      public String username() {
        return "testuser";
      }

      @Override
      public String password() {
        return "testpass";
      }

      @Override
      public DatabaseKind kind() {
        return DatabaseKind.POSTGRESQL;
      }

      @Override
      public Map<String, String> driverProperties() {
        return Map.of();
      }
    };
  }

  // ========== Tests ==========

  @Test
  public void poolConnectsToFakeServer() throws Exception {
    try (FakePgServer server = new FakePgServer()) {
      server.acceptAndServeMany(2);
      Thread.sleep(50);

      PgPipelineConfig pipelineConfig =
          PgPipelineConfig.builder()
              .connectionCount(2)
              .connectTimeout(Duration.ofSeconds(5))
              .queryTimeout(Duration.ofSeconds(5))
              .validationInterval(Duration.ofHours(1))
              .build();

      try (PgPipelinePool pool = PgPipelinePool.create(fakeConfig(server.port()), pipelineConfig)) {
        int result = pool.update(Fragment.of("SELECT 1"));
        assertEqual(result, 1);
      }
    }
  }

  @Test
  public void connectionRecoveryAfterServerKill() throws Exception {
    try (FakePgServer server = new FakePgServer()) {
      server.acceptAndServeMany(2);
      Thread.sleep(50);

      PgPipelineConfig pipelineConfig =
          PgPipelineConfig.builder()
              .connectionCount(2)
              .connectTimeout(Duration.ofSeconds(2))
              .queryTimeout(Duration.ofSeconds(5))
              .validationInterval(Duration.ofHours(1))
              .build();

      try (PgPipelinePool pool = PgPipelinePool.create(fakeConfig(server.port()), pipelineConfig)) {
        int result = pool.update(Fragment.of("SELECT 1"));
        assertEqual(result, 1);

        server.killAllConnections();
        Thread.sleep(100);

        server.acceptAndServeMany(2);
        Thread.sleep(50);

        int recovered = pool.update(Fragment.of("SELECT 1"));
        assertEqual(recovered, 1);
      }
    }
  }

  @Test
  public void readonlyRetryOnConnectionFailure() throws Exception {
    try (FakePgServer server = new FakePgServer()) {
      server.acceptAndServeMany(2);
      Thread.sleep(50);

      PgPipelineConfig pipelineConfig =
          PgPipelineConfig.builder()
              .connectionCount(2)
              .connectTimeout(Duration.ofSeconds(2))
              .queryTimeout(Duration.ofSeconds(5))
              .validationInterval(Duration.ofHours(1))
              .build();

      try (PgPipelinePool pool = PgPipelinePool.create(fakeConfig(server.port()), pipelineConfig)) {
        pool.update(Fragment.of("SELECT 1"));

        server.killConnection(0);
        Thread.sleep(100);

        server.acceptAndServe();
        Thread.sleep(50);

        int result = pool.update(Fragment.of("SELECT 1"));
        assertEqual(result, 1);
      }
    }
  }

  @Test
  public void serverRefusingNewConnections() throws Exception {
    FakePgServer server = new FakePgServer();
    int port = server.port();
    server.acceptAndServe();
    Thread.sleep(50);

    PgPipelineConfig pipelineConfig =
        PgPipelineConfig.builder()
            .connectionCount(1)
            .connectTimeout(Duration.ofSeconds(1))
            .queryTimeout(Duration.ofSeconds(2))
            .validationInterval(Duration.ofHours(1))
            .exhaustionStrategy(PgExhaustionStrategy.THROW)
            .build();

    try (PgPipelinePool pool = PgPipelinePool.create(fakeConfig(port), pipelineConfig)) {
      int result = pool.update(Fragment.of("SELECT 1"));
      assertEqual(result, 1);

      server.killAllConnections();
      server.close();
      Thread.sleep(100);

      boolean threw = false;
      try {
        pool.update(Fragment.of("SELECT 1"));
      } catch (PgPipelineException e) {
        threw = true;
      }
      assertTrue(threw);

      FakePgServer server2 = new FakePgServer();
      try {
        // We cannot reuse the same port deterministically, so this test only verifies
        // that the pool threw correctly when the server was down.
      } finally {
        server2.close();
      }
    }
  }

  @Test
  public void eagerReplacementTriggered() throws Exception {
    try (FakePgServer server = new FakePgServer()) {
      server.acceptAndServeMany(2);
      Thread.sleep(50);

      PgPipelineConfig pipelineConfig =
          PgPipelineConfig.builder()
              .connectionCount(2)
              .connectTimeout(Duration.ofSeconds(2))
              .queryTimeout(Duration.ofSeconds(5))
              .validationInterval(Duration.ofMillis(200))
              .keepaliveTime(Duration.ofMillis(100))
              .build();

      try (PgPipelinePool pool = PgPipelinePool.create(fakeConfig(server.port()), pipelineConfig)) {
        pool.update(Fragment.of("SELECT 1"));

        server.killConnection(0);
        server.acceptAndServe();
        Thread.sleep(500);

        int result = pool.update(Fragment.of("SELECT 1"));
        assertEqual(result, 1);
      }
    }
  }

  @Test
  public void keepalivePingDetectsStaleConnection() throws Exception {
    try (FakePgServer server = new FakePgServer()) {
      server.acceptAndServe();
      Thread.sleep(50);

      PgPipelineConfig pipelineConfig =
          PgPipelineConfig.builder()
              .connectionCount(1)
              .connectTimeout(Duration.ofSeconds(2))
              .queryTimeout(Duration.ofSeconds(5))
              .validationInterval(Duration.ofMillis(200))
              .keepaliveTime(Duration.ofMillis(100))
              .build();

      try (PgPipelinePool pool = PgPipelinePool.create(fakeConfig(server.port()), pipelineConfig)) {
        pool.update(Fragment.of("SELECT 1"));

        server.killConnection(0);
        server.acceptAndServe();
        Thread.sleep(600);

        int result = pool.update(Fragment.of("SELECT 1"));
        assertEqual(result, 1);
      }
    }
  }

  @Test
  public void immediateServerClose() throws Exception {
    try (FakePgServer server = new FakePgServer()) {
      // Server that accepts then immediately closes
      Thread serverThread =
          Thread.ofVirtual()
              .start(
                  () -> {
                    try {
                      Socket client = server.serverSocket.accept();
                      client.close();
                    } catch (IOException ignored) {
                    }
                  });

      Thread.sleep(50);

      PgPipelineConfig pipelineConfig =
          PgPipelineConfig.builder()
              .connectionCount(1)
              .connectTimeout(Duration.ofSeconds(2))
              .queryTimeout(Duration.ofSeconds(2))
              .build();

      boolean threw = false;
      try {
        PgPipelinePool.create(fakeConfig(server.port()), pipelineConfig);
      } catch (PgPipelineException e) {
        threw = true;
      }
      assertTrue(threw);
    }
  }

  @Test
  public void serverSendsErrorDuringAuth() throws Exception {
    try (FakePgServer server = new FakePgServer()) {
      Thread serverThread =
          Thread.ofVirtual()
              .start(
                  () -> {
                    try {
                      Socket client = server.serverSocket.accept();
                      InputStream in = client.getInputStream();
                      OutputStream out = client.getOutputStream();

                      FakePgServer.readStartupMessage(in);
                      FakePgServer.sendErrorResponse(out, "FATAL", "too many connections", "53300");
                      out.flush();
                      client.close();
                    } catch (IOException ignored) {
                    }
                  });

      Thread.sleep(50);

      PgPipelineConfig pipelineConfig =
          PgPipelineConfig.builder()
              .connectionCount(1)
              .connectTimeout(Duration.ofSeconds(2))
              .queryTimeout(Duration.ofSeconds(2))
              .build();

      boolean threw = false;
      String message = "";
      try {
        PgPipelinePool.create(fakeConfig(server.port()), pipelineConfig);
      } catch (PgPipelineException e) {
        threw = true;
        message = e.getMessage();
      }
      assertTrue(threw);
      assertTrue(
          message.contains("too many connections") || message.contains("Connection rejected"));
    }
  }

  @Test
  public void multipleQueriesAfterRecovery() throws Exception {
    try (FakePgServer server = new FakePgServer()) {
      server.acceptAndServeMany(2);
      Thread.sleep(50);

      PgPipelineConfig pipelineConfig =
          PgPipelineConfig.builder()
              .connectionCount(2)
              .connectTimeout(Duration.ofSeconds(2))
              .queryTimeout(Duration.ofSeconds(5))
              .validationInterval(Duration.ofHours(1))
              .build();

      try (PgPipelinePool pool = PgPipelinePool.create(fakeConfig(server.port()), pipelineConfig)) {
        pool.update(Fragment.of("SELECT 1"));
        pool.update(Fragment.of("SELECT 1"));

        server.killAllConnections();
        Thread.sleep(100);

        server.acceptAndServeMany(2);
        Thread.sleep(50);

        for (int i = 0; i < 10; i++) {
          int result = pool.update(Fragment.of("SELECT 1"));
          assertEqual(result, 1);
        }
      }
    }
  }

  @Test
  public void poolCloseIsIdempotent() throws Exception {
    try (FakePgServer server = new FakePgServer()) {
      server.acceptAndServeMany(2);
      Thread.sleep(50);

      PgPipelineConfig pipelineConfig =
          PgPipelineConfig.builder()
              .connectionCount(2)
              .connectTimeout(Duration.ofSeconds(2))
              .queryTimeout(Duration.ofSeconds(5))
              .validationInterval(Duration.ofHours(1))
              .build();

      PgPipelinePool pool = PgPipelinePool.create(fakeConfig(server.port()), pipelineConfig);
      pool.close();
      pool.close();
      pool.close();
    }
  }

  @Test
  public void connectionRefusedOnStartup() throws Exception {
    // Use a port that is not listening
    ServerSocket ss = new ServerSocket(0);
    int port = ss.getLocalPort();
    ss.close();

    PgPipelineConfig pipelineConfig =
        PgPipelineConfig.builder()
            .connectionCount(1)
            .connectTimeout(Duration.ofSeconds(1))
            .queryTimeout(Duration.ofSeconds(1))
            .build();

    boolean threw = false;
    try {
      PgPipelinePool.create(fakeConfig(port), pipelineConfig);
    } catch (PgPipelineException e) {
      threw = true;
    }
    assertTrue(threw);
  }

  // ========== Helpers ==========

  private static void assertEqual(Object actual, Object expected) {
    if (expected == null && actual == null) return;
    if (expected == null || actual == null) {
      throw new AssertionError("Expected: " + expected + ", Actual: " + actual);
    }
    if (!expected.equals(actual)) {
      throw new AssertionError("Expected: " + expected + ", Actual: " + actual);
    }
  }

  private static void assertEqual(int actual, int expected) {
    if (actual != expected) {
      throw new AssertionError("Expected: " + expected + ", Actual: " + actual);
    }
  }

  private static void assertTrue(boolean value) {
    if (!value) throw new AssertionError("Expected true");
  }

  private static void assertFalse(boolean value) {
    if (value) throw new AssertionError("Expected false");
  }
}
