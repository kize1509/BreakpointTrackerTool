plugins {
    kotlin("jvm") version "1.9.25"
}

group = "org.example"
version = "unspecified"
kotlin {
    jvmToolchain(17)
}
repositories {
    mavenCentral()
}

tasks.register("prepareKotlinBuildScriptModel"){}


tasks.test {
    useJUnitPlatform()
}