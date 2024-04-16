package uk.ac.aber.dcs.cs31620.assignment.ui.navigation
/**
 * Wraps as objects, singletons for each screen used in
 * navigation. Each has a unique route.
 * @param route To pass through the route string
 * @author Jay Kirkham
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Settings : Screen("settings")
    object Exercises : Screen("exercises")
    object Workout : Screen("workout")
    object AddOrCreateExercise : Screen("add_or_create_exercise")
    object CreateExercise : Screen("create_exercise")
}

/**
 * List of top-level screens provided as a convenience.
 */
val topLevelScreens = listOf(
    Screen.Home,
    Screen.Exercises,
    Screen.Settings,
    Screen.Workout
)