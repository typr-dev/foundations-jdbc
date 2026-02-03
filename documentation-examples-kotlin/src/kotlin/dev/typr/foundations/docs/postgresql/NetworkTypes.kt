package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.PgType
import dev.typr.foundations.PgTypes
import dev.typr.foundations.data.Cidr
import dev.typr.foundations.data.Inet

@Suppress("unused")
class NetworkTypes {
    //start
    val inetType: PgType<Inet> = PgTypes.inet
    val cidrType: PgType<Cidr> = PgTypes.cidr

    val addr: Inet = Inet("192.168.1.1/24")
    //stop
}
