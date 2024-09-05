package com.example.assignment3_recipeapp.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.assignment3_recipeapp.model.recipe.Recipe
import com.example.assignment3_recipeapp.model.repository.UserFavouriteRepository

// ViewModel for managing user favorite recipes and comments.
class UserFavouriteViewModel() : ViewModel() {
    // Repository handling all data operations related to user favorites.
    private val userFavouriteRepository = UserFavouriteRepository()

    /**
     * Adds a recipe to the user's favorites.
     * @param userEmail the email of the user
     * @param recipe the recipe to be added to favorites
     * @param callback a callback function returning a Boolean indicating success or failure
     */
    fun addToFavorites(userEmail: String, recipe: Recipe, callback: (Boolean) -> Unit) {
        userFavouriteRepository.addToFavorites(userEmail, recipe, callback)
    }

    /**
     * Posts a comment on a specific recipe.
     * @param userEmail the email of the user posting the comment
     * @param recipeId the ID of the recipe being commented on
     * @param commentText the text of the comment
     * @param callback a callback function returning a Boolean indicating success or failure
     */
    fun postComment(userEmail: String, recipeId: String, commentText: String, callback: (Boolean) -> Unit) {
        userFavouriteRepository.postComment(userEmail, recipeId, commentText, callback)
    }

    /**
     * Removes a recipe from the user's favorites.
     * @param userEmail the email of the user
     * @param recipe the recipe to be removed from favorites
     * @param callback a callback function returning a Boolean indicating success or failure
     */
    fun removeFromFavorites(userEmail: String, recipe: Recipe, callback: (Boolean) -> Unit) {
        userFavouriteRepository.removeFromFavorites(userEmail, recipe, callback)
    }

    /**
     * Retrieves the list of favorite recipes for a user.
     * @param userEmail the email of the user whose favorites are being requested
     * @param callback a callback function to return the list of recipes
     */
    fun getFavoriteRecipes(userEmail: String, callback: (List<Recipe>) -> Unit) {
        userFavouriteRepository.getFavoriteRecipes(userEmail, callback)
    }
}