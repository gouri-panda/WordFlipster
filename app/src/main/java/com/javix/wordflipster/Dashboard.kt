package com.javix.wordflipster

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun ChallengesList(challenges: List<Challenge>) {
    LazyColumn {
        items(challenges) { challenge ->
            ChallengeItem(challenge)
        }
    }
}

@Composable
fun ChallengeItem(challenge: Challenge) {

    Row( modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, top =4.dp, bottom = 4.dp, end = 4.dp)
        .background(MaterialTheme.colors.surface)
        .padding(start = 4.dp, top =4.dp, bottom = 4.dp, end =4.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Words Solved: ${challenge.wordsSolved}/${challenge.totalWords}", style = MaterialTheme.typography.body2)
            Spacer(modifier = Modifier.padding(top = 4.dp))
            Text(text = "Total time: 3/5 Min", style = MaterialTheme.typography.body2)
        }
        Column(modifier = Modifier.weight(0.3f)) {
            PercentageCircle(percentage = calculatePercentage(challenge.wordsSolved, challenge.totalWords).toFloat())
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Date: ${formatDate(challenge?.date)}", style = MaterialTheme.typography.body2)
            Spacer(modifier = Modifier.padding(top = 4.dp))
            Text(text ="Average time: ${calculateAverageTime(challenge.timeTaken, challenge.wordsSolved)}", style = MaterialTheme.typography.body2)
        }
    }
}

@Composable
fun ChallengesScreen(viewModel: ChallengeViewModel) {
    val challenges by viewModel.challenges.observeAsState(emptyList())
    viewModel.loadChallenges()

    Column {
        ChallengesList(challenges)
    }
}


@Preview(showBackground = true)
@Composable
fun TestDashboardScreen() {
    val viewModel: ChallengeViewModel = viewModel()
    ChallengesScreen(viewModel)
}
@Composable
fun PercentageCircle(percentage: Float) {
    val primaryColor = Color.Blue
    val secondaryColor = Color.Red

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(40.dp)
    ) {
        Canvas(modifier = Modifier.size(100.dp)) {
            val sweepAngle = 360 * percentage / 100
            val strokeWidth = 12f

            // Draw the background arc (red)
            drawArc(
                color = secondaryColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Draw the completed arc (blue)
            drawArc(
                color = primaryColor,
                startAngle = 0f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        // Draw the percentage text in the center
        Text(
            text = "${percentage.toInt()}%",
            style = MaterialTheme.typography.h6,
            color = Color.Black,
            fontSize = 14.sp
        )
    }
}
fun formatDate(currentDate: Date?) : String  {
    // Format for date only
    val dateFormat = SimpleDateFormat("yy/MM/dd", Locale.getDefault())
    val dateOnly = dateFormat.format(currentDate)
    println("Date only: $dateOnly")

    // Format for time only
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeOnly = timeFormat.format(currentDate)
    println("Time only: $timeOnly")
    return "$dateOnly $timeOnly"
}
fun calculatePercentage(correctWords: Int, totalWords: Int): Double {
    return if (totalWords > 0) {
        (correctWords.toDouble() / totalWords) * 100
    } else {
        0.0
    }
}

