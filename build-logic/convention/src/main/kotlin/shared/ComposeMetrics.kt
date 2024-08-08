package shared

import org.gradle.api.Project

fun Project.buildComposeMetricsParameters(): List<String> {
    val metricParameters = mutableListOf<String>()
    val enableMetricsProvider =
        project.providers.gradleProperty("habittracker.enableComposeCompilerReports")
    val enableMetrics = (enableMetricsProvider.orNull == "true")
    if (enableMetrics) {
        val metricsFolder = project.layout.buildDirectory.dir("compose-metrics")
        metricParameters.add("-P")
        metricParameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + metricsFolder.get().asFile.absolutePath
        )

        val reportsFolder = project.layout.buildDirectory.dir("compose-reports")
        metricParameters.add("-P")
        metricParameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" + reportsFolder.get().asFile.absolutePath
        )
    }

    return metricParameters.toList()
}