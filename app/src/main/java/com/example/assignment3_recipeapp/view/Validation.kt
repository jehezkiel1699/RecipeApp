package com.example.assignment3_recipeapp.view

// Object for input validation methods
object Validation {
    /**
     * Checks if the provided email address is valid.
     * @param email the email address to validate
     * @return true if the email is valid, false otherwise
     */
    fun isEmailValid(email: String): Boolean {
        val trimmedEmail = email.trim()
        return trimmedEmail.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()
    }
    /**
     * Validates if the provided password meets the required criteria.
     * @param password the password to validate
     * @return true if the password is valid, false otherwise
     */
    fun isPasswordValid(password: String): Boolean {
        val trimmedPassword = password.trim()
        val passwordPattern = "^(?=.*[A-Z]).{8,16}$"
        return trimmedPassword.isNotEmpty() && trimmedPassword.matches(passwordPattern.toRegex())
    }
    /**
     * Checks if the provided username is valid.
     * @param username the username to validate
     * @return true if the username is valid, false otherwise
     */
    fun isUsernameValid(username: String): Boolean {
        val trimmedUsername = username.trim()
        return trimmedUsername.isNotEmpty() && trimmedUsername.length <= 25
    }
    /**
     * Validates that all given fields are filled.
     * @param fields a vararg array of strings representing the fields to check
     * @return true if all fields are filled, false otherwise
     */
    fun areFieldsFilled(vararg fields: String): Boolean {
        return fields.all { it.trim().isNotEmpty() }
    }
}