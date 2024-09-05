package com.example.assignment3_recipeapp.page.admin

import com.example.assignment3_recipeapp.viewModel.UserFirebaseViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.assignment3_recipeapp.model.user.UserFirebase
import com.example.assignment3_recipeapp.ui.theme.Background

/**
 * Displays a management screen for admin to handle user details.
 *
 * @param navController The navigation controller for app navigation.
 * @param userViewModel ViewModel that provides user-related data and operations.
 */
@Composable
fun AdminManageUser(navController: NavHostController, userViewModel: UserFirebaseViewModel) {
    val allUsers: List<UserFirebase> by userViewModel.users.observeAsState(emptyList())

    // Fetch users when the Composable enters the Composition
    LaunchedEffect(key1 = true) {
        userViewModel.fetchAllUsers()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(color = Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(color = Background)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Manage Users",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn {
                items(allUsers) { user ->
                    UserItem(user = user, onDeleteClick = {
                        userViewModel.deleteUser(user.key)
                    })
                }
            }
        }
    }
}
/**
 * Renders a single user item with delete functionality.
 *
 * @param user The user data to display.
 * @param onDeleteClick Function to call when the delete button is clicked.
 */
@Composable
fun UserItem(user: UserFirebase, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Background)
            .border(1.dp, Color.Gray),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Background)
        ) {
            // Display user information in a column
            Text(text = "Username: ${user.username}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Name: ${user.name}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Surname: ${user.surname}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Email: ${user.email}")

            // Place the delete button on the right side
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onDeleteClick,
                ) {
                    Text(text = "Delete")
                }
            }
        }
    }
}