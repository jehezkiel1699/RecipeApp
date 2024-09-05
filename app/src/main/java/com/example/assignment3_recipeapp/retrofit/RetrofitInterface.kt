package com.example.assignment3_recipeapp.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {

    @GET("recipes/complexSearch")
    suspend fun customSearch(
        @Query("apiKey") apiKey: String
    ): RecipeResponse

}