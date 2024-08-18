package com.example.tennisapp

import androidx.compose.runtime.*


// Funktion zum Starten des Satz-Tiebreaks
fun startSetTieBreak(playerA: Player, playerB: Player) {
    playerA.tiebreakPoints = 0
    playerB.tiebreakPoints = 0
}

// Funktion zum Starten des Match-Tiebreaks
fun startMatchTieBreak(playerA: Player, playerB: Player) {
    playerA.tiebreakPoints = 0
    playerB.tiebreakPoints = 0
}

// Funktion zum Vergeben eines Punktes
fun addPointToPlayer(
    player: Player,
    opponent: Player,
    isServing: Boolean,
    isTiebreak: Boolean,
    isMatchTiebreak: Boolean,
    setTieBreak: Boolean,
    setsToWin: Int,
    currentSet: Int
) {
    if (isTiebreak) {
        if (isMatchTiebreak) {
            addPointInMatchTieBreak(player, opponent)
        } else {
            addPointInSetTieBreak(player, opponent)
        }
    } else {
        addPointInRegularGame(player, opponent, currentSet, setsToWin)
    }

    checkAndHandleSetWin(player, opponent, setsToWin, currentSet)
}

// Funktion zum Vergeben eines Punktes im Satz-Tiebreak
fun addPointInSetTieBreak(player: Player, opponent: Player) {
    player.tiebreakPoints += 1
    if (player.tiebreakPoints >= 7 && player.tiebreakPoints - opponent.tiebreakPoints >= 2) {
        player.gamesWon += 1 // Satz gewonnen
        player.tiebreakPoints = 0
        opponent.tiebreakPoints = 0
    }
}

// Funktion zum Vergeben eines Punktes im Match-Tiebreak
fun addPointInMatchTieBreak(player: Player, opponent: Player) {
    player.tiebreakPoints += 1
    if (player.tiebreakPoints >= 10 && player.tiebreakPoints - opponent.tiebreakPoints >= 2) {
        player.setsWon += 1 // Match gewonnen
        player.tiebreakPoints = 0
        opponent.tiebreakPoints = 0
    }
}

// Funktion zum Vergeben eines Punktes im regulären Spiel
fun addPointInRegularGame(player: Player, opponent: Player, currentSet: Int, setsToWin: Int) {
    when (player.score) {
        0 -> player.score = 1  // LOVE -> FIFTEEN
        1 -> player.score = 2  // FIFTEEN -> THIRTY
        2 -> player.score = 3  // THIRTY -> FORTY
        3 -> {
            if (opponent.score == 3) {  // Beide haben FORTY
                if (player.score == 4) {  // Spieler hat ADVANTAGE
                    player.score = 0  // ADVANTAGE -> LOVE (Spiel gewonnen)
                    player.gamesWon += 1
                    checkAndHandleSetWin(player, opponent, setsToWin, currentSet)
                } else if (opponent.score == 4) {  // Gegner hat ADVANTAGE
                    player.score = 3  // ADVANTAGE -> DEUCE
                } else {
                    player.score = 4  // FORTY -> ADVANTAGE
                }
            } else {
                player.score = 0  // FORTY -> LOVE (Spiel gewonnen)
                player.gamesWon += 1
                checkAndHandleSetWin(player, opponent, setsToWin, currentSet)
            }
        }
        4 -> {  // Spieler hat ADVANTAGE
            player.score = 0  // ADVANTAGE -> LOVE (Spiel gewonnen)
            player.gamesWon += 1
            checkAndHandleSetWin(player, opponent, setsToWin, currentSet)
        }
    }
}

// Funktion zur Überprüfung und Handhabung des Satzgewinns
fun checkAndHandleSetWin(
    player: Player,
    opponent: Player,
    setsToWin: Int,
    currentSet: Int
) {
    if (player.gamesWon >= 6 && player.gamesWon - opponent.gamesWon >= 2) {
        player.setsWon += 1
        player.gamesWon = 0
        opponent.gamesWon = 0

        if (player.setsWon >= setsToWin) {
            // Match gewonnen
            // Hier kannst du Code hinzufügen, um das Match als gewonnen zu kennzeichnen
        } else if (player.setsWon >= 2 && currentSet >= 3 && setsToWin == 3) {
            // Setze den Match-Tiebreak ein
            startMatchTieBreak(player, opponent)
        } else {
            // Set-Tiebreak einleiten
            if (currentSet >= 1) {
                startSetTieBreak(player, opponent)
            }
        }
    }
}

// Funktion zum Wechseln des Aufschlägers im Tiebreak
fun switchServerInTieBreak(
    currentServer: Player,
    opponent: Player,
    tieBreakServeCount: Int
): Player {
    return if (tieBreakServeCount % 2 == 0) {
        currentServer
    } else {
        opponent
    }
}

// Funktion zur Bearbeitung des Confirm-Buttons
fun handleConfirmButton(
    selectedPlayer: Player?,
    server: Player,
    opponent: Player,
    isTiebreak: Boolean,
    isMatchTiebreak: Boolean,
    setTieBreak: Boolean,
    setsToWin: Int,
    currentSet: Int,
    tieBreakServeCount: Int,
    onUpdateServer: (Player) -> Unit,
    onUpdatePlayerStats: (Player, Player) -> Unit
) {
    if (selectedPlayer != null) {
        addPointToPlayer(
            selectedPlayer,
            opponent,
            selectedPlayer == server,
            isTiebreak,
            isMatchTiebreak,
            setTieBreak,
            setsToWin,
            currentSet
        )
        val newServer = switchServerInTieBreak(
            server,
            opponent,
            tieBreakServeCount
        )
        onUpdateServer(newServer)
        onUpdatePlayerStats(selectedPlayer, opponent)
    }
}
