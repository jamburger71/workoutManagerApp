package uk.ac.aber.dcs.cs31620.assignment.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import uk.ac.aber.dcs.cs31620.assignment.ui.components.IconGrouping
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.Blue40
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.Blue60

@Composable
fun BottomNavBar(
    navController: NavController,
    icons: Map<Screen, IconGrouping>
) {
    val bottomBarState = rememberSaveable { true }
    val barColors: NavigationBarItemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color.White,
        unselectedIconColor = Color.White,
        indicatorColor = Blue40
    )
    AnimatedVisibility(
        modifier = Modifier
            .background(Color.Transparent),
        visible = bottomBarState
    ) {
        NavigationBar(
            modifier = Modifier
                .height(75.dp),
            containerColor = Blue60
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            topLevelScreens.forEach { screen ->
                val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                val labelText = icons[screen]!!.label
                NavigationBarItem(
                    icon = {
                        Icon(
                            modifier = Modifier
                                .height(60.dp)
                                .width(40.dp),
                            imageVector = (if (isSelected)
                                icons[screen]!!.filledIcon
                            else
                                icons[screen]!!.outlineIcon),
                            contentDescription = labelText
                        )
                    },
                    colors = barColors,
                    selected = isSelected,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        }
    }
}