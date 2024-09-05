package com.example.assignment3_recipeapp.model.recipe

/**
 * Represents a single recipe object as fetched from the API.
 *
 * @property idMeal The unique identifier for the meal.
 * @property strMeal The name of the meal.
 * @property strMealThumb The URL for the thumbnail image of the meal.
 * @property strInstructions The cooking instructions for the meal.
 */
data class Recipe(
    val idMeal: String = "",           // Defaulting to an empty string if not provided
    val strMeal: String = "",          // Defaulting to an empty string if not provided
    val strMealThumb: String = "",     // Defaulting to an empty string if not provided
    val strInstructions: String = ""   // Defaulting to an empty string if not provided
)