package com.example.assignment3_recipeapp.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


/**
 * A composable function that provides a user interface for inputting search queries.
 *
 * @param search The current text of the search field.
 * @param modifier A Modifier to be applied to the Column that houses the TextField.
 * @param onValueChange A callback to be invoked when the text in the TextField changes.
 */
@Composable
fun Search(
    search: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    // Column to hold the TextField, with applied modifiers for layout management.
    Column (
        modifier = modifier
            .fillMaxWidth()  // Ensures the column and its children occupy full width available
        //.clip(CircleShape)  // Commented out: Potential style option to clip to a circle shape
    ) {
        // TextField for entering search queries.
        TextField(
            value = search,  // Current search text
            onValueChange = onValueChange,  // Notify when text changes
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon") },  // Leading icon for aesthetics
            placeholder = { Text(text = "Search for recipes") },  // Placeholder text when TextField is empty
            modifier = Modifier
                .fillMaxWidth()  // Expand TextField to fill the width of its parent container
        )
    }
}

