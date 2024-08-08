import org.gradle.jvm.toolchain.JavaLanguageVersion

object Constants {
    const val MIN_SDK = 21
    const val COMPILE_SDK = 35
    const val TARGET_SDK = 35
    val javaToolchainVersion: JavaLanguageVersion = JavaLanguageVersion.of(17)

    const val VERSION_CODE = 6
    const val VERSION_NAME = "1.1.3"
}
