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
    currentGamePointsA:Int,
    currentGamePointsB: Int,
    setScoresA: List<Int>,
    setScoresB: List<Int>,
    isTieBreak: Boolean,
    tieBreakScoreA: Int?,
    tieBreakScoreB: Int?,
    server: Player,
    matchTieBreak: Boolean
) {
    val numberOfSets = maxOf(setScoresA.size, setScoresB.size)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                PlayerBox(playerA.name, isServing = playerA == server)
                ScoreBox(
                    currentGamePoints = currentGamePointsA,
                    isTieBreak = isTieBreak,
                    tieBreakScore = tieBreakScoreA
                )
                // Display SetScoreBoxes for Player A
                SetScoreBox(
                    scoreA = setScoresA.lastOrNull() ?: 0,
                    scoreB = setScoresB.lastOrNull() ?: 0,
                    showTieBreak = numberOfSets > 1,
                    tieBreakScoreA = if (numberOfSets > 1) tieBreakScoreA else null,
                    tieBreakScoreB = if (numberOfSets > 1) tieBreakScoreB else null,
                    isMatchTieBreak = matchTieBreak && numberOfSets == 3
                )
            }
            Row(modifier= Modifier.fillMaxWidth()) {
                PlayerBox(playerB.name, isServing = playerB == server)
                ScoreBox(
                    currentGamePoints = currentGamePointsB,
                    isTieBreak = isTieBreak,
                    tieBreakScore = tieBreakScoreB
                )
                // Display SetScoreBoxes for Player B
                SetScoreBox(
                    scoreA = setScoresA.lastOrNull() ?: 0,
                    scoreB = setScoresB.lastOrNull() ?: 0,
                    showTieBreak = numberOfSets > 1,
                    tieBreakScoreA = if (numberOfSets > 1) tieBreakScoreA else null,
                    tieBreakScoreB = if (numberOfSets > 1) tieBreakScoreB else null,
                    isMatchTieBreak = matchTieBreak && numberOfSets == 3
                )
            }
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
    scoreA: Int,
    scoreB: Int,
    showTieBreak: Boolean,
    tieBreakScoreA: Int?,
    tieBreakScoreB: Int?,
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
            "$tieBreakScoreA-$tieBreakScoreB"
        } else {
            buildString {
                append(scoreA)
                append("\n")
                append(scoreB)
                if (showTieBreak && tieBreakScoreA != null && tieBreakScoreB != null) {
                    append(" ")
                    append(tieBreakScoreA)
                    append("-")
                    append(tieBreakScoreB)
                }
            }
        }
        Text(
            text = text,
            fontSize = if (isMatchTieBreak) 12.sp else 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = if (isMatchTieBreak) MaterialTheme.colorScheme.error else Color.Unspecified
        )
    }
}