plugins {
    kotlin("jvm") version "1.9.22"
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
        kotlin {
            setSrcDirs(listOf("src/kotlin"))
        }
    }
}

dependencies {
    implementation(project(":foundations-jdbc"))
    implementation(project(":foundations-jdbc-kotlin"))
    implementation(project(":foundations-jdbc-hikari"))
    compileOnly(project(":foundations-jdbc-spring"))
    compileOnly("org.springframework:spring-context:${property("springVersion")}")
    compileOnly("org.springframework:spring-tx:${property("springVersion")}")

    compileOnly("org.postgresql:postgresql:${property("postgresqlDriverVersion")}")
    compileOnly("org.mariadb.jdbc:mariadb-java-client:${property("mariadbDriverVersion")}")
    compileOnly("org.duckdb:duckdb_jdbc:${property("duckdbDriverVersion")}")
    compileOnly("com.oracle.database.jdbc:ojdbc11:${property("oracleDriverVersion")}")
    compileOnly("com.microsoft.sqlserver:mssql-jdbc:${property("sqlserverDriverVersion")}")
    compileOnly("com.ibm.db2:jcc:${property("db2DriverVersion")}")
}
