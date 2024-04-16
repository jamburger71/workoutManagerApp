package uk.ac.aber.dcs.cs31620.assignment.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import uk.ac.aber.dcs.cs31620.assignment.R

@Composable
fun AlertPopup(
    isVisible: MutableState<Boolean>,
    titleString: String,
    contentString: String,
    onDismissRequest: () -> Unit = { isVisible.value = false },
    onConfirmRequest: () -> Unit
) {
    if (isVisible.value) {
        AlertDialog(
            title = {
                Text(
                    text = titleString,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = contentString,
                    textAlign = TextAlign.Center
                )
            },
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmRequest()
                    }
                ) {
                    Text(text = stringResource(id = R.string.button_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text(text = stringResource(id = R.string.button_cancel))
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = stringResource(id = R.string.icon_content_description_warning)
                )
            }
        )
    }
}