package com.orangeelephant.sobriety.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.orangeelephant.sobriety.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingDialog(
    @StringRes label: Int = R.string.loading
) {
    AlertDialog(onDismissRequest = { /* Don't dismiss */ }) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(stringResource(id = label), modifier = Modifier.align(CenterHorizontally))
                Spacer(modifier = Modifier.height(10.dp))
                CircularProgressIndicator(modifier = Modifier.align(CenterHorizontally))
            }
        }
    }
}

@Composable
fun WarningDialog(
    onDismiss: () -> Unit,
    @StringRes title: Int,
    @StringRes description: Int,
    @StringRes confirmText: Int = R.string.understood
) {
    GenericAlertDialog(
        onConfirm = onDismiss,
        onDismiss = onDismiss,
        title = title,
        description = description,
        confirmText = confirmText,
        hasCancelButton = false
    )
}

@Composable
fun ConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    @StringRes title: Int,
    @StringRes description: Int
) {
    GenericAlertDialog(
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        title = title,
        description = description,
        hasCancelButton = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenericAlertDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    @StringRes title: Int,
    @StringRes description: Int,
    @StringRes confirmText: Int = R.string.okay,
    hasCancelButton: Boolean
) {
    AlertDialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    stringResource(id = title),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    stringResource(id = description),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(5.dp))

                Row(modifier = Modifier.align(Alignment.End)) {
                    if (hasCancelButton) {
                        TextButton(
                            onClick = { onDismiss() },
                        ) {
                            Text(stringResource(id = R.string.cancel_button))
                        }
                    }
                    TextButton(
                        onClick = { onConfirm() },
                    ) {
                        Text(stringResource(id = confirmText))
                    }
                }
            }
        }
    }
}