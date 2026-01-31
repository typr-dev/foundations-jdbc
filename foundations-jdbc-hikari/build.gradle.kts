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
            setSrcDirs(listOf("src/java"))
        }
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-proc:none")
}

dependencies {
    api(project(":foundations-jdbc"))
    api("com.zaxxer:HikariCP:${property("hikariVersion")}")
}
