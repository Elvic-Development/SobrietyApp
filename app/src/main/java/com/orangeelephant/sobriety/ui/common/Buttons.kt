package com.orangeelephant.sobriety.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.orangeelephant.sobriety.R

@Composable
fun Fab(
    onClick: () -> Unit,
    icon: ImageVector,
    @StringRes contentDescription: Int
) {
    FloatingActionButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 25)),
    ) {
        Icon(icon, contentDescription = stringResource(id = contentDescription))
    }
}

@Preview
@Composable
fun FabPreview() {
    Fab(onClick = { }, icon = Icons.Filled.ArrowBack, contentDescription = R.string.create_button)
}