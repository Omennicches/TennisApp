package com.example.tennisapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

//Baut den Screen in dem der Spielstand erfasst wird
@Composable
fun TennisStartScreen() {
    // Zustand für die Auswahl
    var selectedSets by remember { mutableIntStateOf(1) }
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
        MatchScreen(
            playerA = playerA,
            playerB = playerB,
            selectedSurface = selectedSurface,
            setTieBreak = setTieBreak,
            selectedServer = selectedServer,
            matchtieBreak = matchtieBreak,
            onBack = { matchStarted = false }
        )
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

// MatchScreen composable function
@Composable
fun MatchScreen(
    playerA: Player,
    playerB: Player,
    selectedSurface: String,
    setTieBreak: Boolean,
    selectedServer: String,
    matchtieBreak: Boolean,
    onBack: () -> Unit
) {
    val backgroundColor =
        if (playerA.score > playerB.score) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondaryContainer
    val server = if (playerA.name == selectedServer) playerA else playerB
    val receiver = if (server == playerA) playerB else playerA
    val backgroundImage = when (selectedSurface) {
        "Sand" -> R.drawable.tennisplatz_clay
        "Grass" -> R.drawable.tennisplatz_grass
        "Hartplatz" -> R.drawable.tennisplatz_compund_ace
        else -> R.drawable.tennisplatz_clay
    }

    // State to control the visibility and selection of buttons
    var showPointButtons by remember { mutableStateOf(false) }
    var selectedPlayer by remember { mutableStateOf<Player?>(null) }
    val selectedPoint = remember { mutableStateOf<String?>(null) }
    val selectedStroke = remember { mutableStateOf<String?>(null) }
    val selectedForm = remember { mutableStateOf<String?>(null) }

    // Reset showPointButtons state when selectedPlayer is null
    LaunchedEffect(selectedPlayer) {
        if (selectedPlayer == null) {
            showPointButtons = false
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
        // Hintergrundbild
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Back Button
        IconButton(
            onClick = { onBack() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
                .zIndex(1f) // Ensure it's on top
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Zurück",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Spieler A
            PlayerScoreCard(player = playerA, isServing = playerA.name == selectedServer)

            // Spieler B
            PlayerScoreCard(player = playerB, isServing = playerB.name == selectedServer)

            Spacer(modifier = Modifier.height(80.dp))

            // Spieler Punktet Buttons
            PlayerPointButtons(
                playerA = playerA,
                playerB = playerB,
                selectedPlayer = selectedPlayer,
                onSelectPlayer = { player ->
                    // Handle player selection
                    selectedPlayer = player
                    showPointButtons = true
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Punktet Buttons (bedingte Anzeige)
            if (showPointButtons && selectedPlayer != null) {
                PointButtons(
                    selectedPlayer = selectedPlayer,
                    server = server,
                    opponent = if (server == playerA) playerB else playerA,
                    isTiebreak = setTieBreak,
                    onSelectedPlayerChange = { selectedPlayer = it },
                    showPointButtons = showPointButtons,  // Pass the state reference
                    selectedPoint = selectedPoint,  // Pass the state reference
                    selectedStroke = selectedStroke,  // Pass the state reference
                    selectedForm = selectedForm  // Pass the state reference
                )
            }
        }
    }
}