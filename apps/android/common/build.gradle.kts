import org.jetbrains.compose.compose

plugins {
  kotlin("multiplatform")
  id("org.jetbrains.compose")
  id("com.android.library")
  id("org.jetbrains.kotlin.plugin.serialization")
  id("dev.icerock.mobile.multiplatform-resources")
}

group = "chat.simplex"
version = extra["app.version_name"] as String

kotlin {
  android()
  jvm("desktop") {
    jvmToolchain(11)
  }
  sourceSets {
    all {
      languageSettings {
        optIn("kotlinx.coroutines.DelicateCoroutinesApi")
        optIn("androidx.compose.foundation.ExperimentalFoundationApi")
        optIn("androidx.compose.ui.text.ExperimentalTextApi")
        optIn("androidx.compose.material.ExperimentalMaterialApi")
        optIn("com.arkivanov.decompose.ExperimentalDecomposeApi")
        optIn("kotlinx.serialization.InternalSerializationApi")
        optIn("kotlinx.serialization.ExperimentalSerializationApi")
        optIn("androidx.compose.ui.ExperimentalComposeUiApi")
      }
    }

    val commonMain by getting {
      kotlin.srcDir("./build/generated/moko/commonMain/src/")
      dependencies {
        api(compose.runtime)
        api(compose.foundation)
        api(compose.material)
        api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
        api("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
        api("com.russhwolf:multiplatform-settings:1.0.0")
        api("com.charleskorn.kaml:kaml:0.43.0")
        api("dev.icerock.moko:resources:0.22.3")
        api("dev.icerock.moko:resources-compose:0.22.3")
        api("androidx.compose.ui:ui-tooling:${rootProject.extra["compose_version"] as String}")
        implementation("org.jetbrains.compose.components:components-animatedimage:${rootProject.extra["compose_version"] as String}")
        //Barcode
        api("org.boofcv:boofcv-core:0.40.1")
        implementation("com.godaddy.android.colorpicker:compose-color-picker-jvm:0.7.0")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
    val androidMain by getting {
      dependencies {
        api("androidx.appcompat:appcompat:1.5.1")
        api("androidx.core:core-ktx:1.9.0")
        api("androidx.activity:activity-compose:1.5.0")
        val work_version = "2.7.1"
        api("androidx.work:work-runtime-ktx:$work_version")
        api("androidx.work:work-multiprocess:$work_version")
        implementation("com.google.accompanist:accompanist-insets:0.23.0")

        // Video support
        implementation("com.google.android.exoplayer:exoplayer:2.17.1")

        // Biometric authentication
        implementation("androidx.biometric:biometric:1.2.0-alpha04")

        //Barcode
        implementation("org.boofcv:boofcv-android:0.40.1")
      }
    }
    val desktopMain by getting {
      dependencies {
        api(compose.preview)
      }
    }
    val desktopTest by getting
  }
}

android {
  compileSdkVersion(33)
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  defaultConfig {
    minSdkVersion(24)
    targetSdkVersion(33)
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
}

// LALAL
/*
* compose {
    desktop {
        application {
            mainClass = "me.amikhailov.common.MainKt"
            nativeDistributions {
                targetFormats(
                    org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg,
                    org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi,
                    org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
                )
                packageName = "MokoDesktopApp"
                version = "1.0.0"
            }
        }
    }
}
* */

multiplatformResources {
  multiplatformResourcesPackage = "com.icerockdev.library"
//  multiplatformResourcesClassName = "MR"
}