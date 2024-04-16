package uk.ac.aber.dcs.cs31620.assignment.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.assignment.R
import uk.ac.aber.dcs.cs31620.assignment.ui.components.AlertPopup
import uk.ac.aber.dcs.cs31620.assignment.ui.components.RoundIconButton
import uk.ac.aber.dcs.cs31620.assignment.ui.components.StandardButton
import uk.ac.aber.dcs.cs31620.assignment.ui.database.ExerciseDataBaseManager
import uk.ac.aber.dcs.cs31620.assignment.ui.database.TAG
import uk.ac.aber.dcs.cs31620.assignment.ui.database.WorkoutDay
import uk.ac.aber.dcs.cs31620.assignment.ui.navigation.ExerciseViewModel
import uk.ac.aber.dcs.cs31620.assignment.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.Blue40
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.Blue60
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.Red40
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.workoutContentStyle
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.workoutContentStyleCentered
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.workoutTitleStyle

@Composable
fun ExtendedWindowContent(
    workoutDay: WorkoutDay,
    exerciseDataBaseManager: ExerciseDataBaseManager,
    navController: NavHostController,
    viewModel: ExerciseViewModel,
    extendedWorkout: MutableState<String>
) {
    val areYouSureVisible = remember{ mutableStateOf(false) }
    val workoutDayDay = remember { mutableStateOf(workoutDay.day) }
    Column(
        modifier = Modifier
            .padding(5.dp)
    ) {
        Row(
            modifier = Modifier
                .absolutePadding(top = 10.dp, right = 80.dp)
        ) {
            Text(
                modifier = Modifier
                    .height(40.dp)
                    .width(140.dp)
                    .fillMaxSize(),
                text = workoutDayDay.value,
                color = Color.White,
                style = workoutTitleStyle,
            )
            val text = remember{ mutableStateOf(workoutDay.workoutName) }
            BasicTextField(
                modifier = Modifier
                    .background(color = Blue40)
                    .height(30.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                value = text.value,
                textStyle = workoutContentStyleCentered,
                singleLine = true,
                maxLines = 1,
                onValueChange = {
                    text.value = it
                    workoutDay.workoutName = text.value
                    exerciseDataBaseManager.changeWorkoutName(
                        name = workoutDay.workoutName,
                        workoutDay = workoutDay
                    )
                }
            )
        }

        if (workoutDay.listOfExercise.isEmpty()) {
            Text(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxSize(),
                text = stringResource(id = R.string.workout_noExercisesSet),
                style = workoutContentStyle
            )
        } else {
            workoutDay.listOfExercise.sortBy { exerciseWorkoutPairing ->
                exerciseWorkoutPairing.positionInWorkout
            }
            workoutDay.listOfExercise.forEachIndexed { index, pairing ->
                val exercise = exerciseDataBaseManager.getExercise(pairing.exerciseID)
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .background(color = Blue60)
                        .fillMaxSize()
                ) {
                    Row {
                        Column {
                            Text(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .width(150.dp),
                                text = "${pairing.positionInWorkout}: ${exercise.name}",
                                style = workoutContentStyle
                            )
                            Text(
                                modifier = Modifier
                                    .padding(2.dp),
                                text = "${stringResource(id = R.string.exercise_reps_prefix)} ${exercise.reps}",
                                style = workoutContentStyle
                            )
                            Text(
                                modifier = Modifier
                                    .padding(2.dp),
                                text = "${stringResource(id = R.string.exercise_sets_prefix)} ${exercise.sets}",
                                style = workoutContentStyle
                            )
                            Text(
                                modifier = Modifier
                                    .padding(2.dp),
                                text = "${stringResource(id = R.string.exercise_weight_prefix)} ${exercise.weightInKilos}${
                                    stringResource(
                                        id = R.string.exercise_weight_suffix
                                    )
                                }",
                                style = workoutContentStyle
                            )
                        }
                        Icon(
                            modifier = Modifier
                                .width(80.dp)
                                .height(80.dp)
                                .align(Alignment.CenterVertically),
                            painter = painterResource(id = exercise.icon),
                            tint = Color(exercise.iconColor),
                            contentDescription = stringResource(id = R.string.exercise_icon_default_description)
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 10.dp)
                        ) {
                            RoundIconButton(
                                modifier = Modifier
                                    .height(50.dp)
                                    .width(50.dp),
                                icon = Icons.Filled.KeyboardArrowUp,
                                iconDescription = stringResource(id = R.string.exercise_icon_moveExerciseUpwards_description),
                                onClick = {
                                    Log.i(TAG,"")
                                    if (workoutDay.listOfExercise[index].positionInWorkout > 1)  {
                                        workoutDay.listOfExercise[index].positionInWorkout--
                                        workoutDay.listOfExercise[index-1].positionInWorkout++
                                        exerciseDataBaseManager.changePairingPosition(workoutDay.listOfExercise[index])
                                        exerciseDataBaseManager.changePairingPosition(workoutDay.listOfExercise[index-1])
                                        workoutDayDay.value = ""
                                        workoutDayDay.value = workoutDay.day
                                    }
                                },
                                enabled = (index+1 > 1)
                            )
                            RoundIconButton(
                                modifier = Modifier
                                    .height(50.dp)
                                    .width(50.dp),
                                icon = Icons.Filled.KeyboardArrowDown,
                                iconDescription = stringResource(id = R.string.exercise_icon_moveExerciseUpwards_description),
                                onClick = {
                                    if (workoutDay.listOfExercise[index].positionInWorkout < workoutDay.listOfExercise.size) {
                                        workoutDay.listOfExercise[index].positionInWorkout++
                                        workoutDay.listOfExercise[index+1].positionInWorkout--
                                        exerciseDataBaseManager.changePairingPosition(workoutDay.listOfExercise[index])
                                        exerciseDataBaseManager.changePairingPosition(workoutDay.listOfExercise[index+1])
                                        workoutDayDay.value = ""
                                        workoutDayDay.value = workoutDay.day
                                    }
                                },
                                enabled = (index+1 < workoutDay.listOfExercise.size)
                            )
                        }
                        RoundIconButton(
                            modifier = Modifier
                                .height(50.dp)
                                .width(50.dp)
                                .align(Alignment.CenterVertically)
                                .padding(start = 5.dp),
                            icon = Icons.Filled.Clear,
                            iconDescription = stringResource(id = R.string.exercise_icon_removeExercise_description),
                            onClick = {
                                workoutDay.listOfExercise.removeAt(index)
                                exerciseDataBaseManager.deletePairing(pairing)
                                var startIndex = index
                                while (startIndex != workoutDay.listOfExercise.size) {
                                    workoutDay.listOfExercise[startIndex].positionInWorkout--
                                    exerciseDataBaseManager.changePairingPosition(workoutDay.listOfExercise[startIndex])
                                    startIndex++
                                }
                                workoutDayDay.value = ""
                                workoutDayDay.value = workoutDay.day
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Red40)
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StandardButton(
                text = stringResource(id = R.string.workout_button_clear_text),
                onClick = { areYouSureVisible.value = true }
            ) {
                AlertPopup(
                    isVisible = areYouSureVisible,
                    titleString = stringResource(id = R.string.workout_confirm_clearExercises_title),
                    contentString = stringResource(id = R.string.confirm_cantBeUndone),
                    onDismissRequest = { areYouSureVisible.value = false },
                    onConfirmRequest = {
                        workoutDay.listOfExercise.clear()
                        exerciseDataBaseManager.clearExercisesForWorkout(workoutDay)
                        extendedWorkout.value = ""
                        extendedWorkout.value = workoutDay.day
                        areYouSureVisible.value = false
                    }
                )
            }
            StandardButton(
                text = stringResource(id = R.string.workout_button_addExercise_text),
                onClick = {
                    viewModel.setWorkoutState(workoutDay)
                    navController.navigate(Screen.AddOrCreateExercise.route)
                }
            )
        }
    }
}