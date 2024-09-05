package com.example.assignment3_recipeapp.page.user


import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.assignment3_recipeapp.room.user.User
import com.example.assignment3_recipeapp.room.user.UserViewModel
import com.example.assignment3_recipeapp.ui.theme.Assignment3_RecipeAppTheme
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.example.assignment3_recipeapp.ui.theme.Primary
import com.example.assignment3_recipeapp.ui.theme.Background
import com.example.assignment3_recipeapp.view.Validation
import com.example.assignment3_recipeapp.viewModel.UserFirebaseViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.database
import java.time.LocalDate
import java.time.format.DateTimeFormatter
/**
 * Composable function for displaying the registration success screen.
 *
 * @param navController The navigation controller.
 */
@Composable
fun RegistrationSuccessScreen(navController: NavHostController) {
    Column(modifier = Modifier.background(color = Background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(color = Background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Registration Successful!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = {navController.navigate("loginScreen")},
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(Primary)
        ) {
            Text(text = "OK", color = Color.Black)
        }
    }

}
/**
 * Composable function for displaying an error dialog.
 *
 * @param errorText The error message to display.
 * @param onDismiss Callback for dismissing the dialog.
 */
@Composable
fun ErrorDialog(errorText: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Error") },
        text = { Text(errorText) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

/**
 * Function to save user data to Firebase.
 *
 * @param user The user object to save.
 * @param navController The navigation controller.
 */
fun saveUserDataToFirebase(user: User, navController: NavHostController) {
    val database = Firebase.database
    val usersRef = database.getReference("users")
    val userId = usersRef.push().key
    Log.i("database:", database.toString())
    Log.i("userref:", usersRef.toString())
    Log.i("userid:", userId.toString())

    userId?.let {
        usersRef.child(it).setValue(user)
            .addOnSuccessListener {
                navController.navigate("registerSuccessScreen")
                Log.i("tess", user.toString())
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", e.toString())
            }
    }
}

/**
 * Composable function for the registration form.
 *
 * @param navController The navigation controller.
 * @param userViewModel The user ViewModel.
 * @param userFirebaseViewModel The user Firebase ViewModel.
 */
@RequiresApi(64)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationForm(navController: NavHostController, userViewModel: UserViewModel = viewModel(), userFirebaseViewModel: UserFirebaseViewModel) {
    var username by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val genders = listOf("Male", "Female", "Others")
    var isExpanded by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf(genders[0]) }
    val calendar = Calendar.getInstance()
    calendar.set(2024, 0, 1)
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    var selectedDate by remember {
        mutableStateOf(calendar.timeInMillis)
    }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }


    Surface(color = Background){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = Background),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Row(horizontalArrangement = Arrangement.Start, // Align content to the start (left)
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(color = Background)
                    .fillMaxWidth()) {
                IconButton(onClick = {navController.navigateUp()}) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go Back")
                }
                Text("Back", modifier = Modifier.padding(start = 2.dp))
            }

            Text(
                text = "Registration Form",
                style = MaterialTheme.typography.headlineMedium,
            )
            OutlinedTextField(
                value = username,
                onValueChange = { username = it }, label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it }, label = { Text("First name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = surname,
                onValueChange = { surname = it },
                label = { Text("Last name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )

            Box(
                modifier = Modifier.wrapContentSize()
                // Set background color here
            ) {
                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = it }) {
                    TextField(
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .focusProperties {
                                canFocus = false
                            }
                            .padding(bottom = 8.dp),
                        readOnly = true,
                        value = selectedGender,
                        onValueChange = {},
                        label = { Text("Gender") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                        }
                    )
                    ExposedDropdownMenu(expanded = isExpanded,
                        modifier = Modifier.background(color = Background),
                        onDismissRequest = { isExpanded = false }) {
                        genders.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                modifier = Modifier.background(Primary),
                                onClick = {
                                    selectedGender = selectionOption
                                    isExpanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
            }
            Text(
                text = "",
                style = MaterialTheme.typography.bodyMedium
            )
            if (showDatePicker) {
                DatePickerDialog(onDismissRequest = { showDatePicker = false },
                    modifier = Modifier,
                    confirmButton = {
                        Row(){
                            TextButton(onClick = {
                                showDatePicker = false
                                selectedDate = datePickerState.selectedDateMillis!!
                            })
                            {
                                Text(text = "Select")
                            }}
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text(text = "Cancel")
                        }
                    }) {
                    DatePicker(state = datePickerState)
                }
            }

            Button(
                onClick = { showDatePicker = true },
                colors = ButtonDefaults.buttonColors(Primary)
            ) {
                Text(
                    text = "Enter Date of Birth",
                    color = Color.Black
                )
            }
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
            Text(
                text = "Date of Birth: ${formatter.format(Date(selectedDate))}",
                color = Color.Black
            )

            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val newUser = User(
                        username = username,
                        name = name,
                        surname = surname,
                        email = email,
                        password = password,
                        gender = selectedGender,
                        dateOfBirth = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT).format(Date(selectedDate)),
                        role = "user",
                        registrationDate = getCurrentDate(),
                        profilePictureUrl = ""
                    )
                    Button(
                        onClick = {
                            if (!Validation.areFieldsFilled(
                                    username,
                                    name,
                                    surname,
                                    email,
                                    password,
                                    confirmPassword
                                )
                            ) {
                                errorText = "All fields must be filled."
                                showErrorDialog = true
                            } else if (!Validation.isUsernameValid(username)) {
                                errorText = "Username must be less than 25 characters."
                                showErrorDialog = true
                            } else if (!Validation.isEmailValid(email)) {
                                errorText = "Invalid email format."
                                showErrorDialog = true
                            } else if (!Validation.isPasswordValid(password)) {
                                errorText = "Password must be 8-16 characters long with at least one uppercase letter."
                                showErrorDialog = true
                            } else if (password != confirmPassword) {
                                errorText = "Passwords do not match."
                                showErrorDialog = true

                                    } else {
                                        userFirebaseViewModel.emailExists(email) { exists ->
                                            if (exists) {
                                                errorText = "Email already in use. Please use a different email."
                                                showErrorDialog = true
                                            } else {
                                        userViewModel.insertUser(newUser)
                                        saveUserDataToFirebase(newUser, navController)
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(Primary)
                    ) {
                        Text(
                            text = "Register",
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
    if (showErrorDialog) {
        ErrorDialog(
            errorText = errorText,
            onDismiss = { showErrorDialog = false }
        )
    }
}
/**
 * Function to get the current date.
 *
 * @return The current date in "dd/MM/yyyy" format.
 */
fun getCurrentDate(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return currentDate.format(formatter)
}


