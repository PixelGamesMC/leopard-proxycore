import java.io.ByteArrayOutputStream

plugins {
    kotlin("jvm") version "1.8.0"

    kotlin("plugin.serialization") version "1.8.10"

    // Shades and relocates dependencies into our plugin jar. See https://imperceptiblethoughts.com/shadow/introduction/
    id("com.github.johnrengelman.shadow") version "7.1.2"

    id("maven-publish")
}

val gitDescribe: String by lazy {
    val stdout = ByteArrayOutputStream()
    rootProject.exec {
        commandLine("git", "rev-parse", "--verify", "--short", "HEAD")
        standardOutput = stdout
    }
    stdout.toString().trim()
}

group = "eu.pixelgamesmc.minecraft"
version = gitDescribe

val pixelUsername: String by project
val pixelPassword: String by project

repositories {
    maven {
        credentials {
            username = pixelUsername
            password = pixelPassword
        }

        url = uri("https://repository.pixelgamesmc.eu/releases")
    }
}

dependencies {
    testImplementation(kotlin("test"))

    api("redis.clients:jedis:4.3.2")
    api("org.litote.kmongo:kmongo:4.8.0")
    api("org.litote.kmongo:kmongo-serialization:4.8.0")
    api("org.litote.kmongo:kmongo-id-serialization:4.8.0")
    api("net.kyori:adventure-platform-bungeecord:4.3.0")

    compileOnly("eu.thesimplecloud.simplecloud:simplecloud-plugin:2.4.1")
    compileOnly("io.github.waterfallmc:waterfall-api:1.19-R0.1-SNAPSHOT")
}

kotlin {
    jvmToolchain(17)
}

tasks {
    test {
        useJUnitPlatform()
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything

        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release.set(17)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
    }

    /*
    reobfJar {
      // This is an example of how you might change the output location for reobfJar. It's recommended not to do this
      // for a variety of reasons, however it's asked frequently enough that an example of how to do it is included here.
      outputJar.set(layout.buildDirectory.file("libs/PaperweightTestPlugin-${project.version}.jar"))
    }*/

    shadowJar {
        // helper function to relocate a package into our package

        fun relocate(pkg: String) = relocate(pkg, "eu.pixelgamesmc.dependency.$pkg")

        // relocate cloud, and its transitive dependencies
        //relocate("com.mongodb")
    }
}

publishing {
    publications {
        create<MavenPublication>("Version") {
            from(components["java"])

            artifact(tasks.kotlinSourcesJar.get())
        }

        create<MavenPublication>("Default") {
            version = "-SNAPSHOT"

            from(components["java"])

            artifact(tasks.kotlinSourcesJar.get())
        }
    }

    repositories {
        maven {
            credentials {
                username = pixelUsername
                password = pixelPassword
            }

            url = uri("https://repository.pixelgamesmc.eu/releases")
        }
    }
}