package dev.typr.foundations.pg;

import java.io.BufferedReader;
import java.io.StringReader;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.List;
import org.junit.Test;

public class PgPipelineStaticTest {

  // ========== Test PEM data ==========

  private static final String TEST_CERT_PEM =
      "-----BEGIN CERTIFICATE-----\n"
          + "MIICwTCCAamgAwIBAgIIb6kJ5du3egQwDQYJKoZIhvcNAQEMBQAwDzENMAsGA1UE\n"
          + "AxMEVGVzdDAeFw0yNjAzMjEyMzI1MTVaFw0zNjAzMTgyMzI1MTVaMA8xDTALBgNV\n"
          + "BAMTBFRlc3QwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQC/z6W/NCrk\n"
          + "qb4nrEF0QBLjsBot70AMtscsLylvlq3mmQiCnnzYOtWSpTm8T53escDEhil8CLVZ\n"
          + "V87Zy00ZmPcbAuVVe4sqk+Hc63BBsjj6oAzH0pZalAhdDuoaN31BsDqayiTP7s5w\n"
          + "M6m0hPiXbGVCT9/27bQNJNO9eEQ1n9NZAwBv1UmjWURU7L9yVOMyLsW1od8Xj6I/\n"
          + "SBvBxbc8SRn83ckA1KXRkZ/rg4KrrNIiA4g9Du49M8kH9J4SidlIRcRsgm3/Ni/l\n"
          + "PH4qov95w7DIBHi8PEXr+WlRR7gY40Cl/Z3Iflr91HAkUn1jWyIGhRwaAaPFh6ya\n"
          + "UN8zmJQxaaoRAgMBAAGjITAfMB0GA1UdDgQWBBT0v4kCcAYTKL20tjbtigsBOdjm\n"
          + "DDANBgkqhkiG9w0BAQwFAAOCAQEAfoChzSNgmVEPNPG8JUeABAp7POWem+of7mwT\n"
          + "xn3f/EZW9E+0J4tMc7FPj5pcZPfHEyGvJqxQRKeJVSBfGmebLgAF9QirBMSiHi0S\n"
          + "ByY7pImvb27Gl7aCePJ2eVyS8SsbQGZ74HOracKWvvqhKCgJ1Ik8DUO7vqfSgI8t\n"
          + "KfvigcIHQ72oZyLrJ84hIKmeWt5b9t5NxBWnm800FnRl2xSqED+VZG3iJzfzBCGU\n"
          + "QMpWJbjG2YodDBBQIigiVGwKpFxQ+mR9H32rtp5MHLq1873VTZH5m3ziwi9BQQqv\n"
          + "hEldP2v/K0YMCWvuKbouLM4AJbh+7ZxkK5NFr2U0EfrbuPE58Q==\n"
          + "-----END CERTIFICATE-----\n";

  private static final String TEST_KEY_PEM =
      "-----BEGIN PRIVATE KEY-----\n"
          + "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC/z6W/NCrkqb4n\n"
          + "rEF0QBLjsBot70AMtscsLylvlq3mmQiCnnzYOtWSpTm8T53escDEhil8CLVZV87Z\n"
          + "y00ZmPcbAuVVe4sqk+Hc63BBsjj6oAzH0pZalAhdDuoaN31BsDqayiTP7s5wM6m0\n"
          + "hPiXbGVCT9/27bQNJNO9eEQ1n9NZAwBv1UmjWURU7L9yVOMyLsW1od8Xj6I/SBvB\n"
          + "xbc8SRn83ckA1KXRkZ/rg4KrrNIiA4g9Du49M8kH9J4SidlIRcRsgm3/Ni/lPH4q\n"
          + "ov95w7DIBHi8PEXr+WlRR7gY40Cl/Z3Iflr91HAkUn1jWyIGhRwaAaPFh6yaUN8z\n"
          + "mJQxaaoRAgMBAAECggEAA8cwZ7Ycd61Pv2qNTf/Cl31FIL0/DNkPToOdzHLCeWGv\n"
          + "iRob0asEAKvUOa00SfoEluJhIA4wW0KHYqMWYVAR4vRxRbglnzizONWi3ApphKdB\n"
          + "JTD00PMsQH/+FLbd1tzEpL6hT76vjoZ3++QoHNxv2Kjn245mTuF4MbnUFx3PLQhB\n"
          + "N0A7XuQqgFlkWc70me+PdObjrUu+0VaC/Z9ypvNbzOfjgD8FBEkJ033ASbZ4cYJu\n"
          + "Ey4EIClUiq2Zkd1P85ykrDVhyTciKkO9nPPky5CSpvEXvUIXrqwV9rbyldkUw913\n"
          + "kMPis19hBZehPTLhHf1xvjnaN4L9Zc/0n0rdYIECeQKBgQDx23WKFnCG/GSWyFbT\n"
          + "ADwFXIUlqlnZZptkMDSK4EzS0E756c8mXYNbkPrKHEJHPmYCBgPdDjb1AADHxFh9\n"
          + "BbB/6e5aS+RsDKE+hTlQGDtWE+Onk/478TEoh17e/W3XZkN+DjPKIC7ysT2wi2/9\n"
          + "0WRQ9umPMMR3rc5Dl8OdWcldKQKBgQDLBwKwh0ZQ6f4K09Nk0qaLmo/a+r+EQy3N\n"
          + "+fFNM1qAlVrLZPSKE7zvixTGcw4yJaeScG/5RrHsi83PhSyczUkh7vi5E3GeSR6O\n"
          + "tfItBZcV9bJ35ThscK1OCoDlcX6PjyfhXHLH8jsKYgVXGmFS/HjfoQFVx4PWlwQS\n"
          + "LT+/g30aqQKBgQDxATldQwMa4AL0ZepvFDq7NPjREbG6+445Bfg1s1GaeU1Zp+1u\n"
          + "U50D77KZGBPLnphTx96ttu54sWoVpshuvO6maFvFyv5x1RwQiv0TnTHzkpo2cTjU\n"
          + "ToIn2s1k87EzBYqX4FsnPW7LFGdet3P/v1pxe7o9n+EHSUDTBjY9ZC1OEQKBgCet\n"
          + "jxEA3vUhRczH6SBjKrlN059wMc2fc2w11LrJtn46sivd5wy1Hs+R1tWR1sxmH4G3\n"
          + "B6sn7c3U5LwRws5CMHC5S1KgQiw1DgvF8zZpSUW5Zs+XtJc8ziHaW6Z7r8hOqfSZ\n"
          + "JLV+2ZsF3RVQyoR1YwoRZTlmbJWbnegPf83l3KVBAoGBAImJJeb6MsxFgwYPdAVQ\n"
          + "tQxDjRf8YeV9iGfsuE6i0RhzVJE1vSFcChVYQsMitewQrSB+ZszwLhz8+cErx+Y0\n"
          + "v6uo8eTDrlPlSDQvYc8eEtetoFLf1Xq0kATM9k58bl4eg9w3sa5JWp1E+g/5S64n\n"
          + "YxPh0exp/CJbyie/mNmu4zOH\n"
          + "-----END PRIVATE KEY-----\n";

  // ========== parseJdbcUrl ==========

  @Test
  public void parseJdbcUrlFull() {
    PgPipelinePool.JdbcUrlParts parts =
        PgPipelinePool.parseJdbcUrl("jdbc:postgresql://myhost:5433/mydb");
    assertEqual(parts.host(), "myhost");
    assertEqual(parts.port(), 5433);
    assertEqual(parts.database(), "mydb");
  }

  @Test
  public void parseJdbcUrlDefaultPort() {
    PgPipelinePool.JdbcUrlParts parts =
        PgPipelinePool.parseJdbcUrl("jdbc:postgresql://localhost/testdb");
    assertEqual(parts.host(), "localhost");
    assertEqual(parts.port(), 5432);
    assertEqual(parts.database(), "testdb");
  }

  @Test
  public void parseJdbcUrlWithQueryParams() {
    PgPipelinePool.JdbcUrlParts parts =
        PgPipelinePool.parseJdbcUrl("jdbc:postgresql://host:5432/db?sslmode=require&timeout=30");
    assertEqual(parts.host(), "host");
    assertEqual(parts.port(), 5432);
    assertEqual(parts.database(), "db");
  }

  @Test
  public void parseJdbcUrlNoDatabase() {
    PgPipelinePool.JdbcUrlParts parts = PgPipelinePool.parseJdbcUrl("jdbc:postgresql://host:5432/");
    assertEqual(parts.host(), "host");
    assertEqual(parts.port(), 5432);
    assertEqual(parts.database(), "");
  }

  @Test
  public void parseJdbcUrlNoDatabaseNoSlash() {
    PgPipelinePool.JdbcUrlParts parts = PgPipelinePool.parseJdbcUrl("jdbc:postgresql://host:9999");
    assertEqual(parts.host(), "host");
    assertEqual(parts.port(), 9999);
    assertEqual(parts.database(), "");
  }

  @Test
  public void parseJdbcUrlLocalhost() {
    PgPipelinePool.JdbcUrlParts parts =
        PgPipelinePool.parseJdbcUrl("jdbc:postgresql://127.0.0.1:5432/postgres");
    assertEqual(parts.host(), "127.0.0.1");
    assertEqual(parts.port(), 5432);
    assertEqual(parts.database(), "postgres");
  }

  // ========== convertPlaceholders ==========

  @Test
  public void convertPlaceholdersSimple() {
    assertEqual(
        PgProtocol.convertPlaceholders("SELECT ? FROM t WHERE x = ?"),
        "SELECT $1 FROM t WHERE x = $2");
  }

  @Test
  public void convertPlaceholdersNoParams() {
    assertEqual(PgProtocol.convertPlaceholders("SELECT 1"), "SELECT 1");
  }

  @Test
  public void convertPlaceholdersInSingleQuotes() {
    assertEqual(
        PgProtocol.convertPlaceholders("SELECT '?' FROM t WHERE x = ?"),
        "SELECT '?' FROM t WHERE x = $1");
  }

  @Test
  public void convertPlaceholdersInDoubleQuotes() {
    assertEqual(
        PgProtocol.convertPlaceholders("SELECT \"?\" FROM t WHERE x = ?"),
        "SELECT \"?\" FROM t WHERE x = $1");
  }

  @Test
  public void convertPlaceholdersMultiple() {
    assertEqual(
        PgProtocol.convertPlaceholders("INSERT INTO t (a, b, c) VALUES (?, ?, ?)"),
        "INSERT INTO t (a, b, c) VALUES ($1, $2, $3)");
  }

  @Test
  public void convertPlaceholdersMixedQuotes() {
    assertEqual(
        PgProtocol.convertPlaceholders("SELECT * FROM \"table?\" WHERE name = '?' AND id = ?"),
        "SELECT * FROM \"table?\" WHERE name = '?' AND id = $1");
  }

  @Test
  public void convertPlaceholdersNestedQuotes() {
    assertEqual(
        PgProtocol.convertPlaceholders("SELECT * FROM t WHERE a = 'it''s' AND b = ?"),
        "SELECT * FROM t WHERE a = 'it''s' AND b = $1");
  }

  @Test
  public void convertPlaceholdersEmptySql() {
    assertEqual(PgProtocol.convertPlaceholders(""), "");
  }

  // ========== parseAffectedRows ==========

  @Test
  public void parseAffectedRowsInsert() {
    assertEqual(PgProtocol.parseAffectedRows("INSERT 0 5"), 5);
  }

  @Test
  public void parseAffectedRowsUpdate() {
    assertEqual(PgProtocol.parseAffectedRows("UPDATE 3"), 3);
  }

  @Test
  public void parseAffectedRowsDelete() {
    assertEqual(PgProtocol.parseAffectedRows("DELETE 1"), 1);
  }

  @Test
  public void parseAffectedRowsSelect() {
    assertEqual(PgProtocol.parseAffectedRows("SELECT 10"), 10);
  }

  @Test
  public void parseAffectedRowsEmpty() {
    assertEqual(PgProtocol.parseAffectedRows(""), 0);
  }

  @Test
  public void parseAffectedRowsCreateTable() {
    assertEqual(PgProtocol.parseAffectedRows("CREATE TABLE"), 0);
  }

  @Test
  public void parseAffectedRowsZero() {
    assertEqual(PgProtocol.parseAffectedRows("UPDATE 0"), 0);
  }

  @Test
  public void parseAffectedRowsLargeCount() {
    assertEqual(PgProtocol.parseAffectedRows("INSERT 0 1000000"), 1000000);
  }

  @Test
  public void parseAffectedRowsCopy() {
    assertEqual(PgProtocol.parseAffectedRows("COPY 42"), 42);
  }

  // ========== isStaleStatementError ==========

  @Test
  public void isStaleStatementErrorPreparedNotFound() {
    assertTrue(PgPipelinedConnection.isStaleStatementError("26000"));
  }

  @Test
  public void isStaleStatementErrorDuplicatePrepared() {
    assertTrue(PgPipelinedConnection.isStaleStatementError("42P05"));
  }

  @Test
  public void isStaleStatementErrorCachedPlanChanged() {
    assertTrue(PgPipelinedConnection.isStaleStatementError("0A000"));
  }

  @Test
  public void isStaleStatementErrorOtherCodes() {
    assertFalse(PgPipelinedConnection.isStaleStatementError("42P01"));
    assertFalse(PgPipelinedConnection.isStaleStatementError("23505"));
    assertFalse(PgPipelinedConnection.isStaleStatementError("08006"));
    assertFalse(PgPipelinedConnection.isStaleStatementError("42000"));
    assertFalse(PgPipelinedConnection.isStaleStatementError("26001"));
  }

  @Test
  public void isStaleStatementErrorNull() {
    assertFalse(PgPipelinedConnection.isStaleStatementError(null));
  }

  @Test
  public void isStaleStatementErrorEmpty() {
    assertFalse(PgPipelinedConnection.isStaleStatementError(""));
  }

  // ========== checkReplaceReason ==========

  @Test
  public void replaceReasonNoneWhenFresh() {
    long now = System.nanoTime();
    long created = now - 1_000_000_000L; // 1s ago
    long lastActivity = now - 500_000_000L; // 0.5s ago
    long maxLifetime = 60_000_000_000L; // 60s
    long idleTimeout = 30_000_000_000L; // 30s

    assertEqual(
        PgPipelinePool.checkReplaceReason(created, lastActivity, 0, maxLifetime, idleTimeout, now),
        PgPipelinePool.ReplaceReason.NONE);
  }

  @Test
  public void replaceReasonMaxLifetimeExceeded() {
    long now = System.nanoTime();
    long created = now - 120_000_000_000L; // 120s ago
    long lastActivity = now - 1_000_000_000L;
    long maxLifetime = 60_000_000_000L;
    long idleTimeout = 300_000_000_000L; // 5 min

    assertEqual(
        PgPipelinePool.checkReplaceReason(created, lastActivity, 0, maxLifetime, idleTimeout, now),
        PgPipelinePool.ReplaceReason.MAX_LIFETIME);
  }

  @Test
  public void replaceReasonMaxLifetimeExceededButPending() {
    long now = System.nanoTime();
    long created = now - 120_000_000_000L;
    long lastActivity = now - 1_000_000_000L;
    long maxLifetime = 60_000_000_000L;
    long idleTimeout = 300_000_000_000L;

    assertEqual(
        PgPipelinePool.checkReplaceReason(created, lastActivity, 5, maxLifetime, idleTimeout, now),
        PgPipelinePool.ReplaceReason.NONE);
  }

  @Test
  public void replaceReasonIdleTimeout() {
    long now = System.nanoTime();
    long created = now - 10_000_000_000L; // 10s ago
    long lastActivity = now - 60_000_000_000L; // 60s ago
    long maxLifetime = 300_000_000_000L; // 5 min
    long idleTimeout = 30_000_000_000L; // 30s

    assertEqual(
        PgPipelinePool.checkReplaceReason(created, lastActivity, 0, maxLifetime, idleTimeout, now),
        PgPipelinePool.ReplaceReason.IDLE_TIMEOUT);
  }

  @Test
  public void replaceReasonIdleTimeoutButPending() {
    long now = System.nanoTime();
    long created = now - 10_000_000_000L;
    long lastActivity = now - 60_000_000_000L;
    long maxLifetime = 300_000_000_000L;
    long idleTimeout = 30_000_000_000L;

    assertEqual(
        PgPipelinePool.checkReplaceReason(created, lastActivity, 3, maxLifetime, idleTimeout, now),
        PgPipelinePool.ReplaceReason.NONE);
  }

  @Test
  public void replaceReasonBothDisabled() {
    long now = System.nanoTime();
    long created = now - 999_000_000_000L;
    long lastActivity = now - 999_000_000_000L;

    assertEqual(
        PgPipelinePool.checkReplaceReason(created, lastActivity, 0, 0, 0, now),
        PgPipelinePool.ReplaceReason.NONE);
  }

  @Test
  public void replaceReasonMaxLifetimeTakesPrecedence() {
    long now = System.nanoTime();
    long created = now - 120_000_000_000L;
    long lastActivity = now - 120_000_000_000L;
    long maxLifetime = 60_000_000_000L;
    long idleTimeout = 30_000_000_000L;

    assertEqual(
        PgPipelinePool.checkReplaceReason(created, lastActivity, 0, maxLifetime, idleTimeout, now),
        PgPipelinePool.ReplaceReason.MAX_LIFETIME);
  }

  @Test
  public void replaceReasonExactBoundaryNotTriggered() {
    long now = System.nanoTime();
    long maxLifetime = 60_000_000_000L;
    long created = now - maxLifetime; // exactly at boundary

    assertEqual(
        PgPipelinePool.checkReplaceReason(created, now, 0, maxLifetime, 0, now),
        PgPipelinePool.ReplaceReason.NONE);
  }

  @Test
  public void replaceReasonOnePastBoundary() {
    long now = System.nanoTime();
    long maxLifetime = 60_000_000_000L;
    long created = now - maxLifetime - 1; // 1 nano past boundary

    assertEqual(
        PgPipelinePool.checkReplaceReason(created, now, 0, maxLifetime, 0, now),
        PgPipelinePool.ReplaceReason.MAX_LIFETIME);
  }

  // ========== SSL wire bytes verification ==========

  @Test
  public void sslRequestWireBytes() {
    byte[] expected = {0, 0, 0, 8, 4, (byte) 210, 22, 47};

    int length =
        ((expected[0] & 0xFF) << 24)
            | ((expected[1] & 0xFF) << 16)
            | ((expected[2] & 0xFF) << 8)
            | (expected[3] & 0xFF);
    assertEqual(length, 8);

    int code =
        ((expected[4] & 0xFF) << 24)
            | ((expected[5] & 0xFF) << 16)
            | ((expected[6] & 0xFF) << 8)
            | (expected[7] & 0xFF);
    assertEqual(code, 80877103);
  }

  // ========== PEM certificate parsing ==========

  @Test
  public void parsePemCertificatesSingleCert() throws Exception {
    BufferedReader reader = new BufferedReader(new StringReader(TEST_CERT_PEM));
    List<Certificate> parsed = PgSslContextFactory.parsePemCertificates(reader);
    assertEqual(parsed.size(), 1);
    assertTrue(parsed.get(0) instanceof java.security.cert.X509Certificate);
  }

  @Test
  public void parsePemCertificatesMultipleCerts() throws Exception {
    String doublePem = TEST_CERT_PEM + "\n" + TEST_CERT_PEM;
    BufferedReader reader = new BufferedReader(new StringReader(doublePem));
    List<Certificate> parsed = PgSslContextFactory.parsePemCertificates(reader);
    assertEqual(parsed.size(), 2);
  }

  @Test(expected = PgPipelineException.class)
  public void parsePemCertificatesNoCerts() throws Exception {
    BufferedReader reader = new BufferedReader(new StringReader("just some text\nno certs here\n"));
    PgSslContextFactory.parsePemCertificates(reader);
  }

  @Test
  public void parsePemCertificatesIgnoresExtraText() throws Exception {
    String pem = "# Comment line\nSome header text\n" + TEST_CERT_PEM + "\n# Trailing text\n";
    BufferedReader reader = new BufferedReader(new StringReader(pem));
    List<Certificate> parsed = PgSslContextFactory.parsePemCertificates(reader);
    assertEqual(parsed.size(), 1);
  }

  @Test
  public void parsePemCertificateSubjectDn() throws Exception {
    BufferedReader reader = new BufferedReader(new StringReader(TEST_CERT_PEM));
    List<Certificate> parsed = PgSslContextFactory.parsePemCertificates(reader);
    java.security.cert.X509Certificate x509 = (java.security.cert.X509Certificate) parsed.get(0);
    assertTrue(x509.getSubjectX500Principal().getName().contains("CN=Test"));
  }

  // ========== PEM private key parsing ==========

  @Test
  public void parsePemPrivateKeyRsa() throws Exception {
    BufferedReader reader = new BufferedReader(new StringReader(TEST_KEY_PEM));
    PrivateKey parsed = PgSslContextFactory.parsePemPrivateKey(reader);
    assertEqual(parsed.getAlgorithm(), "RSA");
    assertEqual(parsed.getFormat(), "PKCS#8");
  }

  @Test(expected = PgPipelineException.class)
  public void parsePemPrivateKeyNone() throws Exception {
    BufferedReader reader = new BufferedReader(new StringReader("no key here\n"));
    PgSslContextFactory.parsePemPrivateKey(reader);
  }

  @Test
  public void parsePemPrivateKeyWithExtraText() throws Exception {
    String pem = "# header\n" + TEST_KEY_PEM + "\n# footer\n";
    BufferedReader reader = new BufferedReader(new StringReader(pem));
    PrivateKey parsed = PgSslContextFactory.parsePemPrivateKey(reader);
    assertEqual(parsed.getAlgorithm(), "RSA");
  }

  @Test
  public void parsePemPrivateKeyEcDetection() throws Exception {
    // Generate an EC key pair and test that the parser detects the EC header
    java.security.KeyPairGenerator kpg = java.security.KeyPairGenerator.getInstance("EC");
    kpg.initialize(256);
    java.security.KeyPair kp = kpg.generateKeyPair();

    String b64 = java.util.Base64.getEncoder().encodeToString(kp.getPrivate().getEncoded());
    StringBuilder wrapped = new StringBuilder();
    for (int i = 0; i < b64.length(); i += 64) {
      if (i > 0) wrapped.append('\n');
      wrapped.append(b64, i, Math.min(i + 64, b64.length()));
    }
    String pem = "-----BEGIN EC PRIVATE KEY-----\n" + wrapped + "\n-----END EC PRIVATE KEY-----\n";

    BufferedReader reader = new BufferedReader(new StringReader(pem));
    PrivateKey parsed = PgSslContextFactory.parsePemPrivateKey(reader);
    assertEqual(parsed.getAlgorithm(), "EC");
    assertEqual(parsed, kp.getPrivate());
  }

  // ========== PgPipelineConfig validation ==========

  @Test
  public void configDefaults() {
    PgPipelineConfig config = PgPipelineConfig.builder().build();
    assertEqual(config.connectionCount(), 10);
    assertEqual(config.pipeliningLimit(), 256);
    assertEqual(config.preparedStatementCacheSize(), 256);
    assertEqual(config.sendQueueCapacity(), 1024);
    assertEqual(config.sslMode(), PgPipelineSslMode.DISABLE);
    assertEqual(config.maxTransactionConnections(), 10);
  }

  @Test
  public void configSingleConnection() {
    PgPipelineConfig config = PgPipelineConfig.builder().connectionCount(1).build();
    assertEqual(config.maxTransactionConnections(), 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void configMaxTransactionExceedsConnectionCount() {
    PgPipelineConfig.builder().connectionCount(5).maxTransactionConnections(10).build();
  }

  @Test
  public void configExplicitMaxTransaction() {
    PgPipelineConfig config =
        PgPipelineConfig.builder().connectionCount(10).maxTransactionConnections(3).build();
    assertEqual(config.maxTransactionConnections(), 3);
  }

  @Test
  public void configMaxTransactionEqualsConnectionCount() {
    PgPipelineConfig config =
        PgPipelineConfig.builder().connectionCount(5).maxTransactionConnections(5).build();
    assertEqual(config.maxTransactionConnections(), 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void configMaxTransactionZero() {
    PgPipelineConfig.builder().connectionCount(5).maxTransactionConnections(0).build();
  }

  @Test
  public void configDefaultExhaustionStrategy() {
    PgPipelineConfig config = PgPipelineConfig.builder().build();
    assertEqual(config.exhaustionStrategy(), PgExhaustionStrategy.BLOCK);
  }

  @Test
  public void configDefaultFetchSize() {
    PgPipelineConfig config = PgPipelineConfig.builder().build();
    assertEqual(config.defaultFetchSize(), 1000);
  }

  @Test
  public void configExplicitFetchSize() {
    PgPipelineConfig config = PgPipelineConfig.builder().defaultFetchSize(500).build();
    assertEqual(config.defaultFetchSize(), 500);
  }

  @Test
  public void configExplicitExhaustionStrategy() {
    PgPipelineConfig config =
        PgPipelineConfig.builder().exhaustionStrategy(PgExhaustionStrategy.THROW).build();
    assertEqual(config.exhaustionStrategy(), PgExhaustionStrategy.THROW);
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
