package com.example.assignment3_recipeapp.room.user

import android.app.Application
import com.example.assignment3_recipeapp.model.user.UserDatabase
import com.example.assignment3_recipeapp.model.room.UserDAO
import kotlinx.coroutines.flow.Flow

/**
 * Repository for handling user data operations. It provides an abstraction layer between
 * the Room database access and the UI.
 *
 * @property application Instance of Application used to construct the database.
 */
class UserRepository(application: Application) {
    private var userDao: UserDAO = UserDatabase.getDatabase(application).userDAO()
    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    /**
     * Inserts a new user into the database.
     *
     * @param user The user to be inserted.
     */
    suspend fun insert(user: User) {
        userDao.insertUser(user)
    }

    /**
     * Updates an existing user in the database.
     *
     * @param user The user to be updated.
     */
    suspend fun update(user: User) {
        userDao.updateUser(user)
    }

    /**
     * Deletes a user from the database.
     *
     * @param user The user to be deleted.
     */
    suspend fun delete(user: User) {
        userDao.deleteUser(user)
    }

    /**
     * Validates if an admin user exists with the given email and password.
     *
     * @param email The email of the admin to be validated.
     * @param password The password of the admin to be validated.
     * @return True if an admin user exists, otherwise false.
     */
    suspend fun validateAdmin(email: String, password: String): Boolean {
        return userDao.validateAdmin(email, password) != null
    }

    /**
     * Validates if a user exists with the given email and password.
     *
     * @param email The email of the user to be validated.
     * @param password The password of the user to be validated.
     * @return True if a user exists, otherwise false.
     */
    suspend fun validateUser(email: String, password: String): Boolean {
        return userDao.validateUser(email, password) != null
    }

    /**
     * Retrieves a user by email.
     *
     * @param email The email of the user to be retrieved.
     * @return The user with the specified email or null if no such user exists.
     */
    fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }
}
