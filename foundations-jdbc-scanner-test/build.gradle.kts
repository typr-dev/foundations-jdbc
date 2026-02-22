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

tasks.test {
    useJUnitPlatform()
}

dependencies {
    testImplementation(project(":foundations-jdbc"))
    testImplementation(project(":foundations-jdbc-kotlin"))
    testImplementation(project(":foundations-jdbc-scala_3"))
    testImplementation(project(":documentation-examples-java"))
    testImplementation(project(":documentation-examples-kotlin"))
    testImplementation(project(":documentation-examples-scala"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.postgresql:postgresql:${property("postgresqlDriverVersion")}")
    testRuntimeOnly("org.mariadb.jdbc:mariadb-java-client:${property("mariadbDriverVersion")}")
    testRuntimeOnly("org.duckdb:duckdb_jdbc:${property("duckdbDriverVersion")}")
    testRuntimeOnly("com.oracle.database.jdbc:ojdbc11:${property("oracleDriverVersion")}")
    testRuntimeOnly("com.microsoft.sqlserver:mssql-jdbc:${property("sqlserverDriverVersion")}")
    testRuntimeOnly("com.ibm.db2:jcc:${property("db2DriverVersion")}")
}
