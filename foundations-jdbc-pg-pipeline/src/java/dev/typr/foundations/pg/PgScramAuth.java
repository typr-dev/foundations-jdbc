package dev.typr.foundations.pg;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * SCRAM-SHA-256 authentication for PostgreSQL. Uses only JDK standard library classes — zero
 * external dependencies.
 */
final class PgScramAuth {

  private final String username;
  private final String password;
  private final String clientNonce;
  private String clientFirstMessageBare;
  private byte[] serverSignature;

  PgScramAuth(String username, String password) {
    this.username = username;
    this.password = password;
    byte[] nonceBytes = new byte[24];
    new SecureRandom().nextBytes(nonceBytes);
    this.clientNonce = Base64.getEncoder().encodeToString(nonceBytes);
  }

  byte[] clientFirstMessage() {
    clientFirstMessageBare = "n=" + saslPrep(username) + ",r=" + clientNonce;
    String clientFirstMessage = "n,," + clientFirstMessageBare;
    return clientFirstMessage.getBytes(StandardCharsets.UTF_8);
  }

  byte[] clientFinalMessage(byte[] serverFirstMessage) {
    try {
      String serverFirst = new String(serverFirstMessage, StandardCharsets.UTF_8);

      String combinedNonce = null;
      String saltB64 = null;
      int iterations = 0;

      for (String part : serverFirst.split(",")) {
        if (part.startsWith("r=")) combinedNonce = part.substring(2);
        else if (part.startsWith("s=")) saltB64 = part.substring(2);
        else if (part.startsWith("i=")) iterations = Integer.parseInt(part.substring(2));
      }

      if (combinedNonce == null || saltB64 == null || iterations == 0) {
        throw new PgPipelineException("Invalid SCRAM server-first-message: " + serverFirst);
      }

      if (!combinedNonce.startsWith(clientNonce)) {
        throw new PgPipelineException("Server nonce doesn't start with client nonce");
      }

      byte[] salt = Base64.getDecoder().decode(saltB64);

      // SaltedPassword = Hi(password, salt, iterations)
      byte[] saltedPassword = hi(password, salt, iterations);

      // ClientKey = HMAC(SaltedPassword, "Client Key")
      byte[] clientKey = hmac(saltedPassword, "Client Key".getBytes(StandardCharsets.UTF_8));

      // StoredKey = SHA-256(ClientKey)
      byte[] storedKey = sha256(clientKey);

      // AuthMessage = client-first-message-bare + "," + server-first-message + "," +
      // client-final-message-without-proof
      String clientFinalWithoutProof = "c=biws,r=" + combinedNonce;
      String authMessage =
          clientFirstMessageBare + "," + serverFirst + "," + clientFinalWithoutProof;
      byte[] authMessageBytes = authMessage.getBytes(StandardCharsets.UTF_8);

      // ClientSignature = HMAC(StoredKey, AuthMessage)
      byte[] clientSignature = hmac(storedKey, authMessageBytes);

      // ClientProof = ClientKey XOR ClientSignature
      byte[] clientProof = xor(clientKey, clientSignature);

      // ServerKey = HMAC(SaltedPassword, "Server Key")
      byte[] serverKey = hmac(saltedPassword, "Server Key".getBytes(StandardCharsets.UTF_8));

      // ServerSignature = HMAC(ServerKey, AuthMessage)
      this.serverSignature = hmac(serverKey, authMessageBytes);

      String clientFinal =
          clientFinalWithoutProof + ",p=" + Base64.getEncoder().encodeToString(clientProof);
      return clientFinal.getBytes(StandardCharsets.UTF_8);
    } catch (PgPipelineException e) {
      throw e;
    } catch (Exception e) {
      throw new PgPipelineException("SCRAM-SHA-256 authentication failed", e);
    }
  }

  void verifyServerFinal(byte[] serverFinalMessage) {
    String serverFinal = new String(serverFinalMessage, StandardCharsets.UTF_8);
    if (!serverFinal.startsWith("v=")) {
      throw new PgPipelineException("Invalid server-final-message: " + serverFinal);
    }
    byte[] receivedSignature = Base64.getDecoder().decode(serverFinal.substring(2));
    if (!MessageDigest.isEqual(receivedSignature, serverSignature)) {
      throw new PgPipelineException("Server signature verification failed");
    }
  }

  // ========== MD5 authentication ==========

  static String md5Password(String user, String password, byte[] salt) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(password.getBytes(StandardCharsets.UTF_8));
      md.update(user.getBytes(StandardCharsets.UTF_8));
      String inner = hexEncode(md.digest());
      md.reset();
      md.update(inner.getBytes(StandardCharsets.UTF_8));
      md.update(salt);
      return "md5" + hexEncode(md.digest());
    } catch (Exception e) {
      throw new PgPipelineException("MD5 authentication failed", e);
    }
  }

  // ========== Crypto helpers (JDK standard library) ==========

  private static byte[] hi(String password, byte[] salt, int iterations) throws Exception {
    PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, 256);
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    return factory.generateSecret(spec).getEncoded();
  }

  private static byte[] hmac(byte[] key, byte[] data) throws Exception {
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(new SecretKeySpec(key, "HmacSHA256"));
    return mac.doFinal(data);
  }

  private static byte[] sha256(byte[] data) throws Exception {
    return MessageDigest.getInstance("SHA-256").digest(data);
  }

  private static byte[] xor(byte[] a, byte[] b) {
    byte[] result = new byte[a.length];
    for (int i = 0; i < a.length; i++) {
      result[i] = (byte) (a[i] ^ b[i]);
    }
    return result;
  }

  private static String hexEncode(byte[] bytes) {
    var sb = new StringBuilder(bytes.length * 2);
    for (byte b : bytes) {
      sb.append(Character.forDigit((b >> 4) & 0xF, 16));
      sb.append(Character.forDigit(b & 0xF, 16));
    }
    return sb.toString();
  }

  private static String saslPrep(String input) {
    return input; // simplified — full SASLprep is RFC 4013
  }
}
