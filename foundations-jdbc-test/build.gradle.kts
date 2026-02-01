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
        resources {
            setSrcDirs(listOf("src/resources"))
        }
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-proc:none")
}

tasks.test {
    // Configure Docker socket for Testcontainers on macOS
    val dockerSocket = file("${System.getProperty("user.home")}/.docker/run/docker.sock")
    if (dockerSocket.exists()) {
        environment("DOCKER_HOST", "unix://${dockerSocket.absolutePath}")
        environment("TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE", dockerSocket.absolutePath)
    }
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

    // Testcontainers
    testImplementation("org.testcontainers:testcontainers:${property("testcontainersVersion")}")
    testImplementation("org.testcontainers:postgresql:${property("testcontainersVersion")}")
    testImplementation("org.testcontainers:mariadb:${property("testcontainersVersion")}")
    testImplementation("org.testcontainers:mssqlserver:${property("testcontainersVersion")}")
    testImplementation("org.testcontainers:oracle-free:${property("testcontainersVersion")}")
    testImplementation("org.testcontainers:db2:${property("testcontainersVersion")}")

    // Logging for Testcontainers debug
    testRuntimeOnly("org.slf4j:slf4j-simple:2.0.7")
}
