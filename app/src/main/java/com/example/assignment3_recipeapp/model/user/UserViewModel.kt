package com.example.assignment3_recipeapp.room.user

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
import com.example.assignment3_recipeapp.model.repository.FirebaseRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * ViewModel for managing user data and authentication with Google SignIn.
 *
 * @param application The application that this ViewModel is associated with.
 */
class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val cRepository: UserRepository
    private val _currentUser = MutableLiveData<User?>()
    private val firebaseRepository = FirebaseRepository()
    val currentUser: LiveData<User?> = _currentUser
    init{
        cRepository = UserRepository(application)
    }

    val allUsers: LiveData<List<User>> = cRepository.allUsers.asLiveData()

    /**
     * Inserts a user into the repository.
     */
    fun insertUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.insert(user)
    }

    /**
     * Updates a user in the repository.
     */
    fun updateUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.update(user)
    }

    fun deleteUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.delete(user)
    }
    /**
     * Validates if a user is an admin.
     */
    fun validateAdmin(email: String, password: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        viewModelScope.launch {
            result.value = cRepository.validateAdmin(email, password)
        }
        return result
    }
    /**
     * Sets the current user based on the provided email.
     */
    fun validateUser(email: String, password: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        viewModelScope.launch {
            result.value = cRepository.validateUser(email, password)
        }
        return result
    }

    fun setCurrentUserByEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = cRepository.getUserByEmail(email)
            if (user != null) {
                _currentUser.postValue(user)
            }
        }
    }

    /**
     * Returns a GoogleSignInClient configured with client ID and request options.
     */

    fun getGoogleSignInClient(activity: Activity): GoogleSignInClient {
        val clientID = "633327106563-657j4hjtlckb95c3v6p8ie4ia9t680e6.apps.googleusercontent.com"  // 直接使用
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientID)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(activity, gso)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    /**
     * Processes the Google SignIn result, updating the local user repository as needed.
     */
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
                        viewModelScope.launch(Dispatchers.Main) {


                        }
                    } else {

                        _currentUser.postValue(existingUser)
                        viewModelScope.launch(Dispatchers.Main) {

                        }
                    }
                }
            }
        } catch (e: ApiException) {
            Log.e("LoginScreen", "Google sign in failed: ${e.statusCode}")
        }
    }
}