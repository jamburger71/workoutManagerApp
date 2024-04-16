package uk.ac.aber.dcs.cs31620.assignment

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.assignment.ui.database.ExerciseDataBaseManager
import uk.ac.aber.dcs.cs31620.assignment.ui.exercises.CreateNewExerciseScreen
import uk.ac.aber.dcs.cs31620.assignment.ui.exercises.ExercisesScreen
import uk.ac.aber.dcs.cs31620.assignment.ui.home.AddOrCreateExerciseScreen
import uk.ac.aber.dcs.cs31620.assignment.ui.home.HomeScreen
import uk.ac.aber.dcs.cs31620.assignment.ui.navigation.ExerciseViewModel
import uk.ac.aber.dcs.cs31620.assignment.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.assignment.ui.settings.SettingsScreen
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.AssignmentTheme
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.Blue60
import uk.ac.aber.dcs.cs31620.assignment.ui.workout.WorkoutScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AssignmentTheme {

                val context = LocalContext.current
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BackgroundImage()
                    BuildNavigationGraph(context)
                }
            }
        }
    }
}

@Composable
private fun BuildNavigationGraph(
    context: Context,
    viewModel: ExerciseViewModel = viewModel()
) {
    val navController = rememberNavController()
    val exerciseDataBaseManager = ExerciseDataBaseManager(context)

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController, exerciseDataBaseManager, viewModel)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
        composable(Screen.Exercises.route) {
            ExercisesScreen(navController, exerciseDataBaseManager)
        }
        composable(Screen.Workout.route) {

            BackHandler(true) {/* Do nothing */}
            WorkoutScreen(navController, exerciseDataBaseManager)

        }
        composable(Screen.AddOrCreateExercise.route) {
            AddOrCreateExerciseScreen(navController, exerciseDataBaseManager, viewModel)
        }
        composable(Screen.CreateExercise.route) {
            CreateNewExerciseScreen(navController, exerciseDataBaseManager)
        }
    }
}

@Composable
fun BackgroundImage() {
    val bgImage: Int = rememberSaveable { R.drawable.default_background }
    Surface(
        color = Blue60,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 77.dp,
                bottom = 75.dp
            )
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = bgImage),
            alpha = 0.6F,
            contentDescription = stringResource(R.string.background_image_description)
        )
    }
}