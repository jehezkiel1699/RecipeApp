package com.example.assignment3_recipeapp.retrofit

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {
    private val BASE_URL = "https://api.spoonacular.com/"
    val retrofitService : RetrofitInterface by lazy {
        Log.i("RetrofitObject", "called")
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) .addConverterFactory(GsonConverterFactory.create()) .build()
        retrofit.create(RetrofitInterface::class.java)
    }



}