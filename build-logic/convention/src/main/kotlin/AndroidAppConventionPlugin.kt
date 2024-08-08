import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import shared.buildComposeMetricsParameters
import shared.setAndroidJvmTarget
import shared.setJvmTargets
import shared.setJvmToolchain

class AndroidAppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            // Configure android { ... } block
            extensions.configure<AppExtension> {
                compileSdkVersion = "android-${Constants.COMPILE_SDK}"

                defaultConfig {
                    minSdk = Constants.MIN_SDK
                    targetSdk = Constants.TARGET_SDK

                    versionCode = Constants.VERSION_CODE
                    versionName = Constants.VERSION_NAME
                }

                compileOptions {
                    setJvmTargets()
                }

                // kotlinOptions { ... }
                (this as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") {
                    freeCompilerArgs = freeCompilerArgs + listOf(
                        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    ) + buildComposeMetricsParameters()

                    setAndroidJvmTarget()
                }
            }

            setJvmToolchain()
        }
    }
}