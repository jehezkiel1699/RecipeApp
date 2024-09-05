package com.example.assignment3_recipeapp

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.assignment3_recipeapp.model.repository.FirebaseRepository
import com.example.assignment3_recipeapp.page.navbar.AdminBottomNavigationBar
import com.example.assignment3_recipeapp.page.navbar.AppMainNavigation
import com.example.assignment3_recipeapp.view.navbar.UserBottomNavigationBar
import com.example.assignment3_recipeapp.viewModel.RetrofitViewModel
import com.example.assignment3_recipeapp.ui.theme.Assignment3_RecipeAppTheme
import com.example.assignment3_recipeapp.viewModel.SharedViewModel
import com.example.assignment3_recipeapp.viewModel.UserFavouriteViewModel
import com.example.assignment3_recipeapp.viewModel.UserFirebaseViewModel
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val retrofitModel: RetrofitViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val userFirebaseViewModel : UserFirebaseViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

//        useFirebaseViewModel.initFirebaseAdmin()


        setContent {
            val viewModel: com.example.assignment3_recipeapp.room.user.UserViewModel by viewModels()
            Assignment3_RecipeAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val userFirebaseViewModel: UserFirebaseViewModel by viewModels()
                    val retrofitViewModel: RetrofitViewModel by viewModels()
                    val userViewModel: com.example.assignment3_recipeapp.room.user.UserViewModel by viewModels()
                    val userFavouriteViewModel: UserFavouriteViewModel by viewModels()
                    val showBottomNavUser = navController.currentBackStackEntryFlow.collectAsState(initial = null)
                        .value?.destination?.route == "userHome"
                    val showBottomNavAdmin = navController.currentBackStackEntryFlow.collectAsState(initial = null)
                        .value?.destination?.route == "adminHome"
                    AppMainNavigation(navController = navController,
                        userViewModel = userViewModel,
                        retrofitViewModel = retrofitViewModel,
                        userFirebaseViewModel = userFirebaseViewModel,
                        sharedViewModel = sharedViewModel
                    )

                    if (showBottomNavUser) {
                        UserBottomNavigationBar(retrofitViewModel, userFirebaseViewModel, sharedViewModel, userFavouriteViewModel)
                    }
                    if (showBottomNavAdmin) {
                        AdminBottomNavigationBar(userFirebaseViewModel)


                    }
                }
            }
        }
    }
}





