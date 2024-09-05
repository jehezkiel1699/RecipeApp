package com.example.assignment3_recipeapp.model.user

import com.example.assignment3_recipeapp.model.recipe.Recipe

/**
 * Data class representing a user's favorite recipes.
 * This class encapsulates user email and their list of favorite recipes.
 *
 * @property email The email of the user. Used as an identifier for the user.
 * @property favoriteRecipes A list of [Recipe] objects representing the user's favorite recipes.
 */
data class UserFavourite (
    val email: String,
    val favoriteRecipes: List<Recipe> = listOf() // Defaulting to an empty list if no favorites are provided.
)
