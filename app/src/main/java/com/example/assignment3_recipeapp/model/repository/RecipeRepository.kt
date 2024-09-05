package com.example.assignment3_recipeapp.model.repository

import com.example.assignment3_recipeapp.model.api.RetrofitObject
import com.example.assignment3_recipeapp.model.recipe.RecipeResponse

/**
 * Repository class for managing recipe data retrieval.
 */
class RecipeRepository {

    // Instance of the Retrofit service for making API calls.
    private val searchService = RetrofitObject.retrofitService

    /**
     * Fetches a recipe response based on the given keyword.
     *
     * @param keyword The search term used to query recipes.
     * @return A [RecipeResponse] containing the list of recipes that match the search criteria.
     */
    suspend fun getResponse(keyword: String): RecipeResponse {
        return searchService.customSearch(keyword)
    }
}
