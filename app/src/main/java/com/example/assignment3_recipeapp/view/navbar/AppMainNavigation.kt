package com.example.assignment3_recipeapp.page.navbar

import com.example.assignment3_recipeapp.viewModel.UserFirebaseViewModel
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.annotation.RequiresApi
import com.example.assignment3_recipeapp.page.user.LoginScreen
import com.example.assignment3_recipeapp.page.user.RegistrationForm
import com.example.assignment3_recipeapp.room.user.UserViewModel
import com.example.assignment3_recipeapp.page.admin.AdminManageUser
import com.example.assignment3_recipeapp.page.user.RegistrationSuccessScreen
import com.example.assignment3_recipeapp.view.user.UserHome
import com.example.assignment3_recipeapp.viewModel.RetrofitViewModel
import com.example.assignment3_recipeapp.viewModel.SharedViewModel
/**
 * A composable function representing the main navigation of the app.
 * This function navigates between different screens based on the provided [navController].
 *
 * @param navController The navigation controller.
 * @param userViewModel The ViewModel for managing user data.
 * @param retrofitViewModel The ViewModel for handling Retrofit operations.
 * @param userFirebaseViewModel The ViewModel for Firebase user management.
 * @param sharedViewModel The shared ViewModel for communication between components.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppMainNavigation(
    navController: NavHostController,
    userViewModel: UserViewModel,
    retrofitViewModel: RetrofitViewModel,
    userFirebaseViewModel: UserFirebaseViewModel,
    sharedViewModel: SharedViewModel
) {
    NavHost(navController = navController, startDestination = "loginScreen") {
        composable("loginScreen") {
            LoginScreen(navController, userViewModel, userFirebaseViewModel)
        }
        composable("registrationForm") {
            RegistrationForm(navController, userViewModel, userFirebaseViewModel)
        }
        composable("registerSuccessScreen") {
            RegistrationSuccessScreen(navController)
        }
        composable("userHome") {
            UserHome(navController, retrofitViewModel, sharedViewModel, userFirebaseViewModel)
        }
        composable("adminHome") {
            AdminManageUser(navController, userFirebaseViewModel)
        }
    }
}

