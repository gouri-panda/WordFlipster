package com.javix.wordflipster

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.javix.wordflipster.ui.theme.darkColorCustomColor
import com.javix.wordflipster.ui.theme.lightColorCustomColor
import com.javix.wordflipster.ui.theme.mediumColorCustomColor
import com.javix.wordflipster.ui.theme.wordgimaBackgroundScreen

@Composable
fun BaseScreen(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors =
//                    listOf(
//                        Color(0xFF6D4C41), // Light Brown
//                        Color(0xFF5D4037), // Medium Brown
//                        Color(0xFF4E342E)  // Dark Brown
//                    ) // Todo experiment with those

                    listOf(
                        lightColorCustomColor,
                        mediumColorCustomColor,
                        darkColorCustomColor,
                    )

                )
            )
    ) {
        content()
    }
}

@Composable
fun WordigmaBaseScreen(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors =
                    listOf(
                        wordgimaBackgroundScreen,
                        wordgimaBackgroundScreen
                    )

                )
            )
    ) {
        content()
    }
}

