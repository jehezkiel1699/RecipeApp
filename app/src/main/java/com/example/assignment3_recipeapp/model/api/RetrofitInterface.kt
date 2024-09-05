package com.example.assignment3_recipeapp.model.api

import com.example.assignment3_recipeapp.model.recipe.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Defines the Retrofit service interface for making API calls.
 */
interface RetrofitInterface {

    /**
     * Performs a custom search in the recipe database.
     *
     * @param s The search query string.
     * @return [RecipeResponse] containing the list of recipes matching the query.
     */
    @GET("api/json/v1/1/search.php")
    suspend fun customSearch(
        @Query("s") s: String
    ): RecipeResponse
}