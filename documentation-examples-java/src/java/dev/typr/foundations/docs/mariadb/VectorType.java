package dev.typr.foundations.docs.mariadb;

import dev.typr.foundations.MariaType;
import dev.typr.foundations.MariaTypes;
import dev.typr.foundations.data.Vector;

@SuppressWarnings("unused")
public class VectorType {
  // start
  MariaType<Vector> embedding = MariaTypes.vector(1536);
  // stop
}
