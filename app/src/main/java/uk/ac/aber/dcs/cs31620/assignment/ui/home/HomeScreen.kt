@file:OptIn(ExperimentalMaterial3Api::class)

package uk.ac.aber.dcs.cs31620.assignment.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.assignment.R
import uk.ac.aber.dcs.cs31620.assignment.ui.components.RoundIconButton
import uk.ac.aber.dcs.cs31620.assignment.ui.database.ExerciseDataBaseManager
import uk.ac.aber.dcs.cs31620.assignment.ui.navigation.ExerciseViewModel
import uk.ac.aber.dcs.cs31620.assignment.ui.navigation.TopLevelScaffold


@Composable
fun HomeScreen(
    navController: NavHostController,
    exerciseDataBaseManager: ExerciseDataBaseManager,
    viewModel: ExerciseViewModel
) {
    TopLevelScaffold(navController) {
        val workouts = exerciseDataBaseManager.getAllWorkouts()
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(
                    top = 77.dp,
                    bottom = 75.dp
                )
                .verticalScroll(rememberScrollState()),
            color = Color.Transparent,

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 4.dp)
            ) {
                val extendedWorkout: MutableState<String> = rememberSaveable { mutableStateOf("") }

                workouts.forEach { workoutDay ->
                    workoutDay.listOfExercise = exerciseDataBaseManager.getPairingsForWorkout(workoutDay)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(color = Color(0xC020B2C6))
                    ) {
                        if (extendedWorkout.value == workoutDay.day) {
                            ExtendedWindowContent(
                                workoutDay = workoutDay,
                                exerciseDataBaseManager = exerciseDataBaseManager,
                                navController = navController,
                                viewModel = viewModel,
                                extendedWorkout = extendedWorkout
                            )
                        } else {
                            ShortWindowContent(
                                workoutDay = workoutDay,
                                exerciseDataBaseManager = exerciseDataBaseManager
                            )
                        }
                        RoundIconButton(
                            modifier = Modifier
                                .align(Alignment.TopEnd),
                            icon = if (extendedWorkout.value == workoutDay.day)
                                Icons.Filled.KeyboardArrowUp
                            else
                                Icons.Filled.KeyboardArrowDown,
                            iconDescription = stringResource(id = R.string.drop_down_description),
                            onClick = {
                                if (extendedWorkout.value != workoutDay.day) {
                                    extendedWorkout.value = workoutDay.day
                                } else {
                                    extendedWorkout.value = ""
                                }
                            },
                            enabled = true
                        )
                    }
                }
            }
        }
    }
}