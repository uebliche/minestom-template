
plugins {
    java
    application
}

val javaVersion = System.getenv("JAVA_VERSION") ?: "21"

group = "net.minestom"

dependencies {
    implementation(rootProject)

    runtimeOnly(libs.bundles.logger)
}

application {
    mainClass.set("net.uebliche.demo.Main")
}