# WorkoutKit Android SDK
This repository includes WorkoutKit packages and a demo app.

## Requirements
WorkoutKit SDK supports Android 5.0 or higher (API level 21).

## Installation
**1. Add the JitPack repository to your build file**

Add the following to your `allprojects` section in the `build.gradle` file:

```gradle
allprojects {
    repositories {
        ...
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/fysiki/workoutkit-android-sdk")
        }
    }
}
```

**2. Add the dependency**

Check the available library packages [here](https://github.com/orgs/fysiki/packages?repo_name=workoutkit-android-sdk).
```gradle
dependencies {
    implementation 'com.fizzup:workoutkit:v1'
}
```

## Documentation
1. ### **📱 [Integration guide](docs/integration.md)**
2. ### **📋 [Options](docs/options.md)**  

___

FizzUp WorkoutKit is a complete workout module that can be inserted into your Health App.\
To subscribe to FizzUp WorkoutKit contact business@fizzup.com.
