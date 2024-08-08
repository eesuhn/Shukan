@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import shared.setAndroidJvmTarget
import shared.setJvmTargets
import shared.setJvmToolchain

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            // Apply common plugins
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.gradle.android.cache-fix")
            }

            // Configure android { ... } block
            extensions.configure<LibraryExtension> {
                compileSdk = Constants.COMPILE_SDK

                defaultConfig {
                    minSdk = Constants.MIN_SDK
                }

                compileOptions {
                    setJvmTargets()
                }

                (this as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") {
                    freeCompilerArgs = freeCompilerArgs + listOf(
                        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    )

                    setAndroidJvmTarget()
                }
            }

            setJvmToolchain()

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                add("coreLibraryDesugaring", libs.findLibrary("gradle.android.desugar").get())
            }
        }
    }
}