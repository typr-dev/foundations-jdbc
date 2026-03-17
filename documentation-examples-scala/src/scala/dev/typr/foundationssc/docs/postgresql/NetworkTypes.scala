package dev.typr.foundationssc.docs.postgresql
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object NetworkTypes:
  // start
  val inetType: PgType[Inet] = PgTypes.inet
  val cidrType: PgType[Cidr] = PgTypes.cidr

  val addr: Inet = new Inet("192.168.1.1/24")
  // stop
