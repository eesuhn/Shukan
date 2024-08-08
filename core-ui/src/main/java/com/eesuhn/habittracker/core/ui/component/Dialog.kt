package com.eesuhn.habittracker.core.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.eesuhn.habittracker.core.ui.R

@Composable
fun ConfirmationDialog(
    showDialog: Boolean,
    title: String,
    description: String,
    confirmText: String,
    dismissText: String = stringResource(R.string.common_cancel),
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                FilledTonalButton(onClick = onConfirm) {
                    Text(text = confirmText)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = dismissText)
                }
            },
            title = { Text(text = title) },
            text = { Text(text = description) }
        )
    }
}