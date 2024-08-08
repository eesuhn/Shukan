package shared

import Constants
import com.android.build.api.dsl.CompileOptions
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

fun Project.setJvmToolchain() {
    extensions.configure<KotlinProjectExtension> {
        jvmToolchain {
            languageVersion.set(Constants.javaToolchainVersion)
        }
    }
}

fun KotlinJvmOptions.setAndroidJvmTarget() {
    jvmTarget = Constants.javaToolchainVersion.toString()
}

fun CompileOptions.setJvmTargets() {
    sourceCompatibility = JavaVersion.toVersion(Constants.javaToolchainVersion)
    targetCompatibility = JavaVersion.toVersion(Constants.javaToolchainVersion)
    isCoreLibraryDesugaringEnabled = true
}