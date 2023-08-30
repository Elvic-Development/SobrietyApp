package com.orangeelephant.sobriety.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BackIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.Filled.ArrowBack, contentDescription = "Home")
    }
}

@Composable
fun SettingsLink(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.Filled.Settings, contentDescription = "Settings")
    }
}

@Preview
@Composable
fun BackIconPreview() {
    BackIcon(onClick = {})
}

@Preview
@Composable
fun SettingsIconPreview() {
    SettingsLink(onClick = {})
}