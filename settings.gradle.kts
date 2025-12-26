rootProject.name = "learning-shortest-path-parallelization"

include(
    "jvm:common",
    "jvm:java",
    "jvm:kotlin"
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

