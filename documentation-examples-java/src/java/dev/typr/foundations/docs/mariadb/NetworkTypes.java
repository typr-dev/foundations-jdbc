package dev.typr.foundations.docs.mariadb;

import dev.typr.foundations.MariaType;
import dev.typr.foundations.MariaTypes;
import dev.typr.foundations.data.maria.Inet4;
import dev.typr.foundations.data.maria.Inet6;

@SuppressWarnings("unused")
public class NetworkTypes {
  // start
  MariaType<Inet4> inet4Type = MariaTypes.inet4;
  MariaType<Inet6> inet6Type = MariaTypes.inet6;

  Inet4 ipv4 = Inet4.parse("192.168.1.1");
  Inet6 ipv6 = Inet6.parse("::1");
  // stop
}
