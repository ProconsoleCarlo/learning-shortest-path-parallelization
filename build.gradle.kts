plugins {
    java
    kotlin("jvm") version "2.0.0" apply false
    `maven-publish`
    jacoco
    id("org.sonarqube") version "5.0.0.4638" apply false
}

allprojects {
    group = "it.proconsole.learning.shortestpath.parallelization"
    version = "2.0.0"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "jacoco")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    jacoco {
        toolVersion = "0.8.11"
    }

    tasks.jacocoTestReport {
        dependsOn(tasks.test)
        reports {
            xml.required = true
            html.required = true
        }
    }
}

