package com.eesuhn.habittracker.feature.misc.export

import androidx.compose.runtime.Immutable
import java.net.URI
import java.time.Instant

@Immutable
data class DataSummary(
    val habitCount: Int,
    val actionCount: Int,
    val lastActivity: Instant? // null if there are no actions
)

enum class ExportImportError {
    FilePickerURIEmpty,
    ExportFailed,
    ImportFailed,
    BackupVersionTooHigh
}

@Immutable
data class ExportState(
    val outputFileURI: URI?,
    val error: ExportImportError?
)

@Immutable
data class ImportState(
    val backupFileURI: URI?,
    val backupSummary: DataSummary?,
    val error: ExportImportError?
)