import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22"
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
}

group = "co.in.acmesense"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.ktor:ktor-client-core:2.3.8")
    implementation("io.ktor:ktor-client-cio:2.3.8")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.8")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") // explicitly add coroutine dependency
    implementation("ch.qos.logback:logback-classic:1.5.3")

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.8")
}


kotlin {
    jvmToolchain(17)
}


tasks.test {
    useJUnitPlatform()
}

application{
    mainClass.set("MainKt")
}

tasks{
    shadowJar{
        archiveBaseName.set("FigmaAPI")
    }
}


