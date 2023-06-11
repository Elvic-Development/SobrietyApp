package com.orangeelephant.sobriety.ui.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun SettingsRow(
    @StringRes title: Int,
    @StringRes description: Int,
    onClick: () -> Unit = {},
    imageVector: ImageVector = Icons.Filled.Check
) {
    SettingsRow(
        title = stringResource(id = title),
        description = stringResource(id = description),
        onClick = onClick,
        imageVector = imageVector
    )
}

@Composable
fun SettingsRow(
    title: String,
    description: String,
    onClick: () -> Unit = {},
    imageVector: ImageVector = Icons.Filled.Check
) {
    Row (
        modifier = Modifier
            .padding(
                vertical = 8.dp,
            )
            .clickable { onClick() }
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription =  title,
            modifier = Modifier.padding(all = 14.dp)
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}