package dev.typr.foundations.docs.postgresql

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import dev.typr.kotlinfoundations.data.Cidr
import dev.typr.kotlinfoundations.data.Inet

@Suppress("unused")
class NetworkTypes {
    //start
    val inetType: PgType<Inet> = PgTypes.inet
    val cidrType: PgType<Cidr> = PgTypes.cidr

    val addr: Inet = Inet("192.168.1.1/24")
    //stop
}
