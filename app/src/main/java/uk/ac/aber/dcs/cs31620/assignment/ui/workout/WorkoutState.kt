package uk.ac.aber.dcs.cs31620.assignment.ui.workout

enum class WorkoutState {
    StartOfWorkout,
    StartOfExercise,
    DuringExercise,
    RestingBetweenSets,
    RestingBetweenExercises,
    EndOfWorkout,
    AlreadyCompleted,
    NoWorkoutSet
}