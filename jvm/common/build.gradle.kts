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
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:$mockitoVersion")

    // Annotations
    compileOnly("org.jspecify:jspecify:$jSpecifyVersion")
}

tasks.jar {
    archiveBaseName = "shortestpath-common"
}

tasks.test {
    useJUnitPlatform()
}

