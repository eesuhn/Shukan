package com.eesuhn.habittracker.feature.misc.settings

import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import org.json.JSONArray
import javax.inject.Inject

private const val ASSET_PATH_LICENSES = "licenses.json"

data class Dependency(
    val group: String,
    val artifact: String,
    val license: License?,
    val url: String?
) {
    data class License(val name: String)
}

@HiltViewModel
class LicensesViewModel @Inject constructor(
    private val app: Application
) : ViewModel() {

    val dependencies = MutableStateFlow<ImmutableList<Dependency>>(persistentListOf())

    init {
        dependencies.value = parseDependencies().toImmutableList()
    }

    private fun parseDependencies(): List<Dependency> {
        val dependencies = mutableListOf<Dependency>()

        val stringContent = app.assets.open(ASSET_PATH_LICENSES)
            .bufferedReader()
            .use { it.readText() }

        // Ain't no need for a 3rd party JSON lib for a license screen!
        val dependencyArray = JSONArray(stringContent)
        for (i in 0 until dependencyArray.length()) {
            val dependencyObject = dependencyArray.getJSONObject(i)
            val groupId = dependencyObject.getString("groupId")
            val artifactId = dependencyObject.getString("artifactId")

            val license = if (dependencyObject.has("spdxLicenses")) {
                val licenses = dependencyObject.getJSONArray("spdxLicenses")
                if (licenses.length() > 0) {
                    val license = licenses.getJSONObject(0)
                    val licenseName = license.getString("name")
                    Dependency.License(licenseName)
                } else {
                    null
                }
            } else {
                null
            }

            val url = if (dependencyObject.has("scm")) {
                val scmObject = dependencyObject.getJSONObject("scm")
                if (scmObject.has("url")) {
                    scmObject.getString("url")
                } else {
                    null
                }
            } else {
                null
            }

            dependencies.add(Dependency(groupId, artifactId, license, url))
        }

        return dependencies
    }

}