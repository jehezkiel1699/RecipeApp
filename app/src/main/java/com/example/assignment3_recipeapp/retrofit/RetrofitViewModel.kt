package com.example.assignment3_recipeapp.retrofit

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception

class RetrofitViewModel: ViewModel() {
    private val repository = RecipeRepository()
    val retrofitResponse: MutableState<RecipeResponse> = mutableStateOf(RecipeResponse())
    fun menuResponse() {
        viewModelScope.launch{
            try{
                val responseReturned = repository.getResponse()
                retrofitResponse.value = responseReturned

                Log.i("Test2", responseReturned.toString())
            } catch (e: Exception) {
                Log.i("Error","Response failed")
            }
        }
//        viewModelScope.launch {
//            try {
//                val responseReturned = repository.getResponse()
//                retrofitResponse.value = responseReturned
//            } catch (e: Exception) {
//                Log.i("Error ", "Response failed")
//            }
//        }
    }


}