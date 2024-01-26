package com.orangeelephant.sobriety.ui.common

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.orangeelephant.sobriety.R

@Composable
fun PasswordInputField(
    password: MutableState<String>,
    @StringRes label: Int = R.string.password,
    onDone: KeyboardActionScope.() -> Unit
) {
    OutlinedTextField(
        value = password.value,
        onValueChange = { password.value = it },
        label = { Text(stringResource(label)) },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = onDone
        )
    )
}

@Composable
fun PasswordConfirmationLayout(
    onConfirm: (password: String) -> Unit,
    @StringRes title: Int,
    @StringRes description: Int,
    @StringRes btnPosLabel: Int,
    @StringRes inputBoxLabel: Int = R.string.password,
    @StringRes inputBoxConfirmLabel: Int = R.string.confirm_password
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val password = remember { mutableStateOf("") }
    val passwordConfirm = remember { mutableStateOf("") }

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
        PasswordInputField(
            password = password,
            label = inputBoxLabel
        ) {
            focusManager.moveFocus(FocusDirection.Down)
        }
        Spacer(modifier = Modifier.height(5.dp))
        PasswordInputField(
            password = passwordConfirm,
            label = inputBoxConfirmLabel
        ) {
            if (password.value == passwordConfirm.value && password.value != "") {
                onConfirm(password.value)
            } else {
                Toast.makeText(context, R.string.passwords_dont_match, Toast.LENGTH_SHORT).show()
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        TextButton(
            onClick = { onConfirm(password.value) },
            enabled = password.value == passwordConfirm.value && password.value != "",
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(id = btnPosLabel))
        }
    }
}

@Composable
fun ClickableOutlinedTextField(
    value: TextFieldValue,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: @Composable () -> Unit
) {
    Box {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = modifier,
            label = label
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable(onClick = onClick),
        )
    }
}
