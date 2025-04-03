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

dependencies{
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")
    implementation("io.javalin:javalin:5.6.3")
    testImplementation("io.mockk:mockk:1.13.7")
    implementation("org.slf4j:slf4j-simple:2.0.7")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation("org.mockito:mockito-core:5.9.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")
    testImplementation(kotlin("test"))
    implementation("com.squareup.okhttp3:okhttp:4.9.2")
}

tasks.register("prepareKotlinBuildScriptModel"){}


tasks.test {
    useJUnitPlatform()
}