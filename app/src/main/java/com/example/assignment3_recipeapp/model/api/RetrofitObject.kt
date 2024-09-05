package com.example.assignment3_recipeapp.model.api

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton object to initialize and provide a Retrofit service instance.
 */
object RetrofitObject {

    // Base URL for the API endpoint
    private const val BASE_URL = "https://www.themealdb.com/"

    /**
     * Lazily initialized Retrofit service.
     */
    val retrofitService: RetrofitInterface by lazy {
        // Log initialization to help with debugging
        Log.i("RetrofitObject", "Creating Retrofit instance")

        // Configure OkHttpClient with timeouts
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Set connect timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Set read timeout
            .writeTimeout(30, TimeUnit.SECONDS)   // Set write timeout
            .build()

        // Build the Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Use Gson for JSON conversion
            .client(okHttpClient)
            .build()

        // Create the Retrofit service from the interface
        retrofit.create(RetrofitInterface::class.java)
    }
}
