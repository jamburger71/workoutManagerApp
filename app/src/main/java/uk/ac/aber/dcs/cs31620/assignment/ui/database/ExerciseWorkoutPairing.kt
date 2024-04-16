package uk.ac.aber.dcs.cs31620.assignment.ui.database

data class ExerciseWorkoutPairing(
    val pairID: Int,
    val exerciseID: Int,
    val workoutDay: String,
    var positionInWorkout: Int
)