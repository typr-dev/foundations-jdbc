plugins {
    `java-library`
    scala
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
        scala {
            setSrcDirs(listOf("../foundations-jdbc-scala_2/src/scala"))
        }
    }
}

dependencies {
    api(project(":foundations-jdbc"))
    api("org.scala-lang:scala-library:${property("scala2Version")}")
}
