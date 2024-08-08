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
import shared.buildComposeMetricsParameters

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            // Configure android { ... } block
            extensions.configure<LibraryExtension> {
                buildFeatures.compose = true

                composeOptions.kotlinCompilerExtensionVersion =
                    libs.findVersion("compose-compiler").get().toString()

                // kotlinOptions { ... }
                (this as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") {
                    freeCompilerArgs = freeCompilerArgs + buildComposeMetricsParameters()
                }
            }

            dependencies {
                add("implementation", platform(libs.findLibrary("compose.bom").get()))
                add("api", libs.findLibrary("compose.material3").get())
                add("implementation", libs.findLibrary("compose.ui.ui").get())
                add("implementation", libs.findLibrary("compose.animation").get())
                add("implementation", libs.findLibrary("compose.ui.toolingpreview").get())
                add("debugImplementation", libs.findLibrary("compose.ui.tooling").get())
                add("implementation", libs.findLibrary("showkase.annotation").get())
            }
        }
    }

}

