import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.apollo.graphql)
}

android {
    namespace = "com.fysiki.workoutkitsdkdemo"
    compileSdk = 34
    buildFeatures.buildConfig = true

    defaultConfig {
        applicationId = "com.fysiki.workoutkitsdkdemo"
        minSdk = 21
        targetSdk = 35
        versionCode = System.getenv("GITHUB_VERSION_CODE")?.toInt() ?: 1
        versionName = System.getenv("GITHUB_VERSION") ?: "v16"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true

        val properties = Properties()
        try {
            val localProperties = rootProject.layout.projectDirectory.file("local.properties").asFile
            properties.load(localProperties.inputStream())
            val rawUrl = properties.getProperty("GraphQLServerUrl", "")
            val quotedUrl = "\"$rawUrl\""
            buildConfigField("String", "CLOUD_URL", quotedUrl)
        } catch (e: Exception) {
            buildConfigField("String", "CLOUD_URL", "\"\"") // Empty string fallback
            project.logger.warn("Failed to load GraphQLServerUrl from local.properties: ${e.message}")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }

    // Local
    flavorDimensions += "service"
    productFlavors {
        create("Playstore") {
            dimension = "service"
            isDefault = true
        }
    }
}

apollo {

    val properties = Properties()
    val file = project.rootProject.file("local.properties")
    if (file.exists()) {
        properties.load(file.inputStream())
    }

    service("service") {
        packageName.set("com.fysiki.workoutkitsdkdemo")

        srcDir("src/main/graphql")

        // configure introspection schema download
        introspection {
            // Path to your schema file (will be downloaded here)
            schemaFile.set(file("src/main/graphql/schema.graphqls"))
            endpointUrl.set(properties.getProperty("GraphQLServerUrl"))
            headers.set(mapOf("X-Developer-Authorization-Key" to properties.getProperty("GraphQLAuthKey")))
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment.compose)
    implementation(libs.runtime.livedata)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.glide)
    implementation(libs.annotations)
    implementation(libs.compose)

    implementation(libs.androidx.multidex)
    implementation(libs.androidx.ui.viewbinding)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.apollo.graphql)


    version = System.getenv("GITHUB_VERSION") ?: "v16"

    api("com.fysiki:workoutkit:$version")

    /************************************************************************************************
     * Use local aar file (Needs the import of all libraries used by WorkoutKit)
     ******************************************************************************************/

    //implementation(files("libs/workoutkit-Playstore-release.aar"))
}