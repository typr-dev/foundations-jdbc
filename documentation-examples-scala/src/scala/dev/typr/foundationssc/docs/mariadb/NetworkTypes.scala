package dev.typr.foundationssc.docs.mariadb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object NetworkTypes:
  //start
  val inet4Type: MariaType[Inet4] = MariaTypes.inet4
  val inet6Type: MariaType[Inet6] = MariaTypes.inet6

  val ipv4: Inet4 = Inet4.parse("192.168.1.1")
  val ipv6: Inet6 = Inet6.parse("::1")
  //stop
