package com.javix.wordflipster

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun CategoryGridScreen(categories: List<Category>, navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category ->
            CategoryItem(category, navController)
        }
    }
}

@Composable
fun CategoryItem(category: Category, navController: NavController) {
    // Scale animation for interactive feedback
    val scale = remember { Animatable(1f) }

    LaunchedEffect(scale) {
        scale.animateTo(
            targetValue = 1.05f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .scale(scale.value)
            .clickable(
                onClick = {
                    navController.navigate("mainScreen?category=${category.title}")
                    // Handle click action here
                },
                onClickLabel = "Open ${category.title} category"
            )
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color.White,
        elevation = 10.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.5f),
                            Color.White.copy(alpha = 0.3f)
                        ),
                        radius = 350f
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = category.imageRes),
                    contentDescription = category.title,
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                        .padding(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 18.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

data class Category(
    val title: String,
    val imageRes: Int // Resource ID for the category image
)



@Composable
fun PreviewCategoryGridScreen(navController: NavController) {
    val categories = listOf(
        Category("Food", R.drawable.food),
        Category("Drinks", R.drawable.drinks),
        Category("Fruits", R.drawable.fruits),
        Category("Names", R.drawable.name),
        Category("Animals", R.drawable.animals),
        Category("Colors", R.drawable.colors),
        Category("Countries", R.drawable.countries),
        Category("Sports", R.drawable.sports),
        Category("Seasons", R.drawable.seasons),
        Category("Occupations", R.drawable.occupations)
//        Category("Animals", R.drawable.food),
//        Category("Travel", R.drawable.food)
    )

    CategoryGridScreen(categories, navController)
}