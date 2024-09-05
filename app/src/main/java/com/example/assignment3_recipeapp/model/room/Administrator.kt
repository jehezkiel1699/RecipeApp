package com.example.assignment3_recipeapp.model.room

import android.content.Context
import androidx.room.Room
import com.example.assignment3_recipeapp.room.user.User
import com.example.assignment3_recipeapp.model.user.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


/**
 * Singleton object that handles initialization of database entities and retrieval.
 */
object Administrator {
    private var INSTANCE: UserDatabase? = null

    /**
     * Retrieves a singleton instance of the UserDatabase.
     *
     * @param context The context used to configure the database context.
     * @return A singleton instance of UserDatabase.
     */

    fun getDatabase(context: Context): UserDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                UserDatabase::class.java,
                "user_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }

    /**
     * Initializes default administrator users in the database if they do not exist.
     *
     * @param context The application context used for database operations.
     */

    fun initAdmin(context: Context) {
        val db = getDatabase(context)
        CoroutineScope(Dispatchers.IO).launch {
            val userDao = db.userDAO()
            val exists = userDao.findByUsername("admin1")
            if (exists == null) {
                val adminUser1 = User(username = "admin1", name = "AdminName", surname = "AdminSurname", email = "admin@t.com", password = "admin", gender = "Male", dateOfBirth = "29/09/1999", role = "admin",registrationDate = "01/02/2024", profilePictureUrl = "")
                userDao.insertUser(adminUser1)
            }
        }
    }

    /**
     * Populates the database with predefined user data for testing or initial deployment.
     *
     * @param context The application context used for database operations.
     */

    fun initUsers(context: Context) {
        val db = getDatabase(context)
        CoroutineScope(Dispatchers.IO).launch {
            val userDao = db.userDAO()

            val user1 = User(username = "user1", name = "Robert", surname = "Kennedy", email = "rkennedy@mail.com", password = "user", gender = "Male", dateOfBirth = "22/08/1997", role = "user", registrationDate = "08/07/2023", profilePictureUrl = "")
            val user2 = User(username = "user2", name = "Mia", surname = "Galagher", email = "mgalagher@mail.com", password = "user", gender = "Female", dateOfBirth = "03/05/2002", role = "user", registrationDate = "03/03/2023", profilePictureUrl = "")
            val user3 = User(username = "user3", name = "Josh", surname = "Remington", email = "jremington@mail.com", password = "user", gender = "Male", dateOfBirth = "04/07/1995", role = "user", registrationDate = "11/12/2022", profilePictureUrl = "")
            val user4 = User(username = "user4", name = "David", surname = "Scott", email = "dscott@mail.com", password = "user", gender = "Male", dateOfBirth = "04/08/1998", role = "user", registrationDate = "01/04/2024", profilePictureUrl = "")
            val user5 = User(username = "user5", name = "Michelle", surname = "Deborah", email = "mdeborah@mail.com", password = "user", gender = "Female", dateOfBirth = "16/09/1982", role = "user", registrationDate = "16/02/2024", profilePictureUrl = "")
            val user6 = User(username = "user6", name = "Dorothy", surname = "Evans", email = "devans@mail.com", password = "user", gender = "Female", dateOfBirth = "19/01/1974", role = "user", registrationDate = "13/03/2023", profilePictureUrl = "")
            val user7 = User(username = "user7", name = "James", surname = "Sanders", email = "jsanders@mail.com", password = "user", gender = "Male", dateOfBirth = "11/01/1970", role = "user", registrationDate = "02/02/2023", profilePictureUrl = "")
            val user8 = User(username = "user8", name = "Tobias", surname = "Isaac", email = "tisaac@mail.com", password = "user", gender = "Male", dateOfBirth = "27/03/1997", role = "user", registrationDate = "11/03/2023", profilePictureUrl = "")
            val user9 = User(username = "user9", name = "Eva", surname = "Gloria", email = "egloria@mail.com", password = "user", gender = "Female", dateOfBirth = "13/04/1990", role = "user", registrationDate = "04/01/2023", profilePictureUrl = "")
            val user10 = User(username = "user10", name = "Samuel", surname = "Mendez", email = "smendez@mail.com", password = "user", gender = "Male", dateOfBirth = "17/11/2004", role = "user", registrationDate = "03/03/2023", profilePictureUrl = "")
            userDao.insertUser(user1)
            userDao.insertUser(user2)
            userDao.insertUser(user3)
            userDao.insertUser(user4)
            userDao.insertUser(user5)
            userDao.insertUser(user6)
            userDao.insertUser(user7)
            userDao.insertUser(user8)
            userDao.insertUser(user9)
            userDao.insertUser(user10)

        }
    }

    /**
     * Retrieves all users from the database.
     *
     * @param context The application context.
     * @return A list of User instances from the database.
     */
    fun getUsersFromDatabase(context: Context): List<User> {
        val db = getDatabase(context)
        return runBlocking {
            val userDao = db.userDAO()
            userDao.getAllUsers().first()
        }
    }
}