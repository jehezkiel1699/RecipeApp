package com.example.assignment3_recipeapp.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.assignment3_recipeapp.ui.theme.Primary
import coil.compose.AsyncImage

/**
 * A composable function that displays a recipe in a grid layout.
 * This is a wrapper that configures a RecipeCard with provided properties.
 *
 * @param title The title of the recipe.
 * @param imageUrl URL for the recipe image.
 * @param contentDescription A description of the image for accessibility.
 * @param onClick Function to execute when the card is clicked.
 */
@Composable
fun RecipeGrid(
    title: String,
    imageUrl: String,
    contentDescription: String,


    onClick: () -> Unit

){
    RecipeCard(
        title = title,
        imageUrl = imageUrl,
        contentDescription = contentDescription,
        onClick = onClick,
    )
}
/**
 * A composable function that defines the UI and behavior of a single recipe card.
 * This card displays an image and a title, and it is clickable.
 *
 * @param title The title of the recipe.
 * @param imageUrl URL for the recipe image.
 * @param contentDescription A description of the image for accessibility.
 * @param onClick Function to execute when the card is clicked.
 */


@Composable
fun RecipeCard(
    title: String,
    imageUrl: String,
    contentDescription: String,
    onClick: () -> Unit

){
    Card(
        elevation = 16.dp,
        modifier = Modifier
            .padding(0.dp, 8.dp)
            .clickable(onClick = onClick),
        backgroundColor = Primary
    ) {
        Box(
            modifier = Modifier
                .requiredSize(180.dp, 180.dp)
                .padding(8.dp)

        ){

            AsyncImage(
                model = imageUrl,
                contentDescription = contentDescription,
                modifier = Modifier
                    .aspectRatio(1f),
                contentScale = ContentScale.Fit
            )


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Primary),

            ){
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier

                )

            }



        }
    }
}