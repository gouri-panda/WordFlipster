package com.javix.wordflipster

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.javix.wordflipster.ui.theme.WordFlipsterTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun ChallengesList(challenges: List<Challenge>) {
    WordFlipsterTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = "Dashboard",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(challenges) { challenge ->
                    ChallengeItem(challenge)
                }
            }
        }
    }

}

@Composable
fun ChallengeItem(challenge: Challenge) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon with background circle for better visual structure
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.word_flip),
                    contentDescription = "Challenge Icon",
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = challenge.gameType?.name ?: "",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Words Solved: ${challenge.wordsSolved} / ${challenge.totalWords}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row{
                    Text(
                        text = "Time: ${challenge.timeTaken / 60} min",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "Date: ${formatDate(challenge.date)} min",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "Avg Time: ${convertReadableTimeToString(calculateAverageTime(challenge.timeTaken * 1000, challenge.wordsSolved))} Sec / word",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            }

            Spacer(modifier = Modifier.width(8.dp))
            PercentageCircle(percentage = calculatePercentage(challenge.wordsSolved, challenge.totalWords).toFloat())
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
    viewModel.loadChallenges()
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
            style = MaterialTheme.typography.bodySmall,
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

