package com.example.assignment3_recipeapp.view.user

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.assignment3_recipeapp.model.recipe.Recipe
import com.example.assignment3_recipeapp.model.user.UserFirebase
import com.example.assignment3_recipeapp.view.navbar.Routes
import com.example.assignment3_recipeapp.ui.theme.Background
import com.example.assignment3_recipeapp.ui.theme.Primary
import com.example.assignment3_recipeapp.viewModel.SharedViewModel
import com.example.assignment3_recipeapp.viewModel.UserFavouriteViewModel
import com.example.assignment3_recipeapp.viewModel.UserFirebaseViewModel
/**
 * Composable function representing the recipe detail screen.
 *
 * @param navController The navigation controller.
 * @param sharedViewModel The shared ViewModel.
 * @param userFirebaseViewModel The user Firebase ViewModel.
 * @param userFavoriteViewModel The user favorite ViewModel.
 */
@Composable
fun RecipeDetail(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    userFirebaseViewModel: UserFirebaseViewModel,
    userFavoriteViewModel: UserFavouriteViewModel
){
    var isLiked by remember { mutableStateOf(false) }
    val currentUser by userFirebaseViewModel.currentUser.observeAsState()
    var userEmail = currentUser?.email ?: ""
    var recipe = sharedViewModel.recipes.value

    // States for comments
    var commentText by remember { mutableStateOf("") }
    /**
     * Function to add the recipe to favorites.
     *
     * @param recipe The recipe to add to favorites.
     */
    fun addToFavorites(recipe: Recipe) {
        userFavoriteViewModel.addToFavorites(userEmail, recipe) { success ->
            if (success) {
                Log.i("RecipeDetail", "Added to favorites: ${recipe.strMeal}")
            } else {
                Log.i("RecipeDetail", "Failed to add to favorites")
            }
        }
    }

    if (recipe != null){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Background)
        ){
            Row(horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color(0xFFF9F7C9))
                    .fillMaxWidth()
            ){
                IconButton(
                    onClick = { navController.navigate(Routes.UserHome.value) }
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
                }
                Text("Back", modifier = Modifier.padding(start = 2.dp))
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Background)
            ){
                item{
                    ImageSection(recipe)
                    RecipeDetails(recipe, isLiked, currentUser, { isLiked = it }, { addToFavorites(recipe) })
                    CommentSection(userFavoriteViewModel, userEmail, recipe.idMeal, commentText, { commentText = it }) // Modify here for adding comments
                }
            }
        }
    }
}

@Composable
fun ImageSection(recipe: Recipe) {
    AsyncImage(
        model = recipe.strMealThumb,
        contentDescription = recipe.strMeal,
        modifier = Modifier
            .aspectRatio(1f),
        contentScale = ContentScale.Fit
    )
}
/**
 * Composable function for displaying the details of the recipe.
 *
 * @param recipe The recipe to display.
 * @param isLiked Flag indicating whether the recipe is liked.
 * @param currentUser The current user.
 * @param onLikeClicked Callback for like button click.
 * @param addToFavorites Callback for adding to favorites.
 */
@Composable
fun RecipeDetails(recipe: Recipe, isLiked: Boolean, currentUser: UserFirebase?, onLikeClicked: (Boolean) -> Unit, addToFavorites: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = recipe.strMeal,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(
            onClick = {
                onLikeClicked(!isLiked)
                if (!isLiked) addToFavorites()
            }
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Like",
                tint = if (isLiked) Color.Red else Color.Gray
            )
        }
        Text(
            "Instruction",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            recipe.strInstructions,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CommentSection(
    viewModel: UserFavouriteViewModel,
    userEmail: String,
    recipeId: String,
    commentText: String,
    onCommentChanged: (String) -> Unit
) {
    val context = LocalContext.current
    OutlinedTextField(
        value = commentText,
        onValueChange = onCommentChanged,
        label = { Text("Add a comment") },
        modifier = Modifier.fillMaxWidth()
    )
    Button(
        onClick = {
            if (commentText.isNotBlank()) {
                viewModel.postComment(userEmail, recipeId, commentText) { success ->
                    if (success) {
                        onCommentChanged("")  // Clear the comment field on success
                        Toast.makeText(context, "Comment successfully posted.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to post comment.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        },
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .background(color = Primary),
        colors = ButtonDefaults.buttonColors(Primary)
    ) {
        Text("Post Comment")
    }
    Spacer(modifier = Modifier.height(32.dp))
}
