plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.flatcode.multiwave"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.flatcode.multiwave"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":library"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    //Layout
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
}