package com.example.assignment3_recipeapp.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update
import com.example.assignment3_recipeapp.room.user.User
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing user data in the database.
 */
@Dao
interface UserDAO {
    /**
     * Retrieves all users from the database.
     *
     * @return A flowable list of all users for reactive updates.
     */
    @Query("SELECT * FROM User")
    fun getAllUsers(): Flow<List<User>>

    /**
     * Validates if a user with the specified email and password exists and has the user role.
     *
     * @param email The email of the user.
     * @param password The password of the user.
     * @return A User object if found, null otherwise.
     */
    @Query("SELECT * FROM User WHERE email = :email AND password = :password AND role = 'user'")
    suspend fun validateUser(email: String, password: String): User?

    /**
     * Validates if an admin with the specified email and password exists.
     *
     * @param email The admin's email.
     * @param password The admin's password.
     * @return A User object if found, null otherwise.
     */
    @Query("SELECT * FROM User WHERE email = :email AND password = :password AND role = 'admin'")
    suspend fun validateAdmin(email: String, password: String): User?

    /**
     * Finds a user by username.
     *
     * @param username The username of the user to find.
     * @return A User object if found, null otherwise.
     */
    @Query("SELECT * FROM User WHERE username = :username LIMIT 1")
    suspend fun findByUsername(username: String): User?

    /**
     * Retrieves a user by their email.
     *
     * @param email The email of the user to retrieve.
     * @return A User object if found, null otherwise.
     */
    @Query("SELECT * FROM User WHERE email = :email LIMIT 1")
    fun getUserByEmail(email: String): User?

    /**
     * Inserts a user into the database.
     *
     * @param user The User object to insert.
     */
    @Insert
    suspend fun insertUser(user: User)

    /**
     * Updates a user's information in the database.
     *
     * @param user The User object to update.
     */
    @Update
    suspend fun updateUser(user: User)

    /**
     * Deletes a user from the database.
     *
     * @param user The User object to delete.
     */
    @Delete
    suspend fun deleteUser(user: User)
}
