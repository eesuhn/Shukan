package com.eesuhn.habittracker.feature.misc.export

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream


internal data class BackupContent(
    val habitsCSV: String,
    val actionsCSV: String,
    val metadata: Metadata
) {
    data class Metadata(val backupVersion: Int)
}

internal object ArchiveHandler {

    fun readBackup(input: InputStream): BackupContent {
        val habitsStream = ByteArrayOutputStream()
        val actionsStream = ByteArrayOutputStream()
        val metadataStream = ByteArrayOutputStream()

        ZipInputStream(input).use { inputStream ->
            var zipEntry = inputStream.nextEntry
            while (zipEntry != null) {
                if (zipEntry.isDirectory) {
                    zipEntry = inputStream.nextEntry
                    continue
                }
                when (zipEntry.name) {
                    "habits.csv" -> inputStream.copyTo(habitsStream)
                    "actions.csv" -> inputStream.copyTo(actionsStream)
                    "metadata.txt" -> inputStream.copyTo(metadataStream)
                }

                zipEntry = inputStream.nextEntry
            }
        }

        val metadataContent = metadataStream.toString()

        val backupVersion = metadataContent
            .split("\n")
            .firstOrNull { it.startsWith("backup_version") }
            ?.split("=")
            ?.lastOrNull()
            ?.toIntOrNull()
            ?: throw IllegalStateException("Couldn't find backup version in metadata.txt. File contents: $metadataContent")

        return BackupContent(
            habitsCSV = habitsStream.toString(),
            actionsCSV = actionsStream.toString(),
            metadata = BackupContent.Metadata(backupVersion)
        )
    }

    fun writeBackup(output: OutputStream, content: BackupContent) {
        ZipOutputStream(output).use { outputStream ->
            outputStream.putNextEntry(ZipEntry("habits.csv"))
            outputStream.write(content.habitsCSV.toByteArray())
            outputStream.closeEntry()

            outputStream.putNextEntry(ZipEntry("actions.csv"))
            outputStream.write(content.actionsCSV.toByteArray())
            outputStream.closeEntry()

            outputStream.putNextEntry(ZipEntry("metadata.txt"))
            outputStream.write("backup_version=${content.metadata.backupVersion}".toByteArray())
            outputStream.closeEntry()
        }
    }
}