package com.example.assignment3_recipeapp.model.recipe

/**
 * Data class representing the response from the recipe API.
 *
 * @property meals A list of [Recipe] objects retrieved from the API.
 *                 Defaults to an empty list if no meals are found.
 */
data class RecipeResponse(
    val meals: List<Recipe> = ArrayList()
)
