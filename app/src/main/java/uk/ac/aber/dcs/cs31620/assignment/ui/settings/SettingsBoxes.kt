package uk.ac.aber.dcs.cs31620.assignment.ui.settings

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.assignment.ui.database.SettingsDataStoreHelper
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.Blue60
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.workoutContentStyle

@Composable
fun SettingsBoxToggle(
    stringResource: Int,
    scope: CoroutineScope,
    context: Context,
    settingsManager: SettingsDataStoreHelper,
    key: String
) {
    val value = settingsManager.getDataBoolean(
        context = context,
        stringKey = key
    ).collectAsState(initial = false).value
    val checked = remember { mutableStateOf(false) }
    checked.value = value
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .align(CenterVertically),
            text = stringResource(id = stringResource),
            textAlign = TextAlign.Center,
            style = workoutContentStyle
        )
        Switch(
            modifier = Modifier
                .weight(1f),
            checked = checked.value,
            onCheckedChange = {
                checked.value = !checked.value
                scope.launch {
                    settingsManager.saveDataBoolean(
                        context = context,
                        stringKey = key,
                        newValue = checked.value
                    )
                }
            }
        )
    }
}

@Composable
fun SettingsBoxClick(
    buttonText: String,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(all = 5.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Blue60),
        onClick = { onClick() }
    ) {
        Text(
            text = buttonText,
            style = workoutContentStyle,
            modifier = Modifier
                .background(color = Color.Transparent)
        )
    }
}

