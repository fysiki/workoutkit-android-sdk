# WorkoutKit Android SDK
This repository includes WorkoutKit packages and a demo app.

## Requirements
WorkoutKit SDK supports Android 5.0 or higher (API level 21).

## Installation
**1. Add your Github credentials**

Add the following two lines to the `local.properties` file located at the root of your project,
replace the placeholders with your Github account's credentials. This step is necessary to download
Github packages:
```properties
GithubUser=$YOUR_GITHUB_USERNAME
GithubToken=$YOUR_GITHUB_TOKEN
```

**2. Add the Github repository to your build file**

Add the following to your `allprojects` section in the `build.gradle` file:
```gradle
allprojects {
    repositories {
        ...
        maven {
            val properties = Properties()
            val file = File("local.properties")
            if(file.exists())
                properties.load(file.reader())

            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/fysiki/workoutkit-android-sdk")
            credentials {
                username = properties.getProperty("GithubUser")
                password = properties.getProperty("GithubToken")
            }
        }
    }
}
```

**3. Add the dependency**

Check the available library packages [here](https://github.com/orgs/fysiki/packages?repo_name=workoutkit-android-sdk).
```gradle
dependencies {
    implementation 'com.fizzup:workoutkit:v19'
}
```

## Documentation
1. ### **📱 [Integration guide](docs/integration.md)**
2. ### **📋 [Options](docs/options.md)**  

___

FizzUp WorkoutKit is a complete workout module that can be inserted into your Health App.\
To subscribe to FizzUp WorkoutKit contact business@fizzup.com.
