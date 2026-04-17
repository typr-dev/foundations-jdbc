package dev.typr.foundations.docs.mariadb;

import dev.typr.foundations.MariaType;
import dev.typr.foundations.MariaTypes;

@SuppressWarnings("unused")
public class BinaryTypes {
  // start
  MariaType<byte[]> binaryType = MariaTypes.binaryOf(16);
  MariaType<byte[]> varbinaryType = MariaTypes.varbinaryOf(255);
  MariaType<byte[]> blobType = MariaTypes.blob;
  // stop
}
