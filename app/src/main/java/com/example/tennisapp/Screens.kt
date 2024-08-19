package com.example.tennisapp.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tennisapp.ConfirmButton
import com.example.tennisapp.Player
import com.example.tennisapp.Scoreboard
import com.example.tennisapp.Spiellogik

// TennisStartScreen: This composable represents the initial screen where users enter player names and match settings.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TennisStartScreen(navController: NavController) {
    // State variables to store player names, number of sets, and tie-break settings
    var playerOneName by remember { mutableStateOf(TextFieldValue("")) }
    var playerTwoName by remember { mutableStateOf(TextFieldValue("")) }
    var numberOfSets by remember { mutableStateOf(3) }
    var tieBreakEnabled by remember { mutableStateOf(true) }
    var matchTieBreakEnabled by remember { mutableStateOf(false) }

    // Column to arrange UI elements vertically
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Text fields for player names
        OutlinedTextField(
            value = playerOneName,
            onValueChange = { playerOneName = it },
            label = { Text("Player One Name") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = playerTwoName,
            onValueChange = { playerTwoName = it },
            label = { Text("Player Two Name") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Radio buttons for selecting the number of sets
        Row {
            RadioButton(
                selected = numberOfSets == 3,
                onClick = { numberOfSets = 3 }
            )
            Text("Best of 3 Sets")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = numberOfSets == 5,
                onClick = { numberOfSets = 5 }
            )
            Text("Best of 5 Sets")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Checkboxes for enabling tie-break and match tie-break
        Row {
            Checkbox(
                checked = tieBreakEnabled,
                onCheckedChange = { tieBreakEnabled = it }
            )
            Text("Enable Tie-Break")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Checkbox(
                checked = matchTieBreakEnabled,
                onCheckedChange = { matchTieBreakEnabled = it }
            )
            Text("Enable Match Tie-Break")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Button to start the match
        Button(
            onClick = {
                // Set game rules in Spiellogik
                Spiellogik.numberOfSets = numberOfSets
                Spiellogik.setTieBreakEnabled = tieBreakEnabled
                Spiellogik.matchTieBreakEnabled = matchTieBreakEnabled

                // Create players with empty lists for gamesWon, setsWon, and setTieBreakScore
                val playerA = Player(
                    name = playerOneName.text,
                    score = 0,
                    gamesWonCurrentSet = 0,
                    gamesWon = emptyList(),
                    currentSetsWon = 0,
                    setsWon = emptyList(),
                    currentTieBreakScore = 0,
                    setTieBreakScore = emptyList(),
                    currentMatchTieBreakScore = 0,
                    matchTieBreakScore = emptyList()
                )
                val playerB = Player(
                    name = playerTwoName.text,
                    score = 0,
                    gamesWonCurrentSet = 0,
                    gamesWon = emptyList(),
                    currentSetsWon = 0,
                    setsWon = emptyList(),
                    currentTieBreakScore = 0,
                    setTieBreakScore = emptyList(),
                    currentMatchTieBreakScore = 0,
                    matchTieBreakScore = emptyList()
                )


                navController.currentBackStackEntry?.savedStateHandle?.set("playerA", playerA)
                navController.currentBackStackEntry?.savedStateHandle?.set("playerB", playerB)

                navController.navigate("match_screen")
            }
        ) {
            Text("Start Match")
        }
    }
}

// MatchScreen: This composable represents the main match screen where the score and game controls are displayed.
@Composable
fun MatchScreen(navController: NavController) {
    // Retrieve player data from navigation arguments
    val playerA = navController.currentBackStackEntry?.savedStateHandle?.get<Player>("playerA")
    val playerB = navController.currentBackStackEntry?.savedStateHandle?.get<Player>("playerB")

    // State variable to track the selected player
    var selectedPlayer by remember { mutableStateOf<Player?>(null) }

    if (playerA != null && playerB != null) {
        // Calculate the current game points
        val currentGamePointsA = playerA.score
        val currentGamePointsB = playerB.score

        // Determine if a tiebreak is active
        val isTieBreak = Spiellogik.setTieBreakMode || Spiellogik.matchTieBreakMode

        // Get the tiebreak scores
        val tieBreakScoreA = if (isTieBreak) playerA.setTieBreakScore else null
        val tieBreakScoreB = if (isTieBreak) playerB.setTieBreakScore else null

        // Determine the server
        val server = Spiellogik.currentServer ?: playerA

        // Determine if it's a match tiebreak
        val matchTieBreak = Spiellogik.matchTieBreakMode
        val gamesWonA = playerA.gamesWon
        val gamesWonB = playerB.gamesWon
        val setsWonA = playerA.setsWon
        val setsWonB = playerB.setsWon
        val numberOfSets = Spiellogik.numberOfSets

        // UI Layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the scoreboard
            Scoreboard(
                playerA = playerA,
                playerB = playerB,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Buttons to select a player
            Row {
                Button(onClick = { selectedPlayer = playerA }) {
                    Text(playerA.name)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { selectedPlayer = playerB }) {
                    Text(playerB.name)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Confirm button to award a point to the selected player
            ConfirmButton(
                selectedPlayer = selectedPlayer,
                server = server,
                opponent = if (server == playerA) playerB else playerA,
                isTiebreak = isTieBreak,
                onSelectedPlayerChange = { selectedPlayer = it },
                showPointButtons = selectedPlayer != null,
                selectedPoint = remember { mutableStateOf<String?>(null) },
                selectedStroke = remember { mutableStateOf<String?>(null) },
                selectedForm = remember { mutableStateOf<String?>(null) },
                navController = navController
            )
        }
    }
}
