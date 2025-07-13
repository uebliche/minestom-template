
plugins {
    java
    application
}

val javaVersion = System.getenv("JAVA_VERSION") ?: "21"

group = "net.minestom"

dependencies {
    implementation(project(":"))

    runtimeOnly(libs.bundles.logger)
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
}

application {
    mainClass.set("net.uebliche.demo.Main")
}