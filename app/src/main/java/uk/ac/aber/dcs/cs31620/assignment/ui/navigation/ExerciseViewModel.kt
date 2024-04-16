package uk.ac.aber.dcs.cs31620.assignment.ui.navigation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import uk.ac.aber.dcs.cs31620.assignment.ui.database.WorkoutDay

class ExerciseViewModel: ViewModel() {
    private val uiStateFlow = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = uiStateFlow.asStateFlow()

    fun setWorkoutState(workoutDay: WorkoutDay?) {
        uiStateFlow.update { currentState ->
            currentState.copy(currentWorkout = workoutDay)
        }
    }
}