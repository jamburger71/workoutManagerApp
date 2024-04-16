package uk.ac.aber.dcs.cs31620.assignment.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uk.ac.aber.dcs.cs31620.assignment.R
import uk.ac.aber.dcs.cs31620.assignment.ui.database.ExerciseDataBaseManager
import uk.ac.aber.dcs.cs31620.assignment.ui.database.WorkoutDay
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.workoutContentStyle
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.workoutTitleStyle

@Composable
fun ShortWindowContent(
    workoutDay: WorkoutDay,
    exerciseDataBaseManager: ExerciseDataBaseManager
    ) {

    Column(
        modifier = Modifier
            .padding(horizontal = 5.dp)
    ) {
        var exerciseText: String
        if (workoutDay.listOfExercise.isEmpty()) {
            exerciseText = stringResource(id = R.string.workout_noExercisesSet)
        } else {
            exerciseText = "Exercises: "
            workoutDay.listOfExercise.sortBy { exerciseWorkoutPairing ->
                exerciseWorkoutPairing.positionInWorkout
            }
            workoutDay.listOfExercise.forEach { exercisePairing ->
                val exercise = exerciseDataBaseManager.getExercise(exercisePairing.exerciseID)
                exerciseText = exerciseText + exercise.name + ", "
            }
            exerciseText = exerciseText.dropLast(2)
        }
        Text(
            modifier = Modifier
                .height(40.dp),
            text = if (workoutDay.workoutName != "")
                (workoutDay.day + ": " + workoutDay.workoutName)
            else
                workoutDay.day,
            color = Color.White,
            style = workoutTitleStyle,
        )
        Text(
            modifier = Modifier
                .height(40.dp),
            text = exerciseText,
            color = Color.White,
            style = workoutContentStyle
        )
    }
}