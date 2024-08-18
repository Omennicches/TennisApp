package com.example.tennisapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tennisapp.ui.theme.TennisAppTheme
import androidx.compose.material3.Icon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow


@Composable
fun Scoreboard(
    playerA: Player,
    playerB: Player,
    currentGamePointsA: Int,
    currentGamePointsB: Int,
    setScoresA: List<Int>,
    setScoresB: List<Int>,
    isTieBreak: Boolean,
    tieBreakScoreA: Int?,
    tieBreakScoreB: Int?,
    server: Player
) {val showTieBreak = isTieBreak || setScoresA.size > 1 // Show tiebreak in sets after the first one

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) { // Row for Player A
                PlayerBox(playerA.name, isServing = playerA == server)
                ScoreBox(
                    currentGamePoints = currentGamePointsA,
                    isTieBreak = isTieBreak,
                    tieBreakScore = tieBreakScoreA
                )
                // SetScoreBox for Player A
                if (setScoresA.isNotEmpty() && setScoresB.isNotEmpty()) {
                    SetScoreBox(
                        scoreA = setScoresA.last(),
                        scoreB = setScoresB.last(),
                        showTieBreak = showTieBreak,
                        tieBreakScoreA = if (showTieBreak) tieBreakScoreA else null,
                        tieBreakScoreB = if (showTieBreak) tieBreakScoreB else null
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) { // Row for Player B
                PlayerBox(playerB.name, isServing = playerB == server)
                ScoreBox(
                    currentGamePoints = currentGamePointsB,
                    isTieBreak = isTieBreak,
                    tieBreakScore = tieBreakScoreB
                )
                // SetScoreBox for Player B
                if (setScoresA.isNotEmpty() && setScoresB.isNotEmpty()) {
                    SetScoreBox(
                        scoreA = setScoresA.last(),
                        scoreB =setScoresB.last(),
                        showTieBreak = showTieBreak,
                        tieBreakScoreA = if (showTieBreak) tieBreakScoreA else null,
                        tieBreakScoreB = if (showTieBreak) tieBreakScoreB else null
                    )
                }
            }
        }
    }
}


@Composable
fun PlayerBox(playerName: String, isServing: Boolean) {
    Box(
        modifier = Modifier
            .width(120.dp) // Vorgeschriebene Breite für die PlayerBox
            .height(50.dp)
            .background(MaterialTheme.colorScheme.primary)
            .padding(1.dp),
        contentAlignment = Alignment.CenterStart
    ){
        val density = LocalDensity.current
        val fontSize = with(density) {
            val maxFontSize = 60.sp.toPx()
            val minFontSize = 15.sp.toPx() // Adjust min font size as needed
            val textLength = playerName.length
            val boxWidth = 120.dp.toPx() // Use box width for calculation
            val sizeRatio = boxWidth / textLength
            (sizeRatio / 2).coerceIn(minFontSize, maxFontSize).toSp() // Adjust divisor as needed
        }
        Text(
            text = playerName,
            fontSize = fontSize,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Left,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = if (isServing) 30.dp else 0.dp)
        )
        if (isServing) {
            Image(
                painter = painterResource(id = R.drawable.tennisball),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterEnd)
            )
        }
    }
}


@Composable
fun ScoreBox(
    currentGamePoints: Int,
    isTieBreak: Boolean,
    tieBreakScore: Int?,
) {
    Box(
        modifier = Modifier
            .width(50.dp)
            .height(50.dp) // Gleiche Höhe wie die PlayerBox
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isTieBreak && tieBreakScore != null) {
                "$tieBreakScore"
            } else {
                getPointStatus(currentGamePoints)
            },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun SetScoreBox(
    scoreA: Int,
    scoreB: Int,
    showTieBreak: Boolean,
    tieBreakScoreA: Int?,
    tieBreakScoreB: Int?
) {
    Box(
        modifier = Modifier
            .width(50.dp)
            .height(50.dp) // Gleiche Höhe wie die PlayerBox
            .background(MaterialTheme.colorScheme.secondary)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "$scoreA", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "$scoreB", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            if (showTieBreak && tieBreakScoreA != null && tieBreakScoreB != null) {
                Text(
                    text = "$tieBreakScoreA-$tieBreakScoreB",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
