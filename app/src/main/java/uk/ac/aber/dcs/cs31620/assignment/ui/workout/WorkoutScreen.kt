package uk.ac.aber.dcs.cs31620.assignment.ui.workout

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.assignment.R
import uk.ac.aber.dcs.cs31620.assignment.ui.components.AlertPopup
import uk.ac.aber.dcs.cs31620.assignment.ui.components.StandardButton
import uk.ac.aber.dcs.cs31620.assignment.ui.components.toExercises
import uk.ac.aber.dcs.cs31620.assignment.ui.components.toOrdinal
import uk.ac.aber.dcs.cs31620.assignment.ui.components.toSets
import uk.ac.aber.dcs.cs31620.assignment.ui.components.toUpperFirst
import uk.ac.aber.dcs.cs31620.assignment.ui.database.Exercise
import uk.ac.aber.dcs.cs31620.assignment.ui.database.ExerciseDataBaseManager
import uk.ac.aber.dcs.cs31620.assignment.ui.database.ExerciseWorkoutPairing
import uk.ac.aber.dcs.cs31620.assignment.ui.database.SettingsDataStoreHelper
import uk.ac.aber.dcs.cs31620.assignment.ui.database.TAG
import uk.ac.aber.dcs.cs31620.assignment.ui.database.WorkoutDay
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.Green75
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.Purple50
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.Red40
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.Yellow50
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.runWorkoutTextStyleBig
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.runWorkoutTitleStyle
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.titleTextStyle
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.workoutTitleStyleCentered
import java.time.LocalDate
import kotlin.random.Random

const val LASTDAYCOMPLETEDKEY = "lastDayCompleted"
const val ISTODAYCOMPLETEDKEY = "isTodayCompleted"


@Composable
fun WorkoutScreen(
    navController: NavHostController,
    exerciseDataBaseManager: ExerciseDataBaseManager
) {
    val settingsManager = SettingsDataStoreHelper()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val currentDay = LocalDate.now().dayOfWeek.name.toUpperFirst()
    val today = exerciseDataBaseManager.getWorkout(currentDay)
    val todaysWorkout = exerciseDataBaseManager.getPairingsForWorkout(today)
    val workoutCount = remember { mutableStateOf(0) }
    val workoutState = remember { mutableStateOf(WorkoutState.StartOfWorkout) }
    val currentExercise: MutableState<Exercise?> = remember { mutableStateOf(null) }
    val currentSet = remember { mutableStateOf(0) }

    val lastDayCompleted = settingsManager.getDataString(
        context = context,
        stringKey = LASTDAYCOMPLETEDKEY
    ).collectAsState(initial = "").value
    val isWorkoutCompleted = settingsManager.getDataBoolean(
        context = context,
        stringKey = ISTODAYCOMPLETEDKEY
    ).collectAsState(initial = false).value
    if (lastDayCompleted != today.day && lastDayCompleted != "") {
        LaunchedEffect(scope) {
            scope.launch {
                settingsManager.saveDataBoolean(
                    context = context,
                    stringKey = ISTODAYCOMPLETEDKEY,
                    newValue = false
                )
            }
        }
    }
    if (isWorkoutCompleted) {
        workoutState.value = WorkoutState.AlreadyCompleted
    } else if (todaysWorkout.isEmpty()) {
        workoutState.value = WorkoutState.NoWorkoutSet
    }
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = when (workoutState.value) {
            WorkoutState.DuringExercise -> Yellow50
            WorkoutState.RestingBetweenSets, WorkoutState.RestingBetweenExercises -> Purple50
            else -> Green75
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier
                    .height(77.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                style = titleTextStyle,
                text = "${today.day}${stringResource(id = R.string.runWorkout_title)}"
            )
            Log.i(TAG, workoutState.value.toString())
            val weightMod = Modifier.weight(1f)
            when (workoutState.value) {
                WorkoutState.StartOfWorkout -> StartOfWorkoutContent(
                    weightMod,
                    workoutState,
                    today,
                    navController
                )

                WorkoutState.StartOfExercise -> StartOfExerciseContent(
                    weightMod,
                    workoutState,
                    todaysWorkout,
                    workoutCount,
                    exerciseDataBaseManager,
                    currentExercise,
                    currentSet
                )

                WorkoutState.DuringExercise -> DuringExerciseContent(
                    weightMod,
                    workoutState,
                    workoutCount,
                    todaysWorkout,
                    currentExercise,
                    currentSet,
                    scope,
                    settingsManager,
                    context,
                    today
                )

                WorkoutState.RestingBetweenSets -> RestingBetweenSetsContent(
                    weightMod,
                    workoutState,
                    currentExercise,
                    currentSet
                )

                WorkoutState.RestingBetweenExercises -> RestingBetweenExercisesContent(
                    weightMod,
                    workoutState,
                    todaysWorkout,
                    workoutCount
                )

                WorkoutState.EndOfWorkout -> EndOfWorkoutContent(
                    weightMod,
                    navController
                )

                WorkoutState.AlreadyCompleted -> AlreadyCompletedContent(
                    weightMod,
                    workoutState,
                    navController,
                    scope,
                    settingsManager,
                    context
                )

                WorkoutState.NoWorkoutSet -> NoWorkoutSetContent(weightMod, navController)
            }
            val isPopupVisible = remember { mutableStateOf(false) }
            if (workoutState.value != WorkoutState.NoWorkoutSet && workoutState.value != WorkoutState.AlreadyCompleted) {
                StandardButton(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 15.dp),
                    text = stringResource(id = R.string.runWorkout_button_endEarly),
                    onClick = {
                        isPopupVisible.value = true
                    },
                    color = Red40
                ) {
                    AlertPopup(
                        isVisible = isPopupVisible,
                        titleString = stringResource(id = R.string.runWorkout_confirmExit_title),
                        contentString = stringResource(id = R.string.runWorkout_confirmExit_content),
                        onConfirmRequest = {
                            isPopupVisible.value = false
                            navController.navigateUp()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StartOfWorkoutContent(
    modifier: Modifier,
    workoutState: MutableState<WorkoutState>,
    today: WorkoutDay,
    navController: NavHostController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 20.dp),
            text = stringResource(id = R.string.runWorkout_welcomeMessage),
            style = runWorkoutTitleStyle
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 10.dp),
            text = "${stringResource(id = R.string.runWorkout_welcomeMessageSub)} ${if (today.workoutName != "") today.workoutName else "Unnamed"}!",
            style = runWorkoutTextStyleBig
        )
        StandardButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 70.dp)
                .height(60.dp)
                .width(200.dp),
            text = stringResource(id = R.string.runWorkout_button_startWorkout),
            onClick = {
                workoutState.value = WorkoutState.StartOfExercise
            }
        )
        StandardButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp),
            text = stringResource(id = R.string.runWorkout_button_cancelWorkout),
            onClick = {
                navController.navigateUp()
            },
            color = Red40
        )
    }
}

@Composable
fun StartOfExerciseContent(
    modifier: Modifier,
    workoutState: MutableState<WorkoutState>,
    todaysWorkout: ArrayList<ExerciseWorkoutPairing>,
    workoutCount: MutableState<Int>,
    exerciseDataBaseManager: ExerciseDataBaseManager,
    currentExercise: MutableState<Exercise?>,
    currentSet: MutableState<Int>
) {
    val currentExercisePair = todaysWorkout[workoutCount.value]
    currentExercise.value = exerciseDataBaseManager.getExercise(currentExercisePair.exerciseID)
    currentSet.value = 0
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 20.dp),
            text = "${(workoutCount.value + 1).toOrdinal()} " +
                    if (workoutCount.value + 1 == todaysWorkout.size) "${stringResource(id = R.string.runWorkout_subTitle_andFinal)} " else "" +
                            stringResource(id = R.string.runWorkout_subTitle_exercise),
            style = runWorkoutTitleStyle
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 10.dp),
            text = if (currentExercise.value!!.name != "")
                currentExercise.value!!.name
            else
                stringResource(id = R.string.runWorkout_subTitle_unnamed),
            style = runWorkoutTextStyleBig
        )
        Text(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            text = stringResource(id = R.string.exercise_sets_prefix) + " " + currentExercise.value!!.sets,
            style = workoutTitleStyleCentered
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.exercise_reps_prefix) + " " + currentExercise.value!!.reps,
            style = workoutTitleStyleCentered
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.exercise_weight_prefix) + " " + currentExercise.value!!.weightInKilos + stringResource(
                id = R.string.exercise_weight_suffix
            ),
            style = workoutTitleStyleCentered
        )
        StandardButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 70.dp)
                .height(60.dp)
                .width(200.dp),
            text = stringResource(id = R.string.runWorkout_button_startExercise),
            onClick = {
                workoutState.value = WorkoutState.DuringExercise
            }
        )
    }
}

@Composable
fun DuringExerciseContent(
    modifier: Modifier,
    workoutState: MutableState<WorkoutState>,
    workoutCount: MutableState<Int>,
    todaysWorkout: ArrayList<ExerciseWorkoutPairing>,
    currentExercise: MutableState<Exercise?>,
    currentSet: MutableState<Int>,
    scope: CoroutineScope,
    settingsManager: SettingsDataStoreHelper,
    context: Context,
    today: WorkoutDay
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 80.dp),
            text = ("${stringResource(id = R.string.runWorkout_sets_prefixNoColon)} ${currentSet.value+1} of ${currentExercise.value!!.sets}"),
            style = runWorkoutTitleStyle
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 10.dp),
            text = "${stringResource(id = R.string.runWorkout_subTitle_exercise)}: " +
                    if (currentExercise.value!!.name != "") currentExercise.value!!.name else stringResource(
                        id = R.string.runWorkout_subTitle_unnamed
                    ),
            style = runWorkoutTextStyleBig
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.exercise_reps_prefix) + " " + currentExercise.value!!.reps,
            style = workoutTitleStyleCentered
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.exercise_weight_prefix) + " " + currentExercise.value!!.weightInKilos + stringResource(
                id = R.string.exercise_weight_suffix
            ),
            style = workoutTitleStyleCentered
        )
        StandardButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 70.dp)
                .height(60.dp)
                .width(200.dp),
            text = stringResource(id = R.string.runWorkout_button_endSet),
            onClick = {
                currentSet.value = currentSet.value+1
                if (currentSet.value >= currentExercise.value!!.sets) {
                    workoutCount.value = workoutCount.value + 1
                    if (workoutCount.value == todaysWorkout.size) {
                        workoutState.value = WorkoutState.EndOfWorkout
                        scope.launch {
                            settingsManager.saveDataBoolean(
                                context = context,
                                stringKey = ISTODAYCOMPLETEDKEY,
                                newValue = true
                            )
                            settingsManager.saveDataString(
                                context = context,
                                stringKey = LASTDAYCOMPLETEDKEY,
                                newValue = today.day
                            )
                        }
                    } else {
                        workoutState.value = WorkoutState.RestingBetweenExercises
                    }
                } else {
                    workoutState.value = WorkoutState.RestingBetweenSets
                }
            }
        )
    }
}

@Composable
fun RestingBetweenSetsContent(
    modifier: Modifier,
    workoutState: MutableState<WorkoutState>,
    currentExercise: MutableState<Exercise?>,
    currentSet: MutableState<Int>
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 100.dp),
            text = stringResource(id = R.string.runWorkout_title_rest),
            style = runWorkoutTitleStyle
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.runWorkout_subTitle_setsLeft_prefix)
                    + " " + (currentExercise.value!!.sets - (currentSet.value)).toSets()
                    + " " + stringResource(id = R.string.runWorkout_subTitle_setsLeft_suffix),
            style = workoutTitleStyleCentered
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            text = stringResource(id = getRandomInfo()),
            style = workoutTitleStyleCentered
        )
        StandardButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 70.dp)
                .height(60.dp)
                .width(200.dp),
            text = stringResource(id = R.string.runWorkout_button_nextSet),
            onClick = {
                workoutState.value = WorkoutState.DuringExercise
            }
        )
    }
}

@Composable
fun RestingBetweenExercisesContent(
    modifier: Modifier,
    workoutState: MutableState<WorkoutState>,
    todaysWorkout: ArrayList<ExerciseWorkoutPairing>,
    workoutCount: MutableState<Int>
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 100.dp),
            text = stringResource(id = R.string.runWorkout_title_rest),
            style = runWorkoutTitleStyle
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.runWorkout_subTitle_completedExercise),
            style = workoutTitleStyleCentered
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.runWorkout_subTitle_exercisesLeftPrefix) + " "
             + (todaysWorkout.size - workoutCount.value).toExercises() + " "
             + stringResource(id = R.string.runWorkout_subTitle_exercisesLeftSuffix),
            style = workoutTitleStyleCentered
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            text = stringResource(id = getRandomInfo()),
            style = workoutTitleStyleCentered
        )
        StandardButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 70.dp)
                .height(60.dp)
                .width(200.dp),
            text = stringResource(id = R.string.runWorkout_button_nextExercise),
            onClick = {
                workoutState.value = WorkoutState.StartOfExercise
            }
        )
    }
}

@Composable
fun EndOfWorkoutContent(
    modifier: Modifier,
    navController: NavHostController
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 100.dp),
            text = stringResource(id = R.string.runWorkout_title_workoutComplete),
            style = runWorkoutTitleStyle
        )
        StandardButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 100.dp)
                .height(60.dp)
                .width(200.dp),
            text = stringResource(id = R.string.runWorkout_button_goBack),
            onClick = {
                navController.navigateUp()
            }
        )
    }
}

@Composable
fun AlreadyCompletedContent(
    modifier: Modifier,
    workoutState: MutableState<WorkoutState>,
    navController: NavHostController,
    scope: CoroutineScope,
    settingsManager: SettingsDataStoreHelper,
    context: Context
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 100.dp),
            text = stringResource(id = R.string.runWorkout_title_alreadyCompleted),
            style = runWorkoutTitleStyle
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.runWorkout_subTitle_doWorkoutAgain),
            style = workoutTitleStyleCentered
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StandardButton(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(top = 70.dp),
                text = stringResource(id = R.string.runWorkout_button_redoWorkout),
                onClick = {
                    scope.launch {
                        settingsManager.saveDataBoolean(
                            context = context,
                            stringKey = ISTODAYCOMPLETEDKEY,
                            newValue = false
                        )

                    }
                    workoutState.value = WorkoutState.StartOfWorkout
                }
            )
            StandardButton(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(top = 70.dp),
                text = stringResource(id = R.string.runWorkout_button_goBack),
                onClick = {
                    workoutState.value = WorkoutState.AlreadyCompleted
                    navController.navigateUp()
                }
            )
        }
    }
}

@Composable
fun NoWorkoutSetContent(
    modifier: Modifier,
    navController: NavHostController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 20.dp),
            text = stringResource(id = R.string.runWorkout_welcomeMessageNoExercises),
            style = runWorkoutTitleStyle
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 10.dp),
            text = stringResource(id = R.string.runWorkout_welcomeMessageNoExercisesSub),
            style = runWorkoutTextStyleBig
        )
        StandardButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 70.dp),
            text = stringResource(id = R.string.runWorkout_button_goBack),
            onClick = {
                navController.navigateUp()
            }
        )
    }
}

fun getRandomInfo(): Int {
    val list = intArrayOf(
        R.string.exercise_content_recommendedRestComment1,
        R.string.exercise_content_recommendedRestComment2,
        R.string.exercise_content_recommendedRestComment3,
        R.string.exercise_content_recommendedRestComment4
    )
    return list[Random.nextInt(list.size)]
}