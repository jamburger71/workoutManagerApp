package uk.ac.aber.dcs.cs31620.assignment.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.assignment.R
import uk.ac.aber.dcs.cs31620.assignment.ui.components.RoundIconButton
import uk.ac.aber.dcs.cs31620.assignment.ui.components.StandardButton
import uk.ac.aber.dcs.cs31620.assignment.ui.database.ExerciseDataBaseManager
import uk.ac.aber.dcs.cs31620.assignment.ui.navigation.ExerciseViewModel
import uk.ac.aber.dcs.cs31620.assignment.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.Blue40
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.workoutContentStyle
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.workoutTitleStyle

@Composable
fun AddOrCreateExerciseScreen(
    navController: NavHostController,
    exerciseDataBaseManager: ExerciseDataBaseManager,
    viewModel: ExerciseViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val workoutDay = uiState.currentWorkout

    if (workoutDay != null) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            color = Blue40
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = (stringResource(id = R.string.addExercise_title_addTo) + " " + workoutDay.day),
                    style = workoutTitleStyle,
                    textAlign = TextAlign.Center
                )
                Divider()
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(10.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (exerciseDataBaseManager.getAllExercises().isNotEmpty()) {
                        exerciseDataBaseManager.getAllExercises().forEach { exercise ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .background(color = Color(0xC020B2C6))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .height(40.dp),
                                        text = exercise.name,
                                        style = workoutTitleStyle
                                    )
                                    Row(
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .height(60.dp),
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                        ) {
                                            Text(
                                                text = (stringResource(id = R.string.exercise_reps_prefix) + exercise.reps),
                                                style = workoutContentStyle
                                            )
                                            Text(
                                                text = (stringResource(id = R.string.exercise_sets_prefix) + exercise.sets),
                                                style = workoutContentStyle
                                            )
                                        }
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                        ) {
                                            Text(
                                                text = (stringResource(
                                                    id = R.string.exercise_weight_prefix
                                                ) + exercise.weightInKilos + stringResource(
                                                    id = R.string.exercise_weight_suffix
                                                )),
                                                style = workoutContentStyle
                                            )
                                        }
                                        Icon(
                                            painter = painterResource(id = exercise.icon),
                                            contentDescription = stringResource(id = R.string.exercise_icon_default_description),
                                            tint = Color(exercise.iconColor)
                                        )
                                        RoundIconButton(
                                            icon = Icons.Default.Add,
                                            iconDescription = stringResource(id = R.string.exercise_icon_addExercise_description),
                                            onClick = {
                                                exerciseDataBaseManager.createNewPairing(
                                                    exercise,
                                                    workoutDay,
                                                    workoutDay.listOfExercise.size
                                                )
                                                workoutDay.listOfExercise = (exerciseDataBaseManager.getPairingsForWorkout(workoutDay))
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Blue40)
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            text = stringResource(id = R.string.addExercise_content_noAvailableExercises),
                            style = workoutContentStyle,
                            textAlign = TextAlign.Center
                        )
                    }

                }
                Divider()
                Row(
                    modifier = Modifier
                        .padding(bottom = 10.dp, top = 10.dp)
                        .height(40.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StandardButton(
                        modifier = Modifier,
                        text = stringResource(id = R.string.addExercise_button_createNewExercise),
                        onClick = { navController.navigate(Screen.CreateExercise.route) }
                    )
                    StandardButton(
                        modifier = Modifier,
                        text = stringResource(id = R.string.addExercise_button_saveAndClose),
                        onClick = { navController.navigateUp() }
                    )
                }
            }
        }
    } else {
        Log.e(object{}.javaClass.enclosingMethod?.name, "No Workout Detected To Retrieve Exercises For")
        navController.navigateUp()
    }
}