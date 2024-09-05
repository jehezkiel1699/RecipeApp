package com.example.assignment3_recipeapp.page.navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.assignment3_recipeapp.view.navbar.Routes
/**
 * Data class representing an item in the user navigation bar.
 *
 * @param label The label for the navigation item.
 * @param icon The icon associated with the navigation item.
 * @param route The route associated with the navigation item.
 */
data class UserNavBarItem(
    val label : String = "",
    val icon : ImageVector = Icons.Filled.Home,
    val route : String = ""
){
    /**
     * Function to generate a list of user navigation bar items.
     *
     * @return List of UserNavBarItem instances.
     */
    fun userNavBarItems(): List<UserNavBarItem>{
        return listOf(
            UserNavBarItem(
                label = "Home",
                icon = Icons.Filled.Home,
                route = Routes.UserHome.value
            ),
            UserNavBarItem(
                label = "My List",
                icon = Icons.Filled.AccountCircle,
                route = Routes.UserList.value
            ),
            UserNavBarItem(
                label = "My Profile",
                icon = Icons.Filled.Person,
                route = Routes.UserProfile.value
            )
        )
    }
}