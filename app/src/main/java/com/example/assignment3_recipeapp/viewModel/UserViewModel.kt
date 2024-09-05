package com.example.assignment3_recipeapp.viewModel

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.assignment3_recipeapp.model.repository.UserRepository
import com.example.assignment3_recipeapp.room.user.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate


// ViewModel for user-related operations including authentication and data management.
class UserViewModel(application: Application) : AndroidViewModel(application) {
    // Repository for managing user data.
    private val cRepository: UserRepository

    // LiveData for observing changes in the current user.
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    // Initializes the UserRepository with the application context.
    init {
        cRepository = UserRepository(application)
    }

    // LiveData that exposes a list of all users.
    val allUsers: LiveData<List<User>> = cRepository.allUsers.asLiveData()

    /**
     * Inserts a user into the database asynchronously.
     * @param user the user to insert
     */
    fun insertUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.insert(user)
    }

    /**
     * Updates a user in the database asynchronously.
     * @param user the user to update
     */
    fun updateUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.update(user)
    }

    /**
     * Deletes a user from the database asynchronously.
     * @param user the user to delete
     */
    fun deleteUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.delete(user)
    }

    /**
     * Validates admin credentials asynchronously and returns the result as LiveData.
     * @param email the admin's email
     * @param password the admin's password
     * @return LiveData representing the validation result
     */
    fun validateAdmin(email: String, password: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        viewModelScope.launch {
            result.value = cRepository.validateAdmin(email, password)
        }
        return result
    }

    /**
     * Validates user credentials asynchronously and returns the result as LiveData.
     * @param email the user's email
     * @param password the user's password
     * @return LiveData representing the validation result
     */
    fun validateUser(email: String, password: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        viewModelScope.launch {
            result.value = cRepository.validateUser(email, password)
        }
        return result
    }

    /**
     * Sets the current user based on email asynchronously.
     * @param email the email of the user to set as current
     */
    fun setCurrentUserByEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = cRepository.getUserByEmail(email)
            user?.let {
                _currentUser.postValue(it)
            }
        }
    }

    /**
     * Configures and returns a GoogleSignInClient for authentication.
     * @param activity the activity context used for client configuration
     * @return the configured GoogleSignInClient
     */
    fun getGoogleSignInClient(activity: Activity): GoogleSignInClient {
        val clientID = "YOUR_CLIENT_ID"  // Direct use of a specific Google client ID
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientID)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(activity, gso)
    }

    // Handles the activity result from Google SignIn intent.
    @RequiresApi(Build.VERSION_CODES.O)
    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100) { // Check if the result comes from the correct request code
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    // Processes the result from Google SignIn and updates user data accordingly.
    @RequiresApi(Build.VERSION_CODES.O)
    fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            account?.let { acc ->
                viewModelScope.launch(Dispatchers.IO) {
                    val existingUser = acc.email?.let { cRepository.getUserByEmail(it) }
                    if (existingUser == null) {
                        val newUser = User(
                            email = acc.email ?: "",
                            username = acc.displayName ?: "New User",
                            password = "",
                            name = acc.givenName ?: "",
                            surname = acc.familyName ?: "",
                            gender = "",
                            dateOfBirth = "",
                            registrationDate = LocalDate.now().toString(),
                            profilePictureUrl = acc.photoUrl?.toString() ?: "",
                            role = "user"
                        )
                        cRepository.insert(newUser)
                        _currentUser.postValue(newUser)
                    } else {
                        _currentUser.postValue(existingUser)
                    }
                }
            }
        } catch (e: ApiException) {
            Log.e("LoginScreen", "Google sign in failed: ${e.statusCode}")
        }
    }
}
