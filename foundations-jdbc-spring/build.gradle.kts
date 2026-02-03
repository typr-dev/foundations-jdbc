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
        resources {
            setSrcDirs(listOf("src/resources"))
        }
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-proc:none")
}

dependencies {
    api(project(":foundations-jdbc"))
    compileOnly("org.springframework:spring-jdbc:${property("springVersion")}")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure:${property("springBootVersion")}")
}
