package com.example.assignment3_recipeapp.page.user

import android.app.Activity
import com.example.assignment3_recipeapp.viewModel.UserFirebaseViewModel
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.assignment3_recipeapp.R
import com.example.assignment3_recipeapp.model.user.UserFirebase
import com.example.assignment3_recipeapp.ui.theme.Background
import com.example.assignment3_recipeapp.ui.theme.Primary
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


/**
 * Composable function for displaying the user's profile.
 *
 *
 * @param userFirebaseViewModel The user Firebase ViewModel for managing user data.
 */
@Composable
fun UserProfile(userFirebaseViewModel: UserFirebaseViewModel) {

    val context = LocalContext.current
    val currentUser by userFirebaseViewModel.currentUser.observeAsState()
    val resultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            imageUri?.let { uri ->
                userFirebaseViewModel.uploadPhoto(uri, onSuccess = { photoUrl ->
                    currentUser?.let {
                        val updatedUser = it.copy(profilePictureUrl = photoUrl)
                        userFirebaseViewModel.updateUserProfile(updatedUser, photoUrl) { success ->
                            if (success) {
                                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }, onError = { exception ->
                    Toast.makeText(context, "Upload failed: ${exception.message}", Toast.LENGTH_LONG).show()
                })
            }
        }
    }

    // Initialize email state with the current user's email
    val emailState = remember { mutableStateOf(currentUser?.email ?: "") }

    // Use states for other user details
    val uidState = remember { mutableStateOf("") }
    val usernameState = remember { mutableStateOf("") }
    val nameState = remember { mutableStateOf("") }
    val surnameState = remember { mutableStateOf("") }
    val genderState = remember { mutableStateOf("") }
    val dateOfBirthState = remember { mutableStateOf("") }
    val roleState = remember { mutableStateOf("") }
    val registrationDateState = remember { mutableStateOf("") }
    val profilePictureUrlState = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }
    val isEditMode = remember { mutableStateOf(false) }

    // Effect to automatically load details when the screen is shown or the email changes
    LaunchedEffect(currentUser?.email) {
        emailState.value = currentUser?.email ?: ""
        if (emailState.value.isNotBlank()) {
            fetchUserByEmail(
                email = emailState.value,
                context = context,
                isLoading = isLoading,
                uidState = uidState,
                usernameState = usernameState,
                nameState = nameState,
                surnameState = surnameState,
                emailState = emailState,
                genderState = genderState,
                dateOfBirthState = dateOfBirthState,
                roleState = roleState,
                registrationDateState = registrationDateState,
                profilePictureUrlState = profilePictureUrlState
            )
        }
    }

    Box(modifier = Modifier.background(color = Background)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = Background)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),

                    ) {
                    if (isEditMode.value) {
                        Image(
                            painter = painterResource(id = R.drawable.avatarchef),
                            contentDescription = "Photo Profile",
                            modifier = Modifier
                                .size(100.dp)  // Set the size to 100dp x 100dp
                                .clip(CircleShape)  // Optional: clip as a circle for rounded profile pictures
                                .border(2.dp, Color.Gray, CircleShape), // Optional: add a border
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(32.dp))
//                        Text("Unique ID: ${uidState.value}", style = typography.bodyLarge)
//                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = usernameState.value,
                            onValueChange = { usernameState.value = it },
                            label = { Text("Name") }
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = nameState.value,
                            onValueChange = { nameState.value = it },
                            label = { Text("Name") }
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = surnameState.value,
                            onValueChange = { surnameState.value = it },
                            label = { Text("Surname") }
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Email: ${emailState.value}", style = typography.bodyLarge)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Gender: ${genderState.value}", style = typography.bodyLarge)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Date of Birth: ${dateOfBirthState.value}",
                            style = typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Role: ${roleState.value}", style = typography.bodyLarge)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Registration Date: ${registrationDateState.value}",
                            style = typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Button(
                            onClick = {
                                // Update user details in Firebase
                                val updatedUser = currentUser?.copy(
                                    name = nameState.value,
                                    surname = surnameState.value,
                                    username = usernameState.value
                                )
                                updatedUser?.let {
                                    userFirebaseViewModel.updateUserProfile(it) { success ->
                                        if (success) {
                                            Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                                isEditMode.value = false },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Primary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Save Changes")
                        }
                        Button(
                            onClick = {val photoPickerIntent = Intent(Intent.ACTION_PICK)
                                photoPickerIntent.type = "image/*"
                                resultLauncher.launch(photoPickerIntent)
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Primary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Upload Photo")
                        }

                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.avatarchef),
                            contentDescription = "Photo Profile",
                            modifier = Modifier
                                .size(200.dp)  // Set the size to 100dp x 100dp
                                .clip(CircleShape)  // Optional: clip as a circle for rounded profile pictures
                                .border(2.dp, Color.Gray, CircleShape), // Optional: add a border
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(32.dp))
//                        Text("Unique ID: ${uidState.value}", style = typography.bodyLarge)
//                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Username: ${usernameState.value}", style = typography.bodyLarge)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Name: ${nameState.value}", style = typography.bodyLarge)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Surname: ${surnameState.value}", style = typography.bodyLarge)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Email: ${emailState.value}", style = typography.bodyLarge)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Gender: ${genderState.value}", style = typography.bodyLarge)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Date of Birth: ${dateOfBirthState.value}",
                            style = typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Role: ${roleState.value}", style = typography.bodyLarge)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Registration Date: ${registrationDateState.value}",
                            style = typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { isEditMode.value = true },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Primary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Edit Profile")
                        }
                    }
                }
            }
        }
    }
}

fun fetchUserByEmail(
    email: String,
    context: Context,
    isLoading: MutableState<Boolean>,
    uidState: MutableState<String>,
    usernameState: MutableState<String>,
    nameState: MutableState<String>,
    surnameState: MutableState<String>,
    emailState: MutableState<String>,
    genderState: MutableState<String>,
    dateOfBirthState: MutableState<String>,
    roleState: MutableState<String>,
    registrationDateState: MutableState<String>,
    profilePictureUrlState: MutableState<String>
) {
    isLoading.value = true  // Set loading to true when the fetch starts
    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("users")
    val query = usersRef.orderByChild("email").equalTo(email)
    query.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            isLoading.value = false  // Set loading to false when data is received
            if (dataSnapshot.exists()) {
                dataSnapshot.children.forEach { snapshot ->
                    val user = snapshot.getValue(UserFirebase::class.java)
                    if (user != null) {
                        uidState.value = user.uid.toString()
                        usernameState.value = user.username
                        nameState.value = user.name
                        surnameState.value = user.surname
                        emailState.value = user.email
                        genderState.value = user.gender
                        dateOfBirthState.value = user.dateOfBirth
                        roleState.value = user.role
                        registrationDateState.value = user.registrationDate ?: ""
                        profilePictureUrlState.value = user.profilePictureUrl ?: ""
                    } else {
                        Toast.makeText(context, "Error parsing user data", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "No user found with email $email", Toast.LENGTH_LONG).show()
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            isLoading.value = false  // Set loading to false on error
            Toast.makeText(context, "Database error: ${databaseError.message}", Toast.LENGTH_LONG).show()
        }
    })
}


