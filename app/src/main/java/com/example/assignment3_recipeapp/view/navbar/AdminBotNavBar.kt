package com.example.assignment3_recipeapp.page.navbar
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import com.example.assignment3_recipeapp.model.room.Administrator.getUsersFromDatabase
import com.example.assignment3_recipeapp.page.admin.AdminManageUser
import com.example.assignment3_recipeapp.page.admin.AdminReport
import com.example.assignment3_recipeapp.room.user.User
import com.example.assignment3_recipeapp.ui.theme.Primary
import com.example.assignment3_recipeapp.viewModel.UserFirebaseViewModel
/**
 * Renders the bottom navigation bar for admin pages, including navigation setup and handling.
 *
 * @param userFirebaseViewModel ViewModel used for managing Firebase user data within admin screens.
 */
@Composable
fun AdminBottomNavigationBar(userFirebaseViewModel: UserFirebaseViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current
    Scaffold(
        bottomBar = {
            BottomNavigation (backgroundColor= Primary ){
                val navBackStackEntry by
                navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                AdminNavBarItem().adminNavBarItems().forEach { navItem ->
                    BottomNavigationItem(
                        icon = { Icon(navItem.icon, contentDescription =
                        null) },
                        label = { Text(navItem.label) },
                        selected = currentDestination?.hierarchy?.any {
                            it.route == navItem.route
                        } == true,
                        onClick = {
                            navController.navigate(navItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController,
            startDestination = AdminRoutes.ManageUser.value,
            Modifier.padding(paddingValues)
        ) {
            composable(AdminRoutes.ManageUser.value) {
                AdminManageUser(navController, userFirebaseViewModel)
            }
            composable(AdminRoutes.Report.value) {
                val users: List<User> by remember { mutableStateOf(getUsersFromDatabase(context)) }
                AdminReport(navController, userFirebaseViewModel)
            }
        }
    }
}