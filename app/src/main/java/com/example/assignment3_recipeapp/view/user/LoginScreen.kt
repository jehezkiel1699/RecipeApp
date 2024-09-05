package com.example.assignment3_recipeapp.page.user

import com.example.assignment3_recipeapp.viewModel.UserFirebaseViewModel
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.assignment3_recipeapp.R
import com.example.assignment3_recipeapp.model.user.UserFirebase
import com.example.assignment3_recipeapp.room.user.UserViewModel
import com.example.assignment3_recipeapp.ui.theme.Background
import com.example.assignment3_recipeapp.ui.theme.Primary
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Composable function representing the login screen.
 *
 * @param navController The navigation controller.
 * @param userViewModel The user ViewModel.
 * @param userFirebaseViewModel The user Firebase ViewModel.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(navController: NavController, userViewModel: UserViewModel = viewModel(), userFirebaseViewModel: UserFirebaseViewModel) {
    val context = LocalContext.current
    val activity = context as? Activity
    // State variables for email, password, and error dialog
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var attemptLogin by remember { mutableStateOf(false) }

    // Observing login results from ViewModel
    val loginResultUser by userViewModel.validateUser(email, password).observeAsState()
    val loginResultAdmin by userViewModel.validateAdmin(email, password).observeAsState()

// Launched effect to handle login attempt
    LaunchedEffect(attemptLogin) {
        if (attemptLogin) {
            // Reset flag
            attemptLogin = false

            val database = FirebaseDatabase.getInstance()
            val usersRef = database.getReference("users")
            val query = usersRef.orderByChild("email").equalTo(email)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var loginSuccessful = false
                    dataSnapshot.children.forEach { snapshot ->
                        val user = snapshot.getValue(UserFirebase::class.java)
                        if (user != null && user.password == password) {
                            userFirebaseViewModel.setCurrentUser(user)  // Set the current user in com.example.assignment3_recipeapp.viewModel.UserFirebaseViewModel
                            val navRoute = if (user.role == "admin") "adminHome" else "userHome"
                            navController.navigate(navRoute)
                            loginSuccessful = true
                            return@forEach  // Exit the loop on successful login
                        }
                    }
                    if (!loginSuccessful) {
                        showErrorDialog = true
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    showErrorDialog = true
                }
            })
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(color = Background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
//        Text("My Recipe", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(bottom = 32.dp))
            Image(
                painter = painterResource(id = R.drawable.perfectcookwithspatula), // Replace R.drawable.your_image with the resource ID of your PNG image
                contentDescription = "Title Image", // Provide a content description for accessibility
                modifier = Modifier
                    .size(360.dp) // Adjust the size of the image as needed
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .offset(x = 18.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Lock Icon") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { attemptLogin = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Primary)
            ) {
                Text(text ="Login",
                    style= TextStyle(color = Color.Black,
                        fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate("registrationForm") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Primary)
            ) {
                Text(text ="Register",
                    style= TextStyle(color = Color.Black,
                        fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))


            val signInRequestLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    userViewModel.handleSignInResult(task)
                }
            }
            Button(
                onClick = {
                    if (activity != null) {
                        handleGoogleLogin(
                            navController,
                            userViewModel,
                            activity,
                            signInRequestLauncher
                        )
                    } else {
                        Log.e("LoginScreen", "Activity context is not available.")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Primary)
            ) {
                Text(text ="Login with Google",
                    style= TextStyle(color = Color.Black,
                        fontWeight = FontWeight.Bold)
                )
            }

            if (showErrorDialog) {
                AlertDialog(
                    onDismissRequest = { showErrorDialog = false },
                    title = { Text("Login Failed") },
                    text = { Text("Invalid email or password.") },
                    confirmButton = {
                        Button(onClick = { showErrorDialog = false }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}
/**
 * Function to handle Google login.
 *
 * @param navController The navigation controller.
 * @param userViewModel The user ViewModel.
 * @param activity The activity.
 * @param launcher The launcher.
 */
fun handleGoogleLogin(navController: NavController, userViewModel: UserViewModel, activity: Activity, launcher: ActivityResultLauncher<Intent>) {
    val signInIntent = userViewModel.getGoogleSignInClient(activity).signInIntent
    launcher.launch(signInIntent)
}

