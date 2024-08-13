package com.example.tennisapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tennisapp.ui.theme.TennisAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TennisAppTheme {
                TennisStartScreen()
            }
        }
    }
}
data class Player(
    var name: String,
    var score: Int = 0
)

//Baut den Screen in dem der Spielstand erfasst wird
@Composable
fun TennisStartScreen() {
    // Zustand für die Auswahl
    var selectedSets by remember { mutableStateOf(1) }
    var selectedServer by remember { mutableStateOf("Spieler A") }
    var matchtieBreak by remember { mutableStateOf(true) }
    var matchStarted by remember { mutableStateOf(false) }
    var selectedSurface by remember { mutableStateOf("Sand") }
    var setTieBreak by remember { mutableStateOf(true) }

    // Player Variables
    var playerA by remember { mutableStateOf(Player("Spieler A")) }
    var playerB by remember { mutableStateOf(Player("Spieler B")) }

    val backgroundImage = when (selectedSurface) {
        "Sand" -> R.drawable.tennisplatz_clay
        "Grass" -> R.drawable.tennisplatz_grass
        "Hartplatz" -> R.drawable.tennisplatz_compund_ace
        else -> R.drawable.tennisplatz_clay
    }

    if (matchStarted) {
        MatchScreen(playerA = playerA, playerB = playerB, onBack = { matchStarted = false })
    } else {
        // Layout
        Box(modifier = Modifier.fillMaxSize()) {

            // Hintergrundbild
            Image(
                painter = painterResource(id = backgroundImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Untergrund
                Text("Untergrund:", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Row {
                    SelectionButton(label = "Sand", selected = selectedSurface == "Sand") { selectedSurface = "Sand" }
                    Spacer(modifier = Modifier.width(16.dp))
                    SelectionButton(label = "Grass", selected = selectedSurface == "Grass") { selectedSurface = "Grass" }
                    Spacer(modifier = Modifier.width(16.dp))
                    SelectionButton(label = "Hartplatz", selected = selectedSurface == "Hartplatz") { selectedSurface = "Hartplatz" }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Auswahl der Anzahl der Sätze
                Text("Anzahl der Sätze:", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Row {
                    SelectionButton(label = "1", selected = selectedSets == 1) { selectedSets = 1 }
                    Spacer(modifier = Modifier.width(16.dp))
                    SelectionButton(label = "3", selected = selectedSets == 3) { selectedSets = 3 }
                    Spacer(modifier = Modifier.width(16.dp))
                    SelectionButton(label = "5", selected = selectedSets == 5) { selectedSets = 5 }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Auswahl für Match Tie-Break
                Text("Match Tie Break:", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Row {
                    SelectionButton(label = "Ja", selected = matchtieBreak) { matchtieBreak = true }
                    Spacer(modifier = Modifier.width(16.dp))
                    SelectionButton(label = "Nein", selected = !matchtieBreak) { matchtieBreak = false }
                }

                Spacer(modifier = Modifier.height(10.dp))

                //Auswahl Satz Tie Break
                Text("Satz Tiebreak:", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Row {
                    SelectionButton(label = "Ja", selected = setTieBreak) { setTieBreak = true }
                    Spacer(modifier = Modifier.width(16.dp))
                    SelectionButton(label = "Nein", selected = !setTieBreak) { setTieBreak = false }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Auswahl des Aufschlägers
                Text("Aufschläger:", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Row {
                    SelectionButton(label = playerA.name, selected = selectedServer == playerA.name) { selectedServer = playerA.name }
                    Spacer(modifier = Modifier.width(20.dp))
                    SelectionButton(label = playerB.name, selected = selectedServer == playerB.name) { selectedServer = playerB.name }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Start Match Button
                Button(
                    onClick = { matchStarted = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .clip(CircleShape),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) {
                    Text("Match Starten!", fontSize = 35.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}


@Composable
fun MatchScreen(
    playerA: Player,
    playerB: Player,
    onBack: () -> Unit
) {
    // State to control the display of scoring reasons
    var selectedReason by remember { mutableStateOf<String?>(null) }
    var scorer by remember { mutableStateOf<Player?>(null) }
    val backgroundColor = if (playerA.score > playerB.score) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondaryContainer

    // Layout for the match screen
    Box(modifier = Modifier.fillMaxSize().background(backgroundColor)) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Scoreboard
            Scoreboard(playerA, playerB)

            Spacer(modifier = Modifier.height(20.dp))

            // Buttons for scoring
            Row {
                Button(onClick = {
                    scorer = playerA
                    selectedReason = null
                }) {
                    Text("Spieler A Punktet")
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(onClick = {
                    scorer = playerB
                    selectedReason = null
                }) {
                    Text("Spieler B Punktet")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Scoring reasons
            scorer?.let { scoringPlayer ->
                Text("Grund für Punkt:")
                Row {
                    Button(onClick = { selectedReason = "Ace" }, enabled = scoringPlayer == playerA) {
                        Text("Ass")
                    }
                    Button(onClick = { selectedReason = "Aufschlagsfehler" }, enabled = scoringPlayer == playerB) {
                        Text("Aufschlagsfehler")
                    }
                    Button(onClick = { selectedReason = "Netzfehler" }) {
                        Text("Netzfehler")
                    }
                    Button(onClick = { selectedReason = "Aus" }) {
                        Text("Aus")
                    }
                    Button(onClick = { selectedReason = "Doppel-Aufpraller" }) {
                        Text("Doppel-Aufpraller")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Confirmation button
            Button(onClick = {
                scorer?.let { scoringPlayer ->
                    scoringPlayer.score++
                    selectedReason = null
                    scorer = null
                }
            }) {
                Text("Bestätigen")
            }
        }
    }
}

@Composable
fun Scoreboard(playerA: Player, playerB: Player) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(playerA.name, fontWeight = FontWeight.Bold)
            Text("${playerA.score}")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(playerB.name, fontWeight = FontWeight.Bold)
            Text("${playerB.score}")
        }
    }
}

//Baut die Selection Buutons
@Composable
fun SelectionButton(label: String, selected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondaryContainer
    val textColor = if (selected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSecondaryContainer

    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        val density = LocalDensity.current
        val fontSize = with(density) {
            // Calculate the font size based on the label length
            // Maximum font size is 20.sp, minimum font size is 10.sp
            (80 / label.length).dp.toSp().value.coerceIn(15f, 25f).sp
        }
        Text(
            text = label,
            color = textColor,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            maxLines = 2,  // Allow up to 2 lines of text
            textAlign = TextAlign.Center,
            softWrap = true, //enables text wrapping without breaking words
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 1.dp)
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TennisAppTheme {
        TennisStartScreen()
    }
}
