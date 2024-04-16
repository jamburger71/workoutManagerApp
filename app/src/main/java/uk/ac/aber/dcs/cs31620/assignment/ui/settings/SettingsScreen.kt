package uk.ac.aber.dcs.cs31620.assignment.ui.settings

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.assignment.R
import uk.ac.aber.dcs.cs31620.assignment.ui.components.AlertPopup
import uk.ac.aber.dcs.cs31620.assignment.ui.database.ExerciseDataBaseManager
import uk.ac.aber.dcs.cs31620.assignment.ui.database.SettingsDataStoreHelper
import uk.ac.aber.dcs.cs31620.assignment.ui.navigation.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.Blue40

const val ALLOW_NOTIFY_KEY = "AllowNotifications"

@Composable
fun SettingsScreen(navController: NavHostController) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val deleteAllIsVisible = rememberSaveable { mutableStateOf(false) }
    val deleteExercisesIsVisible = rememberSaveable { mutableStateOf(false) }
    val deleteWorkoutsIsVisible = rememberSaveable { mutableStateOf(false) }

    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val databaseManager = ExerciseDataBaseManager(context = context)
    val settingsManager = SettingsDataStoreHelper()
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri.value = uri
    }

    imageUri.value?.let {
        LaunchedEffect(Unit) {
            val source = ImageDecoder
                .createSource(context.contentResolver, it)
            bitmap.value = ImageDecoder.decodeBitmap(source)
        }
    }
    bitmap.value?.let { bitmapImage ->
        Image(
            bitmap = bitmapImage.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.size(400.dp)
        )
    }

    TopLevelScaffold(navController) {
        Surface(
            color = Blue40,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 77.dp,
                    bottom = 75.dp
                )
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .background(color = Blue40),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SettingsBoxClick(
                    buttonText = stringResource(id = R.string.settings_button_changeBackground),
                    onClick = { launcher.launch("image/*") }
                )
                Divider(color = Color.White)

                SettingsBoxToggle(
                    stringResource = R.string.settings_text_toggleNotifications,
                    context = context,
                    scope = scope,
                    settingsManager = settingsManager,
                    key = ALLOW_NOTIFY_KEY
                )

                Divider(color = Color.White)
                SettingsBoxClick(
                    buttonText = stringResource(id = R.string.settings_button_clearAll),
                    onClick = { deleteAllIsVisible.value = true }
                )
                SettingsBoxClick(
                    buttonText = stringResource(id = R.string.settings_button_clearWorkouts),
                    onClick = { deleteWorkoutsIsVisible.value = true }
                )
                SettingsBoxClick(
                    buttonText = stringResource(id = R.string.settings_button_clearExercises),
                    onClick = { deleteExercisesIsVisible.value = true }
                )
            }
        }

        AlertPopup(
            isVisible = deleteAllIsVisible,
            onDismissRequest = { deleteAllIsVisible.value = false },
            onConfirmRequest = {
                databaseManager.clearAllData()
                deleteAllIsVisible.value = false
                               },
            titleString = stringResource(id = R.string.settings_confirm_clearAll_title),
            contentString = stringResource(id = R.string.settings_confirm_clearAll_content)
        )

        AlertPopup(
            isVisible = deleteExercisesIsVisible,
            onDismissRequest = { deleteExercisesIsVisible.value = false },
            onConfirmRequest = {
                databaseManager.clearAllExercises()
                deleteExercisesIsVisible.value = false
                               },
            titleString = stringResource(id = R.string.settings_confirm_clearExercises_title),
            contentString = stringResource(id = R.string.settings_confirm_clearExercises_content)
        )

        AlertPopup(
            isVisible = deleteWorkoutsIsVisible,
            onDismissRequest = { deleteWorkoutsIsVisible.value = false },
            onConfirmRequest = {
                databaseManager.clearAllWorkouts()
                deleteWorkoutsIsVisible.value = false
                               },
            titleString = stringResource(id = R.string.settings_confirm_clearWorkouts_title),
            contentString = stringResource(id = R.string.confirm_cantBeUndone)
        )
    }
}

