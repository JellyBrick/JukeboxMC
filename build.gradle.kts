import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jmailen.gradle.kotlinter.tasks.FormatTask
import org.jmailen.gradle.kotlinter.tasks.LintTask

plugins {
    kotlin("jvm") version "1.8.20-RC"
    application
    id("org.jmailen.kotlinter") version "3.14.0"
    id("com.github.johnrengelman.shadow") version "8.1.0"
}

repositories {
    mavenCentral()
    maven {
        name = "opencollab-snapshots"
        url = uri("https://repo.opencollab.dev/maven-snapshots/")
    }
    maven {
        name = "opencollab-releases"
        url = uri("https://repo.opencollab.dev/maven-releases/")
    }

    maven {
        name = "jitpack"
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(group = "org.ow2.asm", name = "asm", version = "9.4")
    implementation(group = "org.apache.commons", name = "commons-lang3", version = "3.12.0")
    implementation(group = "com.fasterxml.jackson.core", name = "jackson-core", version = "2.14.2")
    implementation(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.14.2")
    implementation(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin", version = "2.14.2")
    implementation(group = "com.github.Kaooot.Protocol", name = "bedrock-v575", version = "215edc744c") {
        exclude(group = "com.nukkitx.fastutil")
        exclude(group = "io.netty")
    }
    implementation(group = "io.netty", name = "netty-common", version = "4.1.90.Final")
    implementation(group = "io.netty", name = "netty-buffer", version = "4.1.90.Final")
    implementation(group = "io.netty", name = "netty-codec", version = "4.1.90.Final")
    implementation(group = "io.netty", name = "netty-transport", version = "4.1.90.Final")
    implementation(group = "com.google.code.gson", name = "gson", version = "2.10.1")
    implementation(group = "com.googlecode.json-simple", name = "json-simple", version = "1.1.1")
    implementation(group = "it.unimi.dsi", name = "fastutil", version = "8.5.12")
    implementation(group = "net.daporkchop", name = "leveldb-mcpe-jni", version = "0.0.10-SNAPSHOT")
    implementation(group = "com.google.guava", name = "guava", version = "31.1-jre")
    implementation(group = "com.spotify", name = "completable-futures", version = "0.3.5")
    implementation(group = "org.apache.commons", name = "commons-math3", version = "3.6.1")
    implementation(group = "commons-io", name = "commons-io", version = "2.11.0")
    implementation(group = "org.yaml", name = "snakeyaml", version = "2.0")
    implementation(group = "org.apache.logging.log4j", name = "log4j-api", version = "2.20.0")
    implementation(group = "org.apache.logging.log4j", name = "log4j-core", version = "2.20.0")
    implementation(group = "org.jline", name = "jline-terminal", version = "3.23.0")
    implementation(group = "org.jline", name = "jline-terminal-jna", version = "3.23.0")
    implementation(group = "org.jline", name = "jline-reader", version = "3.23.0")
    implementation(group = "net.minecrell", name = "terminalconsoleappender", version = "1.3.0") {
        exclude(group = "org.apache.logging.log4j", module = "log4j-core")
        exclude(group = "org.apache.logging.log4j", module = "log4j-api")
    }

    compileOnly(group = "org.jetbrains", name = "annotations", version = "24.0.1")
}

group = "org.jukeboxmc"
version = "1.0.0-Beta-1"
description = "JukeboxMC"

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = JavaVersion.VERSION_17.toString()
}

tasks.create<LintTask>("ktLint") {
    group = "verification"
    source(files("src"))
}

tasks.create<FormatTask>("ktFormat") {
    group = "formatting"
    source(files("src"))
}

application {
    mainClass.set("org.jukeboxmc.Bootstrap")
}

tasks {
    shadowJar {
        dependsOn("ktLint", distTar, distZip)
        archiveClassifier.set("")
        manifest {
            attributes(
                "Main-Class" to "org.jukeboxmc.Bootstrap",
                "Multi-Release" to true,
            )
        }
    }
}
