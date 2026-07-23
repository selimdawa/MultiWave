plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.vanniktech)
}

android {
    namespace = "io.selimdawa.multiwave"
    compileSdk = 37

    defaultConfig {
        minSdk = 24

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

mavenPublishing {
    coordinates(groupId = "io.github.selimdawa", artifactId = "multi-wave", version = "1.0.0")

    publishToMavenCentral(automaticRelease = true)

    if (!System.getenv("JITPACK").isNullOrEmpty()) {
        // Skip signing on JitPack
    } else {
        signAllPublications()
    }

    pom {
        name.set("Multi Wave")
        description.set("A powerful and customizable multi-layered wave animation header for Android, featuring gradient support and smooth wave effects.")

        url.set("https://github.com/selimdawa/MultiWave")

        licenses {
            license {
                name.set("Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("selimdawa")
                name.set("Selim Dawa")
                email.set("selimdawa@gmail.com")
            }
        }

        scm {
            url.set("https://github.com/selimdawa/MultiWave")
            connection.set("scm:git:https://github.com/selimdawa/MultiWave.git")
            developerConnection.set("scm:git:ssh://git@github.com:selimdawa/MultiWave.git")
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
}