package com.javix.wordflipster

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.w3c.dom.Text

@Composable
fun PhraseEndingScreen(level: Int, quote: String, timer: String, onNextButtonClickListener:(Int) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F1EF)) // Light background color
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Level Header
            Box(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "Level $level",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7E5A87)
                )
            }

            // Quote Box
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = quote,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp,
                        color = Color(0xFF333333),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = "Marilyn Monroe\nAmerican actress, 1926-1962",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Timer
            Text(
                text = "",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7E5A87)
            ) // Todo Add this later

            // Next Button
            Button(
                onClick = { onNextButtonClickListener(level)},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22B573)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(16.dp, bottom = 32.dp)
                    .fillMaxWidth(0.5f)
                    .height(50.dp)
            ) {
                Text(
                    text = "NEXT",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

        }
    }
}