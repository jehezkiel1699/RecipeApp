package com.example.assignment3_recipeapp.model.repository

import android.app.Application
import com.example.assignment3_recipeapp.model.room.UserDAO
import com.example.assignment3_recipeapp.model.user.UserDatabase
import com.example.assignment3_recipeapp.room.user.User
import kotlinx.coroutines.flow.Flow

/**
 * Repository for handling CRUD operations on user data via Room database.
 */
class UserRepository(application: Application) {
    // Obtains an instance of UserDAO from the singleton UserDatabase.
    private var userDao: UserDAO = UserDatabase.getDatabase(application).userDAO()

    // Stream of all users, observed as a flow.
    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    /**
     * Inserts a user into the database.
     *
     * @param user The user to insert.
     */
    suspend fun insert(user: User) {
        userDao.insertUser(user)
    }

    /**
     * Updates a user in the database.
     *
     * @param user The user to update.
     */
    suspend fun update(user: User) {
        userDao.updateUser(user)
    }

    /**
     * Deletes a user from the database.
     *
     * @param user The user to delete.
     */
    suspend fun delete(user: User) {
        userDao.deleteUser(user)
    }

    /**
     * Validates if an admin user exists with the specified email and password.
     *
     * @param email The email of the admin.
     * @param password The password of the admin.
     * @return True if an admin exists, false otherwise.
     */
    suspend fun validateAdmin(email: String, password: String): Boolean {
        return userDao.validateAdmin(email, password) != null
    }

    /**
     * Validates if a user exists with the specified email and password.
     *
     * @param email The email of the user.
     * @param password The password of the user.
     * @return True if a user exists, false otherwise.
     */
    suspend fun validateUser(email: String, password: String): Boolean {
        return userDao.validateUser(email, password) != null
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email The email of the user.
     * @return The user, or null if not found.
     */
    fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }
}
