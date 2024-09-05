package com.example.assignment3_recipeapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.assignment3_recipeapp.model.recipe.Recipe
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

// ViewModel for managing shared data across components, specifically for recipe data.
class SharedViewModel : ViewModel() {
    // Firebase database reference.
    private val dbRef = Firebase.database.reference

    // LiveData for observing favorite recipes list updates.
    val favoriteRecipes = MutableLiveData<List<Recipe>>()

    // LiveData for observing changes in a single recipe.
    val recipes = MutableLiveData<Recipe>()

    /**
     * Fetches favorite recipes from Firebase for the specified user email.
     * @param userEmail the email of the user whose favorite recipes are to be fetched
     */

    fun fetchFavoriteRecipes(userEmail: String) {
        dbRef.child("favorites").orderByChild("userEmail").equalTo(userEmail)
            .addListenerForSingleValueEvent(object : ValueEventListener {  // Changed to addListenerForSingleValueEvent
                override fun onDataChange(snapshot: DataSnapshot) {
                    val recipesSet = mutableSetOf<Recipe>()
                    snapshot.children.forEach {
                        it.child("recipe").getValue(Recipe::class.java)?.let { recipe ->
                            recipesSet.add(recipe)  // Adds only unique recipes
                        }
                    }
                    favoriteRecipes.postValue(recipesSet.toList())  // Use postValue for thread safety
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Failed to fetch favorite recipes", error.toException())
                }
            })
    }
}

