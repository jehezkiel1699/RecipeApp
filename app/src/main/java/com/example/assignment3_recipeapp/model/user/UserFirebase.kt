package com.example.assignment3_recipeapp.model.user

/**
 * Data class representing a user in Firebase.
 * This class is used for handling user information within the application, particularly for Firebase operations.
 *
 * @property uid Unique identifier for the user. Defaults to 0L if not set.
 * @property username User's chosen username. Defaults to an empty string if not set.
 * @property name User's first name. Defaults to an empty string if not set.
 * @property surname User's last name. Defaults to an empty string if not set.
 * @property email User's email address. Defaults to an empty string if not set.
 * @property gender User's gender. Defaults to an empty string if not set.
 * @property dateOfBirth User's date of birth in String format. Defaults to an empty string if not set.
 * @property password User's password for login purposes. Defaults to an empty string if not set.
 * @property role User's role within the application (e.g., user, admin). Defaults to an empty string if not set.
 * @property registrationDate The date on which the user registered. Nullable and defaults to null.
 * @property profilePictureUrl URL to the user's profile picture. Nullable and defaults to null.
 */
data class UserFirebase(
    val uid: Long = 0L,
    val username: String = "",
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val gender: String = "",
    val dateOfBirth: String = "",
    val password: String = "",
    val role: String = "",
    val registrationDate: String? = null,
    val profilePictureUrl: String? = null,
    val key: String = ""
)