package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.{PgType, PgTypes}
import dev.typr.foundations.data.{Cidr, Inet}

@SuppressWarnings(Array("unused"))
object NetworkTypes:
  //start
  val inetType: PgType[Inet] = PgTypes.inet
  val cidrType: PgType[Cidr] = PgTypes.cidr

  val addr: Inet = new Inet("192.168.1.1/24")
  //stop
