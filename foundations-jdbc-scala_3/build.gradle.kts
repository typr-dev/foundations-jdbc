plugins {
    `java-library`
    scala
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

sourceSets {
    main {
        scala {
            setSrcDirs(listOf("../foundations-jdbc-scala/src/scala"))
        }
    }
}

dependencies {
    api(project(":foundations-jdbc"))
    api("org.scala-lang:scala3-library_3:${property("scala3Version")}")

    compileOnly("org.postgresql:postgresql:${property("postgresqlDriverVersion")}")
    compileOnly("org.mariadb.jdbc:mariadb-java-client:${property("mariadbDriverVersion")}")
    compileOnly("org.duckdb:duckdb_jdbc:${property("duckdbDriverVersion")}")
    compileOnly("com.oracle.database.jdbc:ojdbc11:${property("oracleDriverVersion")}")
    compileOnly("com.microsoft.sqlserver:mssql-jdbc:${property("sqlserverDriverVersion")}")
    compileOnly("com.ibm.db2:jcc:${property("db2DriverVersion")}")
}
