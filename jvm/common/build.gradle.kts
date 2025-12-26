plugins {
    `java-library`
}

val slf4Version = "2.0.17"
val logbackVersion = "1.5.23"
val junitVersion = "5.14.1"
val mockitoVersion = "5.21.0"
val jSpecifyVersion = "1.0.0"

dependencies {
    // API (esposte ai consumer)
    api("org.slf4j:slf4j-api:$slf4Version")

    // Implementation (interne)
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // Test
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-junit-jupiter:$mockitoVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Annotations
    compileOnly("org.jspecify:jspecify:$jSpecifyVersion")
}

tasks.jar {
    archiveBaseName = "shortestpath-common"
}
