package com.eesuhn.habittracker.feature.misc.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.eesuhn.habittracker.core.ui.component.AppDefaultAppBar
import com.eesuhn.habittracker.core.ui.theme.PreviewTheme
import com.eesuhn.habittracker.core.ui.theme.isDynamicThemeAvailable
import com.eesuhn.habittracker.feature.misc.R
import com.eesuhn.habittracker.core.ui.R as coreR

@SuppressLint("ComposableLambdaParameterNaming")
@Composable
fun SettingsScreen(
    appInfo: AppInfo,
    crashReportingEnabled: Boolean,
    dynamicColorEnabled: Boolean,
    onBack: () -> Unit,
    onRateClick: () -> Unit,
    onSourceClick: () -> Unit,
    onLicensesClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onCrashReportingChange: (Boolean) -> Unit,
    onDynamicColorChange: (Boolean) -> Unit,
    debugSettings: @Composable () -> Unit
) {
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        AppDefaultAppBar(
            title = { Text(stringResource(R.string.settings_title)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Rounded.ArrowBack, stringResource(coreR.string.common_back))
                }
            }
        )
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            if (isDynamicThemeAvailable()) {
                SwitchSetting(
                    name = stringResource(R.string.settings_item_dynamic_color),
                    checked = dynamicColorEnabled,
                    onCheckedChange = onDynamicColorChange
                )
            }
            SwitchSetting(
                name = stringResource(R.string.settings_item_crash_reporting),
                checked = crashReportingEnabled,
                onCheckedChange = onCrashReportingChange
            )

            SettingHeader(name = stringResource(R.string.settings_header_about))
            TextRow(
                name = stringResource(R.string.app_name),
                subtitle = "${appInfo.versionName} (${appInfo.buildType})"
            )
            NavigationSetting(
                name = stringResource(R.string.settings_item_rate),
                onClick = onRateClick
            )
            NavigationSetting(
                name = stringResource(R.string.settings_item_view_source),
                onClick = onSourceClick
            )
            NavigationSetting(
                name = stringResource(R.string.settings_item_licenses),
                onClick = onLicensesClick
            )
            NavigationSetting(
                name = stringResource(R.string.settings_item_privacy_policy),
                onClick = onPrivacyClick
            )

            debugSettings() // no-op in release variant
        }
    }
}

@Composable
fun SettingHeader(name: String) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outlineVariant)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 72.dp, end = 16.dp, bottom = 2.dp, top = 16.dp),
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.tertiary
        )
    }

}

@Composable
private fun SwitchSetting(
    name: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, start = 56.dp)
        )
        Spacer(Modifier.weight(1f))
        Switch(checked, onCheckedChange)
    }
}

@Composable
fun NavigationSetting(
    name: String,
    onClick: () -> Unit
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(start = 72.dp, end = 16.dp, top = 20.dp, bottom = 20.dp),
        text = name,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
internal fun TextRow(name: String, subtitle: String) {
    Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 56.dp),
            text = name,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 56.dp)
        )
    }
}

private fun Context.openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply { data = url.toUri() }
    try {
        startActivity(intent)
    } catch (e: Throwable) {
        // Fail silently, it's unlikely that real user devices don't have a browser
    }
}

@Preview
@ShowkaseComposable(name = "Screen", group = "Settings")
@Composable
fun PreviewSettingsScreen() {
    PreviewTheme {
        SettingsScreen(
            appInfo = AppInfo(
                versionName = "1.0.0",
                buildType = "debug",
                appId = "com.eesuhn.habittracker",
                urlPrivacyPolicy = "",
                urlSourceCode = ""
            ),
            crashReportingEnabled = true,
            dynamicColorEnabled = true,
            onBack = {},
            onSourceClick = {},
            onLicensesClick = {},
            onPrivacyClick = {},
            onRateClick = {},
            onCrashReportingChange = {},
            onDynamicColorChange = {},
            debugSettings = {}
        )
    }
}