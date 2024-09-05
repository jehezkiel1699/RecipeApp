package com.example.assignment3_recipeapp.view.navbar

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.assignment3_recipeapp.page.navbar.UserNavBarItem
import com.example.assignment3_recipeapp.page.user.UserProfile
import com.example.assignment3_recipeapp.view.user.UserHome
import com.example.assignment3_recipeapp.view.user.UserList
import com.example.assignment3_recipeapp.viewModel.RetrofitViewModel
import com.example.assignment3_recipeapp.ui.theme.Primary
import com.example.assignment3_recipeapp.view.user.RecipeDetail
import com.example.assignment3_recipeapp.viewModel.SharedViewModel
import com.example.assignment3_recipeapp.viewModel.UserFavouriteViewModel
import com.example.assignment3_recipeapp.viewModel.UserFirebaseViewModel


/**
 * Composable function for displaying the bottom navigation bar for the user.
 *
 * @param retrofitViewModel The Retrofit ViewModel.
 * @param userFirebaseViewModel The UserFirebase ViewModel.
 * @param sharedViewModel The Shared ViewModel.
 * @param userFavoriteViewModel The UserFavourite ViewModel.
 */
@Composable
fun UserBottomNavigationBar(
    retrofitViewModel: RetrofitViewModel,
    userFirebaseViewModel: UserFirebaseViewModel,
    sharedViewModel: SharedViewModel,
    userFavoriteViewModel: UserFavouriteViewModel
){

    val navController = rememberNavController()
    Scaffold(

        bottomBar = {
            BottomNavigation(
                backgroundColor = Primary,
//                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                UserNavBarItem().userNavBarItems().forEach { navItem ->
                    BottomNavigationItem(
                        icon = { Icon(navItem.icon, contentDescription = null) },
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
            startDestination = Routes.UserHome.value,
            Modifier.padding(paddingValues)
        ) {
            composable(Routes.UserHome.value) {
                UserHome(navController, retrofitViewModel, sharedViewModel, userFirebaseViewModel)
            }
            composable(Routes.RecipeDetail.value) { // Add composable for RecipeDetail
                RecipeDetail(navController, sharedViewModel, userFirebaseViewModel, userFavoriteViewModel)
            }
            composable(Routes.UserList.value) {
                UserList(sharedViewModel, userFirebaseViewModel, navController, sharedViewModel)
            }
            composable(Routes.UserProfile.value) {
                UserProfile(userFirebaseViewModel = userFirebaseViewModel)
            }
        }
    }
}
