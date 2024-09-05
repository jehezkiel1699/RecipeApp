package com.example.assignment3_recipeapp.view.user

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.assignment3_recipeapp.ui.theme.Assignment3_RecipeAppTheme
import com.example.assignment3_recipeapp.view.components.RecipeGrid
import com.example.assignment3_recipeapp.view.components.Search
import com.example.assignment3_recipeapp.viewModel.RetrofitViewModel
import com.example.assignment3_recipeapp.ui.theme.Background
import com.example.assignment3_recipeapp.ui.theme.Primary
import com.example.assignment3_recipeapp.view.navbar.Routes
import com.example.assignment3_recipeapp.viewModel.SharedViewModel
import com.example.assignment3_recipeapp.viewModel.UserFirebaseViewModel

/**
 * Composable function for displaying the user's home screen.
 *
 * @param navController The navigation controller.
 * @param viewModel The Retrofit ViewModel for fetching data.
 * @param sharedViewModel The Shared ViewModel for sharing data across composables.
 * @param userFirebaseViewModel The user Firebase ViewModel for managing user data.
 */
@Composable
fun UserHome(
    navController: NavHostController,
    viewModel: RetrofitViewModel,
    sharedViewModel: SharedViewModel,
    userFirebaseViewModel: UserFirebaseViewModel
){

    var search by remember { mutableStateOf("") }
    val menuReturned by viewModel.retrofitResponse
    val recipes = menuReturned.meals
    val currentUser by userFirebaseViewModel.currentUser.observeAsState()


    LaunchedEffect(search) {
        if (search.isNotEmpty()) {
            viewModel.getResponse(search)
            Log.i("UserHome", "Fetching data for search: $search")
        } else {
            viewModel.getResponse("")  // Optionally handle the initial load or empty search query
            Log.i("UserHome", "Fetching default or initial data.")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Background)
    ){
        Column (
            modifier = Modifier
                .padding(
                    bottom = 16.dp
                )
                .align(Alignment.CenterHorizontally)
        ){
            Text(
                text = "Hi, ${currentUser?.name ?: "Not logged in"}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Primary)
                    .padding(
                        16.dp,
                        32.dp
                    )
            )
        }

        Column (
            modifier = Modifier
                .padding(16.dp)
        ){

            // Search
            Search(
                search = search,
                onValueChange = { search = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // List of the recipe
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ){
                item{
                    if (recipes.isNotEmpty()){

                        val chunkedRecipes = recipes.chunked(2)



                        for (recipe in chunkedRecipes) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                if (recipe.isNotEmpty()) {
                                    val recipe1 = recipe[0] // Get the first recipe
                                    RecipeGrid(
                                        title = recipe1.strMeal,
                                        imageUrl = recipe1.strMealThumb,
                                        contentDescription = recipe1.strMeal,
                                        onClick = {
                                            sharedViewModel.recipes.value = recipe1
                                            navController.navigate(Routes.RecipeDetail.value)


                                        }
                                    )
                                }

                                if (recipe.size > 1) { // Check if there's a second recipe
                                    val recipe2 = recipe[1]
                                    RecipeGrid(
                                        title = recipe2.strMeal,
                                        imageUrl = recipe2.strMealThumb,
                                        contentDescription = recipe2.strMeal,
                                        onClick = {
                                            sharedViewModel.recipes.value = recipe2
                                            Log.i("RECIPE DETAILS", recipe2.toString())

                                            navController.navigate(Routes.RecipeDetail.value)

                                        }
                                    )
                                }
                            }
                        }

                    } else{
                        Text("No recipes found.")
                    }

                }
            }
        }

    }

}
