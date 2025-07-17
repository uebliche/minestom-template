import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.compile.JavaCompile

plugins {
    id("java-library")
    id("nl.littlerobots.version-catalog-update") version "1.0.0"
    id("maven-publish")
}

group = "net.uebliche"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(24)
}

tasks.test {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
        name = "jitpack"
    }
    maven {
        url = uri("https://repo.codemc.io/repository/maven-public/")
        name = "codemc"
    }
    maven {
        url = uri("https://repo.hypera.dev/snapshots/")
        name = "hypera"
    }
}

dependencies {
    api(libs.jetbrainsAnnotations)
    api(libs.minestom)
    api(libs.minimessage)
    api(libs.mongodb)
  //  implementation(libs.mongodb)
    api(libs.polar)
    api(libs.schem)
    api(libs.terra)
    api(libs.bundles.logger)
    api("dev.lu15:simple-voice-chat-minestom:0.2.0-SNAPSHOT")
    api("com.github.TogAr2:MinestomPvP:-SNAPSHOT")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}