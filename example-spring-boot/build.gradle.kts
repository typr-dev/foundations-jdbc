plugins {
    java
    id("org.springframework.boot") version "3.4.1"
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
            setSrcDirs(listOf("src/java"))
        }
    }
}

dependencies {
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation(project(":foundations-jdbc-spring"))
    runtimeOnly("org.duckdb:duckdb_jdbc:${property("duckdbDriverVersion")}")
}
