package dev.typr.foundations.pg;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Builds JDK {@link SSLContext} instances from PgPipelineConfig SSL settings. Zero external
 * dependencies — uses only {@code java.security} and {@code javax.net.ssl}.
 */
final class PgSslContextFactory {

  static SSLContext createSslContext(PgPipelineConfig config) {
    try {
      PgPipelineSslMode mode = config.sslMode();

      TrustManager[] trustManagers;
      if (mode == PgPipelineSslMode.REQUIRE) {
        trustManagers = new TrustManager[] {TRUST_ALL};
      } else {
        // VERIFY_CA or VERIFY_FULL — load CA cert
        String rootCertPath = config.sslRootCert();
        if (rootCertPath == null || rootCertPath.isEmpty()) {
          throw new PgPipelineException("sslRootCert is required for sslMode=" + mode);
        }
        trustManagers = buildCaTrustManagers(rootCertPath);
      }

      KeyManager[] keyManagers = null;
      if (config.sslCert() != null && config.sslKey() != null) {
        keyManagers = buildClientKeyManagers(config.sslCert(), config.sslKey());
      }

      SSLContext ctx = SSLContext.getInstance("TLS");
      ctx.init(keyManagers, trustManagers, null);
      return ctx;
    } catch (GeneralSecurityException | IOException e) {
      throw new PgPipelineException("Failed to create SSL context: " + e.getMessage(), e);
    }
  }

  private static TrustManager[] buildCaTrustManagers(String caCertPath)
      throws GeneralSecurityException, IOException {
    List<Certificate> certs = loadPemCertificates(caCertPath);
    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
    trustStore.load(null, null);
    for (int i = 0; i < certs.size(); i++) {
      trustStore.setCertificateEntry("ca-" + i, certs.get(i));
    }
    TrustManagerFactory tmf =
        TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(trustStore);
    return tmf.getTrustManagers();
  }

  private static KeyManager[] buildClientKeyManagers(String certPath, String keyPath)
      throws GeneralSecurityException, IOException {
    List<Certificate> certs = loadPemCertificates(certPath);
    PrivateKey privateKey = loadPemPrivateKey(keyPath);

    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(null, null);
    keyStore.setKeyEntry("client", privateKey, new char[0], certs.toArray(new Certificate[0]));

    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(keyStore, new char[0]);
    return kmf.getKeyManagers();
  }

  private static List<Certificate> loadPemCertificates(String path)
      throws GeneralSecurityException, IOException {
    try (BufferedReader reader = Files.newBufferedReader(Path.of(path))) {
      return parsePemCertificates(reader);
    }
  }

  static List<Certificate> parsePemCertificates(BufferedReader reader)
      throws GeneralSecurityException, IOException {
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    List<Certificate> certs = new ArrayList<>();
    StringBuilder pem = new StringBuilder();
    boolean inCert = false;

    String line;
    while ((line = reader.readLine()) != null) {
      if (line.contains("BEGIN CERTIFICATE")) {
        inCert = true;
        pem.setLength(0);
      } else if (line.contains("END CERTIFICATE")) {
        inCert = false;
        byte[] der = Base64.getDecoder().decode(pem.toString());
        certs.add(cf.generateCertificate(new ByteArrayInputStream(der)));
      } else if (inCert) {
        pem.append(line.trim());
      }
    }

    if (certs.isEmpty()) {
      throw new PgPipelineException("No certificates found in PEM data");
    }
    return certs;
  }

  private static PrivateKey loadPemPrivateKey(String path)
      throws GeneralSecurityException, IOException {
    try (BufferedReader reader = Files.newBufferedReader(Path.of(path))) {
      return parsePemPrivateKey(reader);
    }
  }

  static PrivateKey parsePemPrivateKey(BufferedReader reader)
      throws GeneralSecurityException, IOException {
    StringBuilder pem = new StringBuilder();
    boolean inKey = false;
    String algorithm = "RSA";

    String line;
    while ((line = reader.readLine()) != null) {
      if (line.contains("BEGIN") && line.contains("PRIVATE KEY")) {
        inKey = true;
        if (line.contains("EC")) algorithm = "EC";
        pem.setLength(0);
      } else if (line.contains("END") && line.contains("PRIVATE KEY")) {
        inKey = false;
      } else if (inKey) {
        pem.append(line.trim());
      }
    }

    if (pem.isEmpty()) {
      throw new PgPipelineException("No private key found in PEM data");
    }

    byte[] der = Base64.getDecoder().decode(pem.toString());
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(der);
    KeyFactory kf = KeyFactory.getInstance(algorithm);
    return kf.generatePrivate(spec);
  }

  private static final X509TrustManager TRUST_ALL =
      new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
          return new X509Certificate[0];
        }
      };
}
