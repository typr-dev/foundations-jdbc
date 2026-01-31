plugins {
    java
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

sourceSets {
    test {
        java {
            setSrcDirs(listOf("src/java"))
        }
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-proc:none")
}

dependencies {
    testImplementation(project(":foundations-jdbc-hikari"))
    testImplementation("junit:junit:${property("junitVersion")}")
    testImplementation("org.postgresql:postgresql:${property("postgresqlDriverVersion")}")
    testImplementation("org.mariadb.jdbc:mariadb-java-client:${property("mariadbDriverVersion")}")
    testImplementation("org.duckdb:duckdb_jdbc:${property("duckdbDriverVersion")}")
    testImplementation("com.oracle.database.jdbc:ojdbc11:${property("oracleDriverVersion")}")
    testImplementation("com.microsoft.sqlserver:mssql-jdbc:${property("sqlserverDriverVersion")}")
    testImplementation("com.ibm.db2:jcc:${property("db2DriverVersion")}")
}
