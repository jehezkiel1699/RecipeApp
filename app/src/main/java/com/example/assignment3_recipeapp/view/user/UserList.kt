package com.example.assignment3_recipeapp.view.user

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.assignment3_recipeapp.R
import com.example.assignment3_recipeapp.model.recipe.Recipe
import com.example.assignment3_recipeapp.ui.theme.Background
import com.example.assignment3_recipeapp.ui.theme.Primary
import com.example.assignment3_recipeapp.view.components.RecipeGrid
import com.example.assignment3_recipeapp.view.navbar.Routes
import com.example.assignment3_recipeapp.viewModel.RetrofitViewModel
import com.example.assignment3_recipeapp.viewModel.SharedViewModel
import com.example.assignment3_recipeapp.viewModel.UserFavouriteViewModel
import com.example.assignment3_recipeapp.viewModel.UserFirebaseViewModel

/**
 * Composable function for displaying the user's list screen.
 *
 * @param navController The navigation controller.
 * @param viewModel The Retrofit ViewModel for fetching data.
 * @param sharedViewModel The Shared ViewModel for sharing data across composables.
 * @param userFirebaseViewModel The user Firebase ViewModel for managing user data.
 */
@Composable
fun UserList(
    viewModel: SharedViewModel,  // View model for managing shared data
    userFirebaseViewModel: UserFirebaseViewModel,  // View model for user authentication and data
    navController: NavHostController,  // NavController for navigating between composables
    sharedViewModel: SharedViewModel  // SharedViewModel for managing shared data across composables
) {
    val currentUser by userFirebaseViewModel.currentUser.observeAsState()
    val favoriteRecipes by viewModel.favoriteRecipes.observeAsState(initial = emptyList())

    // Fetch favorite recipes when the userEmail updates
    LaunchedEffect(key1 = currentUser) {
        currentUser?.email?.let { email ->
            viewModel.fetchFavoriteRecipes(email)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Background)
    ){
        Column(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        ){
            Text(
                text = "Favorites for ${currentUser?.name ?: "Not logged in"}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Primary)
                    .padding(16.dp, 32.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ){
            // Optionally include a search bar (mock functionality)
            SearchComponent()

            // Displaying favorite recipes in rows of two
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ){
                item{
                    if (favoriteRecipes.isNotEmpty()){

                        val chunkedRecipes = favoriteRecipes.chunked(2)



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
                        androidx.compose.material.Text("No recipes found.")
                    }

                }
            }

            if (favoriteRecipes.isEmpty()) {
                Text("No favorite recipes found.")
            }
        }
    }
}

@Composable
fun SearchComponent() {
    var searchText by remember { mutableStateOf("") }
    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        label = { Text("Search Recipes") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun RecipeCard(recipe: Recipe, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.5f)  // Adjust the width for grid display
            .padding(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = rememberImagePainter(
                    data = recipe.strMealThumb,
                    builder = {
                        crossfade(true)
//                        placeholder(R.drawable.placeholder) // Assuming you have a placeholder drawable
                    }
                ),
                contentDescription = recipe.strMeal,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp) // Adjust the height as needed
                    .clip(shape = RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = recipe.strMeal,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Button(
                onClick = {
                    // Navigate to the detailed view
                    navController.navigate(Routes.RecipeDetail.value)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("View Details")
            }
        }
    }
}
