package com.example.assignment3_recipeapp.model.repository

import android.util.Log
import com.example.assignment3_recipeapp.model.user.UserFirebase
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * Repository for handling Firebase operations related to users.
 */
class FirebaseRepository {
    // Reference to the Firebase database "users" node
    private val databaseReference = FirebaseDatabase.getInstance().getReference("users")

    /**
     * Checks if an email already exists in the Firebase database.
     *
     * @param email The email to check.
     * @return True if the email exists, false otherwise.
     */
    suspend fun emailExists(email: String): Boolean {
        val snapshot = databaseReference.orderByChild("email").equalTo(email).get().await()
        return snapshot.exists()
    }

    /**
     * Initializes users in the Firebase database. This method is intended to
     * pre-populate the database with user data.
     */
    fun initFirebaseUsers() {
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")

        CoroutineScope(Dispatchers.IO).launch {
            val users = listOf(
                UserFirebase(uid = 20001, username = "user1", name = "Robert", surname = "Kennedy", email = "rkennedy@mail.com", password = "user", gender = "Male", dateOfBirth = "22/08/1997", role = "user", registrationDate = "08/07/2023"),
                // Add more UserFirebase instances here as needed
            )

            users.forEach { user ->
                val key = usersRef.push().key  // Generate a unique key for each user
                key?.let {
                    usersRef.child(it).setValue(user).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("FirebaseRepository", "User uploaded successfully: ${user.username}")
                        } else {
                            Log.e("FirebaseRepository", "Failed to upload user: ${task.exception?.message}")
                        }
                    }
                }
            }
        }
    }
}





