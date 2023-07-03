package com.orangeelephant.sobriety.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun TextDivider(@StringRes text: Int) {
    Row(Modifier.padding(vertical = 40.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier
            .height(1.dp)
            .weight(1f)
            .background(MaterialTheme.colorScheme.outline))
        Text(
            text = stringResource(id = text),
            style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.outline),
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Box(modifier = Modifier
            .height(1.dp)
            .weight(1f)
            .background(MaterialTheme.colorScheme.outline))
    }
}