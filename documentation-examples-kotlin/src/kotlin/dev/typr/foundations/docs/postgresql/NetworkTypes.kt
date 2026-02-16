package dev.typr.foundations.docs.postgresql

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class NetworkTypes {
    //start
    val inetType: PgType<Inet> = PgTypes.inet
    val cidrType: PgType<Cidr> = PgTypes.cidr

    val addr: Inet = Inet("192.168.1.1/24")
    //stop
}
