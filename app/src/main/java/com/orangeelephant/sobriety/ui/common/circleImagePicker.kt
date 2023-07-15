package com.orangeelephant.sobriety.ui.common

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.stringPreferencesKey
import coil.compose.AsyncImage
import com.orangeelephant.sobriety.R

@Composable
fun SinglePhotoPicker(
    selectedImageUri: MutableState<Uri?>,
    onImageSelected: (Uri?) -> Unit,
) {
    val singlePhotoDialog = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> onImageSelected(uri) }
    )

    val isImageSelected = selectedImageUri.value != null

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .clickable {
                    singlePhotoDialog.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = selectedImageUri.value,
                contentDescription = stringResource(R.string.image_description),
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Row {
            if (isImageSelected) {
                TextButton(onClick = { singlePhotoDialog.launch( PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = stringResource(R.string.change_image),
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 4.dp))

                    Text(stringResource(R.string.change_image))
                }
                TextButton(onClick = { selectedImageUri.value = null }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_bin),
                        contentDescription = stringResource(R.string.remove_image),
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 4.dp))

                    Text(stringResource(R.string.remove_image))
                }
            } else {
                TextButton(onClick = { singlePhotoDialog.launch( PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
                    Text(stringResource(R.string.add_image))
                }
            }
        }
    }
}
