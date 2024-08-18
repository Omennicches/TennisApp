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
import androidx.compose.material3.*


//Baut den Screen in dem der Spielstand erfasst wird
@Composable
fun TennisStartScreen(
    onStartMatch: (MatchSettings) -> Unit
) {
    // Zustand für die Auswahl
    var selectedSets by remember { mutableIntStateOf(1) }
    var selectedServer by remember { mutableStateOf("Spieler A") }
    var matchtieBreak by remember { mutableStateOf(true) }
    var matchStarted by remember { mutableStateOf(false) }
    var selectedSurface by remember { mutableStateOf("Sand") }
    var setTieBreak by remember { mutableStateOf(true) }

    // Player Variables
    var playerA by remember {
        mutableStateOf(Player(
            name = "Spieler A",
            score = 0,
            setScores = mutableListOf(),  // Leere Liste für den Anfang
            gamesWon = 0,
            setsWon = 0
        ))
    }
    var playerB by remember {
        mutableStateOf(Player(
            name = "Spieler B",
            score = 0,
            setScores = mutableListOf(),  // Leere Liste für den Anfang
            gamesWon = 0,
            setsWon = 0
        ))
    }



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
            matchTieBreak = matchtieBreak,  // Richtiger Name
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

@Composable
fun MatchScreen(
    playerA: Player,
    playerB: Player,
    selectedSurface: String,
    setTieBreak: Boolean,
    matchTieBreak: Boolean,
    selectedServer: String,
    onBack: () -> Unit
) {
    val backgroundColor =
        if (playerA.score > playerB.score) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondaryContainer
    val server = if (playerA.name == selectedServer) playerA else playerB
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

    // Dynamische Verwaltung der Sätze
    val setScoresA = playerA.setScores.map { it.first }
    val setScoresB = playerB.setScores.map { it.second }

    // Berechnung der aktuellen Punkte und TieBreak-Punkte
    val currentGamePointsA = playerA.score
    val currentGamePointsB = playerB.score
    val tieBreakScoreA = if (setTieBreak || matchTieBreak) playerA.tiebreakPoints else null
    val tieBreakScoreB = if (setTieBreak || matchTieBreak) playerB.tiebreakPoints else null

    Box(modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor)) {
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
                .zIndex(1f)
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
            Spacer(modifier = Modifier.height(30.dp))

            // Dynamisches Scoreboard
            Scoreboard(
                playerA = playerA,
                playerB = playerB,
                currentGamePointsA = currentGamePointsA,
                currentGamePointsB = currentGamePointsB,
                setScoresA = setScoresA,
                setScoresB = setScoresB,
                isTieBreak = setTieBreak || matchTieBreak,
                tieBreakScoreA = tieBreakScoreA,
                tieBreakScoreB = tieBreakScoreB,
                server = server
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Spieler Punktet Buttons
            PlayerPointButtons(
                playerA = playerA,
                playerB = playerB,
                selectedPlayer = selectedPlayer,
                onSelectPlayer = { player ->
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
                    isTiebreak = setTieBreak || matchTieBreak,
                    onSelectedPlayerChange = { selectedPlayer = it },
                    showPointButtons = showPointButtons,
                    selectedPoint = selectedPoint,
                    selectedStroke = selectedStroke,
                    selectedForm = selectedForm
                )
            }
        }
    }
}

