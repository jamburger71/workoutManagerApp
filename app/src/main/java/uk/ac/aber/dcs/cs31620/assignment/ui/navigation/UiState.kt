package uk.ac.aber.dcs.cs31620.assignment.ui.navigation

import uk.ac.aber.dcs.cs31620.assignment.ui.database.WorkoutDay

data class UiState(
    val currentWorkout: WorkoutDay? = null
)
