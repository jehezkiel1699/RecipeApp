package com.example.assignment3_recipeapp.page.navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents an item in the admin navigation bar.
 *
 * @property label The display label of the navigation item.
 * @property icon The icon associated with the navigation item.
 * @property route The navigation route associated with the item.
 */
data class AdminNavBarItem (
    val label : String = "",
    val icon : ImageVector = Icons.Filled.Home,
    val route : String = ""
) {
    fun adminNavBarItems(): List<AdminNavBarItem> {
        return listOf(
            AdminNavBarItem(
                label = "Manage User",
                icon = Icons.Filled.Person,
                route = AdminRoutes.ManageUser.value
            ),
            AdminNavBarItem(
                label = "Report",
                icon = Icons.Filled.Info,
                route = AdminRoutes.Report.value
            )
        )
    }
}