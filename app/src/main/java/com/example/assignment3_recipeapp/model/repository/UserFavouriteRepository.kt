package com.example.assignment3_recipeapp.model.repository

import android.util.Log
import com.example.assignment3_recipeapp.model.recipe.Recipe
import com.example.assignment3_recipeapp.model.user.UserFavourite
import com.google.firebase.Firebase
import com.google.firebase.database.*

/**
 * Repository for handling user favorites and comments functionality with Firebase.
 */
class UserFavouriteRepository {
    // Reference to Firebase database
    private val database = Firebase.database.reference

    /**
     * Adds a recipe to the user's list of favorites in Firebase.
     *
     * @param userEmail The email of the user.
     * @param recipe The recipe to add to favorites.
     * @param callback A callback to be invoked with the operation success status.
     */
    fun addToFavorites(userEmail: String, recipe: Recipe, callback: (Boolean) -> Unit) {
        val favoritesRef = database.child("favorites")
        val favoriteId = favoritesRef.push().key  // This will create a new unique node under "favorites"

        val favoriteData = mapOf(
            "userEmail" to userEmail,
            "recipe" to mapOf(
                "idMeal" to recipe.idMeal,
                "strMeal" to recipe.strMeal,
                "strMealThumb" to recipe.strMealThumb,
                "strInstructions" to recipe.strInstructions
            )
        )

        favoriteId?.let {
            favoritesRef.child(it).setValue(favoriteData)
                .addOnSuccessListener {
                    Log.i("UserFavouriteRepository", "Favorite successfully added.")
                    callback(true)
                }
                .addOnFailureListener { exception ->
                    Log.e("UserFavouriteRepository", "Failed to add favorite.", exception)
                    callback(false)
                }
        } ?: Log.e("UserFavouriteRepository", "Failed to generate a unique key for favorites.")
    }

    /**
     * Posts a comment to a specific recipe.
     *
     * @param userEmail The email of the user posting the comment.
     * @param recipeId The ID of the recipe being commented on.
     * @param commentText The content of the comment.
     * @param callback A callback to be invoked with the operation success status.
     */
    fun postComment(userEmail: String, recipeId: String, commentText: String, callback: (Boolean) -> Unit) {
        val commentsRef = database.child("comments")
        val commentId = commentsRef.push().key

        val commentData = mapOf(
            "userEmail" to userEmail,
            "recipeId" to recipeId,
            "comment" to commentText
        )

        commentId?.let {
            commentsRef.child(it).setValue(commentData)
                .addOnSuccessListener {
                    Log.i("UserFavouriteRepository", "Comment successfully posted.")
                    callback(true)
                }
                .addOnFailureListener { exception ->
                    Log.e("UserFavouriteRepository", "Failed to post comment.", exception)
                    callback(false)
                }
        } ?: Log.e("UserFavouriteRepository", "Failed to generate a unique key for comments.")
    }

    /**
     * Removes a recipe from the user's favorites.
     *
     * @param userEmail The email of the user.
     * @param recipe The recipe to remove from favorites.
     * @param callback A callback to be invoked with the operation success status.
     */
    fun removeFromFavorites(userEmail: String, recipe: Recipe, callback: (Boolean) -> Unit) {
        val userRef = database.child("users").child(userEmail.replace(".", "_"))
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(UserFavourite::class.java)?.let { user ->
                    val updatedFavorites = user.favoriteRecipes.filterNot { it.idMeal == recipe.idMeal }.toMutableList()
                    userRef.child("userFavourites").setValue(updatedFavorites)
                        .addOnSuccessListener { callback(true) }
                        .addOnFailureListener { callback(false) }
                } ?: callback(false)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }
        })
    }

    /**
     * Retrieves the list of favorite recipes for a user.
     *
     * @param userEmail The email of the user whose favorites are being requested.
     * @param callback A callback to be invoked with the list of favorite recipes.
     */
    fun getFavoriteRecipes(userEmail: String, callback: (List<Recipe>) -> Unit) {
        val userRef = database.child("users").child(userEmail.replace(".", "_"))
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(UserFavourite::class.java)?.let { user ->
                    callback(user.favoriteRecipes)
                } ?: callback(emptyList())
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }
}
