package com.example.assignment3_recipeapp.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.assignment3_recipeapp.model.recipe.Recipe
import com.example.assignment3_recipeapp.model.user.UserFirebase
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// ViewModel for managing Firebase user data interactions.
class UserFirebaseViewModel : ViewModel() {
    // Reference to the Firebase database "users" node.
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")

    // MutableLiveData to track the list of users.
    private val _users = MutableLiveData<List<UserFirebase>>()

    // LiveData to expose the list of users.
    val users: LiveData<List<UserFirebase>> = _users

    // MutableLiveData to keep track of the current user.
    private val _currentUser = MutableLiveData<UserFirebase>()

    // LiveData to expose the current user.
    val currentUser: LiveData<UserFirebase> = _currentUser

    /**
     * Fetches all users from the Firebase database and updates LiveData.
     */
    fun fetchAllUsers() {
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<UserFirebase>()
                snapshot.children.forEach { child ->
                    val key = child.key // Retrieve the unique ID (key)
                    val user = child.getValue(UserFirebase::class.java)?.copy(key = key ?: "")

                    user?.let { userList.add(it) }
                }
                _users.postValue(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to fetch users", error.toException())
            }
        })
    }

    /**
     * Extracts the year from a date string in the format "DD/MM/YYYY".
     * @param date the date string
     * @return the year as a string or null if the format is incorrect
     */
    fun extractYearFromDate(date: String?): String? {
        // Expected format "DD/MM/YYYY"
        return date?.takeIf { it.length == 10 }?.substring(6)
    }

    /**
     * Sets the current user and updates LiveData.
     * @param user the UserFirebase object to set as current
     */
    fun setCurrentUser(user: UserFirebase) {
        _currentUser.value = user
    }

    /**
     * Checks if an email exists in the database.
     * @param email the email to check
     * @param onResult callback function to handle the result
     */
    fun emailExists(email: String, onResult: (Boolean) -> Unit) {
        dbRef.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    onResult(dataSnapshot.exists())  // Email exists if dataSnapshot.exists() returns true
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    onResult(false)  // Assume email does not exist on cancellation or error
                }
            })
    }

    /**
     * Inserts a user into the Firebase database.
     * @param user the UserFirebase object to insert
     */
    fun insertUser(user: UserFirebase) {
        // Your existing code to add user to the database
        dbRef.child(user.email.replace(".", ","))
            .setValue(user)  // Using email as key, replacing '.' with ',' since Firebase keys can't contain '.'
    }

    /**
     * Updates user profile details in the Firebase database.
     * @param user the user to update
     * @param photoUrl the new photo URL (optional)
     * @param onUpdateComplete callback function to handle the result
     */
    fun updateUserProfile(
        user: UserFirebase,
        photoUrl: String? = null,
        onUpdateComplete: (Boolean) -> Unit
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("users/${user.uid}")
        val updates = mapOf(
            "username" to user.username,
            "name" to user.name,
            "surname" to user.surname,
            "email" to user.email,
            "gender" to user.gender,
            "dateOfBirth" to user.dateOfBirth,
            "role" to user.role,
            "registrationDate" to user.registrationDate,
            "profilePictureUrl" to (photoUrl ?: user.profilePictureUrl)
        )

        dbRef.updateChildren(updates)
            .addOnSuccessListener {
                onUpdateComplete(true)
            }
            .addOnFailureListener {
                onUpdateComplete(false)
            }
    }

    fun deleteUser(key: String) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users").child(key.toString())
        userRef.removeValue()
    }

    /**
     * Uploads a photo to Firebase storage and retrieves the URL.
     * @param photoUri the URI of the photo to upload
     * @param onSuccess callback to handle success with the URL string
     * @param onError callback to handle errors
     */
    fun uploadPhoto(photoUri: Uri, onSuccess: (String) -> Unit, onError: (Exception) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val photoRef = storageRef.child("profilePictures/${System.currentTimeMillis()}")

        photoRef.putFile(photoUri)
            .addOnSuccessListener {
                photoRef.downloadUrl.addOnSuccessListener { uri ->
                    onSuccess(uri.toString())
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun initFirebaseAdmin() {
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")

        CoroutineScope(Dispatchers.IO).launch {
            val users = listOf(
                UserFirebase(
                    uid = 20001,
                    username = "admin",
                    name = "admin",
                    surname = "admin",
                    email = "admin@gmail.com",
                    password = "admin",
                    gender = "Male",
                    dateOfBirth = "22/08/1997",
                    role = "admin",
                    registrationDate = "08/07/2023"
                ),
                // Add more UserFirebase instances here as needed
            )

            users.forEach { user ->
                val key = usersRef.push().key  // Generate a unique key for each user
                key?.let {
                    usersRef.child(it).setValue(user).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(
                                "FirebaseRepository",
                                "User uploaded successfully: ${user.username}"
                            )
                        } else {
                            Log.e(
                                "FirebaseRepository",
                                "Failed to upload user: ${task.exception?.message}"
                            )
                        }
                    }
                }
            }
        }
    }
}
