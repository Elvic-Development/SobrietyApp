package com.orangeelephant.sobriety.ui.common

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.orangeelephant.sobriety.R

@Composable
fun LogoAndName() {
    Icon(
        painterResource(id = R.drawable.lotus_app_icon),
        contentDescription = stringResource(id = R.string.app_name)
    )
    Text(
        stringResource(id = R.string.app_name),
        style = MaterialTheme.typography.headlineLarge
    )
}

@Composable
fun Logo() {
    Icon(
        painterResource(id = R.drawable.lotus_app_icon),
        contentDescription = stringResource(id = R.string.app_name)
    )
}