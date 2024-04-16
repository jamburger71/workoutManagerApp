package uk.ac.aber.dcs.cs31620.assignment.ui.exercises

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
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
import uk.ac.aber.dcs.cs31620.assignment.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.assignment.ui.navigation.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.workoutContentStyle

@Composable
fun ExercisesScreen(
    navController: NavHostController,
    exerciseDataBaseManager: ExerciseDataBaseManager
) {

    TopLevelScaffold(navController) {
        val listOfExercises = exerciseDataBaseManager.getAllExercises()
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(
                    top = 77.dp,
                    bottom = 75.dp
                ),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(state = ScrollState(0))
                        .weight(1f)
                ) {
                    val composableListOfExercises = remember { mutableStateListOf(listOfExercises) }
                    if (composableListOfExercises[0].isNotEmpty()) {
                        composableListOfExercises[0].forEach { exercise ->
                            val composableExercise = remember { mutableStateOf(exercise) }
                            Box(
                                modifier = Modifier
                                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                                    .background(color = Color(0xC020B2C6))
                                    .padding(
                                        top = 10.dp,
                                        start = 10.dp,
                                        end = 10.dp,
                                        bottom = 10.dp
                                    )
                                    .fillMaxWidth()
                            ) {
                                Row {
                                    Column {
                                        Text(
                                            modifier = Modifier
                                                .padding(2.dp)
                                                .width(150.dp),
                                            text = composableExercise.value.name,
                                            style = workoutContentStyle
                                        )
                                        Text(
                                            modifier = Modifier
                                                .padding(2.dp),
                                            text = "${stringResource(id = R.string.exercise_reps_prefix)} ${composableExercise.value.reps}",
                                            style = workoutContentStyle
                                        )
                                        Text(
                                            modifier = Modifier
                                                .padding(2.dp),
                                            text = "${stringResource(id = R.string.exercise_sets_prefix)} ${composableExercise.value.sets}",
                                            style = workoutContentStyle
                                        )
                                        Text(
                                            modifier = Modifier
                                                .padding(2.dp),
                                            text = "${stringResource(id = R.string.exercise_weight_prefix)} ${composableExercise.value.weightInKilos}${
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
                                        painter = painterResource(id = composableExercise.value.icon),
                                        tint = Color(composableExercise.value.iconColor),
                                        contentDescription = stringResource(id = R.string.exercise_icon_default_description)
                                    )
                                    val isModifyVisible = remember { mutableStateOf(false) }
                                    Row(
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                    ) {
                                        RoundIconButton(
                                            modifier = Modifier
                                                .aspectRatio(1f)
                                                .weight(1f),
                                            icon = Icons.Filled.Settings,
                                            iconDescription = stringResource(id = R.string.exercise_icon_editExercise_description),
                                            onClick = {
                                                isModifyVisible.value = true
                                            }
                                        ) {
                                            ModifyExerciseDialog(
                                                visible = isModifyVisible,
                                                exercise = composableExercise,
                                                exerciseDataBaseManager = exerciseDataBaseManager
                                            )
                                        }
                                        val deleteExercisePopupVisible = remember { mutableStateOf(false) }
                                        RoundIconButton(
                                            modifier = Modifier
                                                .aspectRatio(1f)
                                                .weight(1f),
                                            icon = Icons.Filled.Clear,
                                            iconDescription = stringResource(id = R.string.exercise_icon_deleteExercise_description),
                                            onClick = { deleteExercisePopupVisible.value = true }
                                        ) {
                                            AlertPopup(
                                                isVisible = deleteExercisePopupVisible,
                                                titleString = stringResource(id = R.string.exercise_dialog_areYouSureDelete_title),
                                                contentString = stringResource(id = R.string.confirm_cantBeUndone),
                                                onConfirmRequest = {
                                                    exerciseDataBaseManager.deleteExercise(exercise)
                                                    exerciseDataBaseManager.deletePairingsForExercise(exercise)
                                                    composableListOfExercises[0] = exerciseDataBaseManager.getAllExercises()
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    StandardButton(
                        modifier = Modifier
                            .padding(all = 10.dp),
                        text = stringResource(id = R.string.addExercise_button_createNewExercise),
                        onClick = { navController.navigate(Screen.CreateExercise.route) }
                    )
                }
            }
        }
    }
}