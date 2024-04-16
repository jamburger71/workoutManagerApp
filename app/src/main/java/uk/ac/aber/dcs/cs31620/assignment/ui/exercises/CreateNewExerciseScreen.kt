package uk.ac.aber.dcs.cs31620.assignment.ui.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.assignment.R
import uk.ac.aber.dcs.cs31620.assignment.ui.components.StandardButton
import uk.ac.aber.dcs.cs31620.assignment.ui.database.ExerciseDataBaseManager
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.Blue40
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.Blue60
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.workoutContentStyleAlignedLeftLarge
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.workoutContentStyleAlignedRightLarge
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.workoutTitleStyle

@Composable
fun CreateNewExerciseScreen(
    navController: NavHostController,
    exerciseDataBaseManager: ExerciseDataBaseManager
    ) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        color = Blue40
    ) {
        Column {
            val exerciseName = remember{ mutableStateOf("") }
            val sets = remember{ mutableStateOf(0) }
            val reps = remember{ mutableStateOf(0) }
            val weight = remember{ mutableStateOf(0) }
            val exerciseIcon = remember { mutableStateOf(R.drawable.benchpress) }
            val currentColor = remember { mutableStateOf(Color.White) }
            Text(
                modifier = Modifier
                    .padding(all = 10.dp)
                    .fillMaxWidth(),
                text = (stringResource(id = R.string.createExercise_title_createNewExercise)),
                style = workoutTitleStyle,
                textAlign = TextAlign.Center
            )
            Divider(color = Color.White)
            Row {
                val maxChar = 18
                Text(
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    text = stringResource(id = R.string.createExercise_content_exerciseName),
                    style = workoutContentStyleAlignedRightLarge
                )
                BasicTextField(
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .weight(1f)
                        .background(color = Blue60)
                        .height(30.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    value = exerciseName.value,
                    singleLine = true,
                    maxLines = 1,
                    onValueChange = {
                        if (it.length <= maxChar) {
                            exerciseName.value = it
                        }
                    },
                    textStyle = workoutContentStyleAlignedLeftLarge,
                )
            }
            Row {
                val maxReps = 100
                Text(
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    text = stringResource(id = R.string.createExercise_content_exerciseReps),
                    style = workoutContentStyleAlignedRightLarge
                )
                BasicTextField(
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .weight(1f)
                        .background(color = Blue60)
                        .height(30.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    value = reps.value.toString(),
                    singleLine = true,
                    maxLines = 1,
                    onValueChange = {
                        try {
                            if (it.toInt() > maxReps) {
                                reps.value = maxReps
                            } else if (it.toInt() < 1) {
                                reps.value = 1
                            } else {
                                reps.value = it.toInt()
                            }
                        } catch (_: Exception){}
                    },
                    textStyle = workoutContentStyleAlignedLeftLarge,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            }
            Row {
                val maxSets = 100
                Text(
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    text = stringResource(id = R.string.createExercise_content_exerciseSets),
                    style = workoutContentStyleAlignedRightLarge
                )

                BasicTextField(
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .weight(1f)
                        .background(color = Blue60)
                        .height(30.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    value = sets.value.toString(),
                    singleLine = true,
                    maxLines = 1,
                    onValueChange = {
                        try {
                            if (it.toInt() > maxSets) {
                                sets.value = maxSets
                            } else if (it.toInt() < 1) {
                                sets.value = 1
                            } else {
                                sets.value = it.toInt()
                            }
                        } catch (_: Exception){}
                    },
                    textStyle = workoutContentStyleAlignedLeftLarge,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            }
            Row {
                val maxWeight = 999
                Text(
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    text = stringResource(id = R.string.createExercise_content_exerciseWeight),
                    style = workoutContentStyleAlignedRightLarge
                )
                BasicTextField(
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .weight(1f)
                        .background(color = Blue60)
                        .height(30.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    value = weight.value.toString(),
                    singleLine = true,
                    maxLines = 1,
                    onValueChange = {
                        try {
                            if (it.toInt() > maxWeight) {
                                weight.value = maxWeight
                            } else if (it.toInt() < 0) {
                                weight.value = 0
                            } else {
                                weight.value = it.toInt()
                            }
                        } catch (_: Exception){}
                    },
                    textStyle = workoutContentStyleAlignedLeftLarge,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            }
            Divider(color = Color.White)
            val isChooseIconVisible = remember{ mutableStateOf(false) }
            StandardButton(
                modifier = Modifier
                    .padding(all = 10.dp)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.addExercise_button_changeIcon),
                onClick = { isChooseIconVisible.value = true }
            ) {
                SelectIconDialog(
                    visible = isChooseIconVisible,
                    iconVariable = exerciseIcon,
                    currentColor = currentColor
                )
            }
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = exerciseIcon.value),
                contentDescription = stringResource(id = R.string.exercise_icon_default_description),
                tint = currentColor.value
            )
            Row(
                modifier = Modifier
                    .padding(bottom = 10.dp, top = 10.dp)
                    .height(40.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StandardButton(
                    modifier = Modifier,
                    text = stringResource(id = R.string.button_cancel),
                    onClick = {
                        navController.navigateUp()
                    }
                )
                StandardButton(
                    modifier = Modifier,
                    text = stringResource(id = R.string.addExercise_button_saveAndClose),
                    onClick = {
                        exerciseDataBaseManager.addExercise(
                            name = if (exerciseName.value != "") exerciseName.value else "Unnamed",
                            reps = kotlin.math.max(reps.value,1),
                            sets = kotlin.math.max(sets.value,1),
                            weight = kotlin.math.max(weight.value,0),
                            icon = exerciseIcon.value,
                            iconColor = currentColor.value.toArgb()
                        )
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}