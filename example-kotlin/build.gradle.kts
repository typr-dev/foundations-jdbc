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
    mainClass.set("dev.typr.foundations.example.MainKt")
}

dependencies {
    implementation(project(":foundations-jdbc"))
    implementation(project(":foundations-jdbc-kotlin"))
    implementation("org.duckdb:duckdb_jdbc:${property("duckdbDriverVersion")}")
}
