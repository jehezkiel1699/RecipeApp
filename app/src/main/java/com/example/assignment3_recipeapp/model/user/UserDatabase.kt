package com.example.assignment3_recipeapp.model.user

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.assignment3_recipeapp.model.room.UserDAO
import com.example.assignment3_recipeapp.room.user.User

/**
 * Singleton class for Room database access.
 * It holds the database instance and serves as the main access point for the underlying connection to the app's persisted data.
 */
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDAO

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        /**
         * Retrieves the singleton instance of UserDatabase.
         * Uses a synchronized block to ensure that the database is a singleton to prevent having multiple instances.
         *
         * @param context The application context used to configure the database context.
         * @return The single instance of UserDatabase.
         */
        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database" // Database name
                )
                    .fallbackToDestructiveMigration() // Strategy for handling migrations if no migrations were provided.
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}


