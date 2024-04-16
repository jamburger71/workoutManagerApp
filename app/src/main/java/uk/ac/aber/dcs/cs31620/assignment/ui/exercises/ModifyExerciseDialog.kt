package uk.ac.aber.dcs.cs31620.assignment.ui.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import uk.ac.aber.dcs.cs31620.assignment.R
import uk.ac.aber.dcs.cs31620.assignment.ui.components.StandardButton
import uk.ac.aber.dcs.cs31620.assignment.ui.database.Exercise
import uk.ac.aber.dcs.cs31620.assignment.ui.database.ExerciseDataBaseManager
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.Blue40
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.Blue60
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.workoutContentStyleAlignedLeftLarge
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.workoutContentStyleAlignedRightLarge
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.workoutTitleStyle

@Composable
fun ModifyExerciseDialog(
    visible: MutableState<Boolean>,
    exercise: MutableState<Exercise>,
    exerciseDataBaseManager: ExerciseDataBaseManager
) {
    if (visible.value) {
        val originalExercise = exercise.value
        val exerciseName = remember{ mutableStateOf(exercise.value.name) }
        val sets = remember{ mutableStateOf(exercise.value.sets) }
        val reps = remember{ mutableStateOf(exercise.value.reps) }
        val weight = remember{ mutableStateOf(exercise.value.weightInKilos) }
        val exerciseIcon = remember { mutableStateOf(exercise.value.icon) }
        val currentColor = remember { mutableStateOf(Color(exercise.value.iconColor)) }
        Dialog(
            onDismissRequest = {
                exercise.value = originalExercise
                visible.value = false
            }
        ) {
            Box(
                modifier = Modifier
                    .background(color = Blue40)
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.editExercise_title),
                        style = workoutTitleStyle
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
                        val maxSets = 100
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
                        val maxReps = 100
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
                    val isChooseIconVisible = remember { mutableStateOf(false) }
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
                                visible.value = false
                            }
                        )
                        StandardButton(
                            modifier = Modifier,
                            text = stringResource(id = R.string.addExercise_button_saveAndClose),
                            onClick = {
                                exercise.value = Exercise(
                                    id = exercise.value.id,
                                    name = exerciseName.value,
                                    reps = reps.value,
                                    sets = sets.value,
                                    weightInKilos = weight.value,
                                    icon = exerciseIcon.value,
                                    iconColor = currentColor.value.toArgb()
                                )
                                exerciseDataBaseManager.modifyExercise(exercise.value)
                                visible.value = false
                            }
                        )
                    }
                }
            }
        }
    }
}