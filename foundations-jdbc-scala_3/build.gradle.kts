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
            setSrcDirs(listOf("../foundations-jdbc-scala/src/scala"))
        }
    }
}

dependencies {
    api(project(":foundations-jdbc"))
    api("org.scala-lang:scala3-library_3:${property("scala3Version")}")
}
