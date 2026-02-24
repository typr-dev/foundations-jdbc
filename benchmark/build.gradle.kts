plugins {
    kotlin("jvm") version "2.3.0"
    application
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

application {
    mainClass.set("dev.typr.foundations.benchmark.BenchmarkRunnerKt")
}

tasks.named<JavaExec>("run") {
    workingDir = rootProject.projectDir
}

dependencies {
    implementation(project(":foundations-jdbc"))
    implementation(project(":foundations-jdbc-kotlin"))
    implementation("org.duckdb:duckdb_jdbc:${property("duckdbDriverVersion")}")
    implementation("org.jdbi:jdbi3-core:3.45.4")
    implementation("org.springframework:spring-jdbc:${property("springVersion")}")
    implementation("org.hibernate.orm:hibernate-core:6.6.4.Final")
}
