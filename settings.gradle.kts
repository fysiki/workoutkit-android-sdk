import java.util.Properties

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}
dependencyResolutionManagement {
    // Read properties from local.properties
    val properties = Properties()
    val file = File("local.properties")
    if(file.exists())
        properties.load(file.reader())

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/fysiki/workoutkit-android-sdk")
            credentials {
                username = properties.getProperty("GithubUser") ?: System.getenv("GITHUB_USERNAME")
                password = properties.getProperty("GithubToken") ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

rootProject.name = "WorkoutKitSDK"
include(":app")
include(":app:workoutkit")
