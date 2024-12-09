package com.javix.wordflipster

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.javix.wordflipster.Navigation.Screens
import com.javix.wordflipster.Navigation.WordFlipsterNavigationSetup
import com.javix.wordflipster.ui.theme.WordFlipsterTheme


class WelcomeScreen: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WordFlipsterTheme {
                val navController = rememberNavController()
                val systemUiController = rememberSystemUiController()

                // Change the status bar color to something darker for better contrast
                systemUiController.setSystemBarsColor(color = Color.Black)
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp),
                ) { innerPadding ->
                    WordFlipsterNavigationSetup(navController)
                }
            }
        }
    }


}


@Composable
fun WelcomeScreenComposeWrapper(navController: NavHostController) {
    TopBar(navController = navController) {}
    GameTypeList(gameTypes = gameTypes()) { gameType ->
        // Handle game type selection
        if (gameType.name == navController.context.getString(R.string.word_flip)) {
            navController.navigate(Screens.WordFlipOnboarding.route)
        } else if (gameType.name == navController.context.getString(R.string.word_Chain)) {
            navController.navigate(Screens.WordChainOnboarding.route)
        } else if(gameType.name ==  navController.context.getString(R.string.word_shuffle)) {
            navController.navigate(Screens.WordShuffleOnboarding.route)
        } else if (gameType.name == navController.context.getString(R.string.word_cryptic)) {
            navController.navigate(Screens.WordCrypticOnboarding.route)
        } else if (gameType.name == navController.context.getString(R.string.word_code)) {
            navController.navigate(Screens.WordigmaScreen.route)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameTypeList(gameTypes: List<GameType>, onGameTypeSelected: (GameType) -> Unit) {
    // Parallax background effect
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Title with Animated Visibility
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(700))
            ) {
                Text(
                    text = "Choose Your Game",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(top = 64.dp, start = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            // Wave Effect at the Bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            )

            // Staggered Slide-In Animation for Cards
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // Two columns
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(gameTypes) { index, gameType ->
                    GameTypeCard(
                        gameType = gameType,
                        onGameTypeSelected = onGameTypeSelected,
                        modifier = Modifier.animateItemPlacement(
                            animationSpec = tween(200 * (index + 1))
                        )
                    )
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTypeCard(
    gameType: GameType,
    onGameTypeSelected: (GameType) -> Unit,
    modifier: Modifier = Modifier
) {
    var isSelected by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (isSelected) 5f else 0f)

    var showToast by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Use LaunchedEffect to show the Toast when showToast is set to true
    LaunchedEffect(showToast) {
        if (showToast) {
            Toast.makeText(context, gameType.description, Toast.LENGTH_SHORT).show()
            showToast = false // Reset the state to avoid repeated toasts
        }
    }


    Card(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .width(140.dp)
            .height(160.dp)
            .shadow(
                elevation = if (isSelected) 16.dp else 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = if (isSelected) Color.White else Color.Transparent // Glow effect
            )
            .graphicsLayer {
                scaleX = if (isSelected) 1.05f else 1f
                scaleY = if (isSelected) 1.05f else 1f
                rotationZ = rotation
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        // Animated tooltip pop-up on long press
                        showToast = true
                    },
                    onTap = {
                        isSelected = !isSelected
                        onGameTypeSelected(gameType)
                    }
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.9f) else MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(16.dp),
        onClick = {
            isSelected = !isSelected
            onGameTypeSelected(gameType)
        },
        interactionSource = remember { MutableInteractionSource() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.TopEnd,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Ribbon for Popular Game Types
                if (gameType.isPopular) {
                    Box(
                        modifier = Modifier
                            .background(Color.Red, shape = RoundedCornerShape(bottomStart = 8.dp))
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "Popular",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }
            }
            Image(
                painter = painterResource(id = gameType.iconResId),
                contentDescription = gameType.name,
                modifier = Modifier
                    .size(56.dp)
                    .graphicsLayer {
                        scaleX = if (isSelected) 1.2f else 1f
                        scaleY = if (isSelected) 1.2f else 1f
                    },
            )
            Text(
                text = gameType.name,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
