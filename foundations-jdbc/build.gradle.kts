plugins {
    `java-library`
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
        java {
            setSrcDirs(listOf("src/java", "generated-and-checked-in"))
        }
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-proc:none")
}

dependencies {
    api("org.jetbrains:annotations:${property("jetbrainsAnnotationsVersion")}")

    compileOnly("org.postgresql:postgresql:${property("postgresqlDriverVersion")}")
    compileOnly("org.mariadb.jdbc:mariadb-java-client:${property("mariadbDriverVersion")}")
    compileOnly("org.duckdb:duckdb_jdbc:${property("duckdbDriverVersion")}")
    compileOnly("com.oracle.database.jdbc:ojdbc11:${property("oracleDriverVersion")}")
    compileOnly("com.microsoft.sqlserver:mssql-jdbc:${property("sqlserverDriverVersion")}")
    compileOnly("com.ibm.db2:jcc:${property("db2DriverVersion")}")
}
