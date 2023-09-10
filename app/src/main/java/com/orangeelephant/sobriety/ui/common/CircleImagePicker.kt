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
import androidx.compose.material3.Button
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
import coil.compose.AsyncImage
import com.orangeelephant.sobriety.R

@Composable
fun CircleImagePicker(
    selectedImageUri: MutableState<Uri?>,
    onImageSelected: (Uri?) -> Unit,
) {
    val isImageSelected = selectedImageUri.value != null
    var previousUri: Uri? = null

    val singlePhotoDialog = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            previousUri = uri
            onImageSelected(uri)
        } else {
            // No URI is selected, revert to the previous one if available
            previousUri?.let { onImageSelected(it) }
        }
    }

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

            if (selectedImageUri.value == null){
                Button(
                    onClick = { singlePhotoDialog.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_photo),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp))
                }
            } else{
                AsyncImage(
                    model = selectedImageUri.value,
                    contentDescription = stringResource(R.string.image_description),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

        }

        Row {
            if (isImageSelected) {

                // change
                TextButton(onClick = {
                    singlePhotoDialog.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = stringResource(R.string.change_image),
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 4.dp)
                    )

                    Text(stringResource(R.string.change_image))
                }

                // remove
                TextButton(onClick = { selectedImageUri.value = null })
                {
                    Icon(
                        painter = painterResource(R.drawable.ic_bin),
                        contentDescription = stringResource(R.string.remove_image),
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 4.dp)
                    )
                    Text(stringResource(R.string.remove_image))
                }

            } else {
                // add image
                TextButton(onClick = {
                    singlePhotoDialog.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }) {
                    Text(stringResource(R.string.add_image))
                }
            }
        }
    }
}


