plugins {
    `java-library`
}

allprojects {
    group = property("group") as String
    version = property("version") as String

    repositories {
        mavenCentral()
    }
}
