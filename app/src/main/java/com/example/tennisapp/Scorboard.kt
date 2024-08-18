package com.example.tennisapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow


@Composable
fun Scoreboard(playerA: Player, playerB: Player) {
    Column {
        Text(text = "Score: ${playerA.score} - ${playerB.score}", color = Color.Red)
        Text(text = "Games: ${playerA.gamesWon} - ${playerB.gamesWon}")
        Text(text = "Sets: ${playerA.setsWon} - ${playerB.setsWon}")

        // Display tie-break score if in tie-break mode
        if (Spiellogik.setTieBreakMode) {
            Text(text = "Tie-Break Score: ${playerA.setTieBreakScore} - ${playerB.setTieBreakScore}")
        }

        // Display match tie-break score if in match tie-break mode
        if (Spiellogik.matchTieBreakMode) {
            Text(text = "Match Tie-Break Score: ${playerA.matchTieBreakScore} - ${playerB.matchTieBreakScore}")
        }
    }
}

@Composable
fun PlayerBox(playerName: String, isServing: Boolean) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(50.dp)
            .background(MaterialTheme.colorScheme.primary)
            .padding(1.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        val density = LocalDensity.current
        val fontSize = with(density) {
            val maxFontSize = 60.sp.toPx()
            val minFontSize = 15.sp.toPx()
            val textLength = playerName.length
            val boxWidth = 120.dp.toPx()
            (boxWidth / (textLength * 2)).coerceIn(minFontSize, maxFontSize).toSp()
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
                .padding(end = if(isServing) 30.dp else 0.dp)
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
    tieBreakScore: Int?
) {
    Box(
        modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        val scoreText = if (isTieBreak) {
            tieBreakScore?.toString() ?: "0"
        } else {
            getPointStatus(currentGamePoints)
        }
        Text(
            text = scoreText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SetScoreBox(
    score: Int,
    showTieBreak: Boolean,
    tieBreakScore: Int?,
    isMatchTieBreak: Boolean
) {
    Box(
        modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .background(MaterialTheme.colorScheme.secondary)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        val text = if (isMatchTieBreak) {
            "$tieBreakScore-$tieBreakScore"
        } else {
            buildString {
                append(score)
                if (showTieBreak && tieBreakScore != null) {
                    append(" ")
                    append(tieBreakScore)
                }
            }
        }
        Text(text = text,
            fontSize = if (isMatchTieBreak) 12.sp else 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = if (isMatchTieBreak) MaterialTheme.colorScheme.error else Color.Unspecified
        )
    }
}