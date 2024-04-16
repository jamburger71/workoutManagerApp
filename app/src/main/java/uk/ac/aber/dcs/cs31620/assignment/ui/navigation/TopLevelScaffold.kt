package uk.ac.aber.dcs.cs31620.assignment.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController

import uk.ac.aber.dcs.cs31620.assignment.R
import uk.ac.aber.dcs.cs31620.assignment.ui.components.IconGrouping

/**
 * Creates the page scaffold to contain top app bar, navigation drawer,
 * bottom navigation button and of course the page content.
 * @param navController To pass through the NavHostController since navigation is required
 * @param pageContent So that callers can plug in their own page content.
 * By default an empty lambda.
 * @author Chris Loftus
 */



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopLevelScaffold(
    navController: NavHostController,
    pageContent: @Composable (innerPadding: PaddingValues) -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val icons = mapOf(
        Screen.Home to IconGrouping(
            filledIcon = Icons.Filled.Home,
            outlineIcon = Icons.Outlined.Home,
            label = stringResource(id = R.string.home_button_text),
            iconColor = Color.Red,
            titleText = stringResource(id = R.string.home_title)
        ),
        Screen.Settings to IconGrouping(
            filledIcon = Icons.Filled.Settings,
            outlineIcon = Icons.Outlined.Settings,
            label = stringResource(id = R.string.settings_button_text),
            iconColor = Color.Blue,
            titleText = stringResource(id = R.string.settings_title)
        ),

        Screen.Exercises to IconGrouping(
            filledIcon = Icons.Filled.Person,
            outlineIcon = Icons.Outlined.Person,
            label = stringResource(id = R.string.exercises_button_text),
            iconColor = Color.Magenta,
            titleText = stringResource(id = R.string.exercises_title)
        ),
        Screen.Workout to IconGrouping(
            filledIcon = Icons.Filled.PlayArrow,
            outlineIcon = Icons.Outlined.PlayArrow,
            label = stringResource(id = R.string.workout_button_text),
            iconColor = Color.Green
        )
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = Modifier
            .background(Color.Transparent),
        containerColor = Color.Transparent,
        topBar = {
            TopTitleBar(navController,icons)
        },
        bottomBar = {
            BottomNavBar(navController,icons)
        },
        content =  { innerPadding ->
            pageContent(innerPadding)
        }
    )
}

