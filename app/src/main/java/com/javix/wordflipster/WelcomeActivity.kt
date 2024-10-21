package com.javix.wordflipster

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val roundCornerShape = 100

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
               EditLettersButton()
                MinutesButton()
                Button(
                    onClick = { /*TODO*/ },
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp),  // Rounded corners
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                    modifier = Modifier.size(200.dp, 50.dp)
                ) {
                    Text("Play", color = Color.White)
                }
            }
        }
        }
}

@Composable
fun EditLettersButton() {

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(onClick = {},
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            modifier = Modifier.size(50.dp, 50.dp)) {Text("-", color = Color.Black)}
        Button(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp),  // Rounded corners
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),  // Blue background
            modifier = Modifier.size(150.dp, 50.dp)
        ) {
            Text("4 Letters", color = Color.White)  // White text
        }
        Button(onClick = {},
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            modifier = Modifier.size(50.dp, 50.dp)) {Text("+", color = Color.Black)}
    }

}

@Composable
fun MinutesButton() {

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(onClick = {},
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            modifier = Modifier.size(50.dp, 50.dp)) {Text("-", color = Color.Black)}
        Button(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp),  // Rounded corners
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),  // Blue background
            modifier = Modifier.size(150.dp, 50.dp)
        ) {
            Text("2 Minutes", color = Color.White)  // White text
        }
        Button(onClick = {},
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            modifier = Modifier.size(50.dp, 50.dp)) {Text("+", color = Color.Black)}
    }

}