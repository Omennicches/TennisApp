package com.example.tennisapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import com.example.tennisapp.ui.theme.TennisAppTheme
import androidx.compose.runtime.*




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TennisAppTheme {
                // Zustand, um den aktuellen Screen zu steuern
                var matchStarted by remember { mutableStateOf(false) }

                // Initialisiere die Spieler
                val playerA = remember {
                    mutableStateOf(Player(
                        name = "Spieler A",
                        score = 0,
                        gamesWon = 0,
                        setsWon = 0,
                        isServing = true
                    ))
                }
                val playerB = remember {
                    mutableStateOf(Player(
                        name = "Spieler B",
                        score = 0,
                        gamesWon = 0,
                        setsWon = 0,
                        isServing = false
                    ))
                }

                // Zustand für die Satz-Ergebnisse
                val setScores = remember { mutableStateListOf<Pair<Int, Int>>() }

                // Zustand für Match-Tiebreak
                val matchTieBreak = remember { mutableStateOf(false) }

                // Zustand für ausgewählte Einstellungen
                val selectedSets = remember { mutableStateOf(1) }
                val selectedSurface = remember { mutableStateOf("Sand") }
                val setTieBreak = remember { mutableStateOf(true) }

                // UI wird basierend auf dem Zustand `matchStarted` gesteuert
                if (matchStarted) {
                    MatchScreen(
                        playerA = playerA.value,
                        playerB = playerB.value,
                        selectedSurface = selectedSurface.value,
                        setTieBreak = setTieBreak.value,
                        selectedServer = playerA.value.name,  // Beispielhaft Spieler A als Server gesetzt
                        matchTieBreak = matchTieBreak.value,
                        onBack = { matchStarted = false }  // Zurück zum Startbildschirm
                    )
                } else {
                    TennisStartScreen(
                        onStartMatch = {
                            matchTieBreak.value = it.matchTieBreak
                            selectedSets.value = it.selectedSets
                            selectedSurface.value = it.selectedSurface
                            setTieBreak.value = it.setTieBreak
                            matchStarted = true
                        }
                    )
                }
            }
        }
    }
}
