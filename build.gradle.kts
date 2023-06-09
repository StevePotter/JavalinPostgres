plugins {
    kotlin("jvm") version "1.8.10"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

version = "1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(19)
}

application {
    mainClass.set("AppKt")
}

tasks {
    withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
        archiveBaseName.set("app")
        archiveClassifier.set("")
        archiveVersion.set("")
    }

    build {
        dependsOn(shadowJar)
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    implementation("io.javalin:javalin:5.4.2")
    implementation("org.slf4j:slf4j-simple:2.0.7")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.2")
    implementation("org.ktorm:ktorm-core:3.6.0")
    implementation("org.ktorm:ktorm-support-postgresql:3.6.0")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("commons-validator:commons-validator:1.7")
    implementation("io.konform:konform-jvm:0.4.0")
    implementation("org.passay:passay:1.6.3")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}