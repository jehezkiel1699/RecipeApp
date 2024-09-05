package com.example.assignment3_recipeapp.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment3_recipeapp.model.recipe.RecipeResponse
import com.example.assignment3_recipeapp.model.repository.RecipeRepository
import kotlinx.coroutines.launch
import java.lang.Exception

// ViewModel for managing API responses via Retrofit.
class RetrofitViewModel: ViewModel() {
    // Repository instance for accessing data operations.
    private val repository = RecipeRepository()

    // Mutable state to hold and observe the API response.
    val retrofitResponse: MutableState<RecipeResponse> = mutableStateOf(RecipeResponse())

    /**
     * Fetches response from the repository based on the provided keyword.
     * @param keyword the search keyword to fetch the recipe response
     */
    fun getResponse(keyword: String) {
        viewModelScope.launch { // Launches a coroutine in the ViewModel's scope
            try {
                val responseReturned = repository.getResponse(keyword)
                retrofitResponse.value = responseReturned // Updates the mutable state with the new response
                Log.i("Successfully returned", responseReturned.toString()) // Logs the response for debugging
            } catch (e: Exception) {
                Log.i("Error", e.toString()) // Logs exception if something goes wrong
            }
        }
    }
}