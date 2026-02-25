plugins {
    `java-library`
    `maven-publish`
}

allprojects {
    group = property("group") as String
    version = property("version") as String

    repositories {
        mavenCentral()
    }
}

subprojects {
    pluginManager.withPlugin("java-library") {
        apply(plugin = "maven-publish")
        configure<JavaPluginExtension> {
            withSourcesJar()
        }
        configure<PublishingExtension> {
            publications {
                create<MavenPublication>("maven") {
                    from(components["java"])
                }
            }
        }
    }
}
