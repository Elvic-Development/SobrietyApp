package com.orangeelephant.sobriety.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.orangeelephant.sobriety.R
import kotlin.math.min

@Composable
fun <T> ExpandableList (
    @StringRes title: Int,
    items: SnapshotStateList<T>,
    numItemsUnexpanded: Int = 5,
    itemHolder: @Composable (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Text(
        stringResource(id = title),
        style = MaterialTheme.typography.headlineMedium,
    )

    if (expanded) {
        for (item in items) {
            itemHolder(item)
        }
    } else {
        for (i in 0 until min(items.size, numItemsUnexpanded)) {
            itemHolder(items[i])
        }

        if (items.size > numItemsUnexpanded) {
            Text(
                stringResource(id = R.string.show_more),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.clickable {
                    expanded = !expanded
                }
            )
        }
    }
}