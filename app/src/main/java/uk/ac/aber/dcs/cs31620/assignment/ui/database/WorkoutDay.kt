package uk.ac.aber.dcs.cs31620.assignment.ui.database


/**
 * Creates a workout day with the day, name, and list of exercises
 *
 * @author Jay Kirkham
 */
data class WorkoutDay(
    var workoutName: String = "",
    val day: String,
    var listOfExercise: ArrayList<ExerciseWorkoutPairing> = ArrayList()
)