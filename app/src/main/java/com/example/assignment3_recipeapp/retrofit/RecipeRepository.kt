package com.example.assignment3_recipeapp.retrofit

class RecipeRepository {
    private val recipeService = RetrofitObject.retrofitService
    private val API_KEY = "edd5ac87e0514a77a99de2297a355f33"

    suspend fun getResponse() : RecipeResponse {
        return recipeService.customSearch(
            apiKey = API_KEY
        )
    }
}