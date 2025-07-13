import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.compile.JavaCompile

plugins {
    id("java-library")
    id("nl.littlerobots.version-catalog-update") version "1.0.0"
}

group = "net.uebliche"
version = "1.0-SNAPSHOT"

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
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(24)
}

dependencies {
    api(libs.jetbrainsAnnotations)
    api(libs.minestom)
    api(libs.minimessage)
    api(libs.mongodb)
    implementation(libs.mongodb)
    api(libs.polar)
    api(libs.schem)
    api(libs.terra)
    api(libs.bundles.logger)
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}