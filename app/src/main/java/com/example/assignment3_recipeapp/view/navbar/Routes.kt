package com.example.assignment3_recipeapp.view.navbar

/**
 * Enum class representing different routes in the app.
 * Each route has a corresponding string value.
 */
enum class Routes(val value: String) {
    UserHome("Home"),          // Route for the user's home screen
    RecipeDetail("RecipeDetail"),  // Route for the recipe detail screen
    UserList("MyList"),        // Route for the user's list screen
    UserProfile("MyProfile"),   // Route for the user's profile screen
    LoginScreen("LoginScreen"), // Route for the login screen
    RegistrationForm("RegistrationForm") // Route for the registration form screen
    // RegistrationSuccessScreen("RegistrationSuccessScreen") // Route for the registration success screen (commented out)
}