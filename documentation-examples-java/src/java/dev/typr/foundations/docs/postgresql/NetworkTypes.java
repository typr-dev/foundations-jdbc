package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.data.Cidr;
import dev.typr.foundations.data.Inet;

@SuppressWarnings("unused")
public class NetworkTypes {
  // start
  PgType<Inet> inetType = PgTypes.inet;
  PgType<Cidr> cidrType = PgTypes.cidr;

  Inet addr = new Inet("192.168.1.1/24");
  // stop
}
