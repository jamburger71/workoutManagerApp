package uk.ac.aber.dcs.cs31620.assignment.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import uk.ac.aber.dcs.cs31620.assignment.ui.components.IconGrouping
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.Blue60
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.titleTextStyle

@Composable
fun TopTitleBar(
    navController: NavController,
    icons: Map<Screen, IconGrouping>,
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var text = ""
    topLevelScreens.forEach { screen ->
        if (currentDestination?.hierarchy?.any { it.route == screen.route } == true) {
            text = if (icons[screen]!!.titleText != null) icons[screen]!!.titleText!! else ""
        }
    }
    Text(
        modifier = Modifier
            .background(
                color = Blue60
            )
            .height(77.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        style = titleTextStyle,
        text = text
    )
}