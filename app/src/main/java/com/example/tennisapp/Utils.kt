package com.example.tennisapp

fun addPointToPlayer(player: Player, opponent: Player, isServer: Boolean, isTiebreak: Boolean) {
    val scores = listOf(0, 15, 30, 40)
    if (player.score == 40 && opponent.score == 40) {
        // Einstand-Logik
        if (isTiebreak) {
            player.tiebreakPoints++
        } else {
            // Vorteil-Logik
            if (player.score == 40 && opponent.score == 40) {
                player.score = 0
                opponent.score = 0
            } else if (player.score == 40) {
                // Vorteil Vor
                player.score = -1  // Special value for Vorteil Vor
            } else if (opponent.score == 40) {
                // Vorteil Rück
                opponent.score = -1  // Special value for Vorteil Rück
            }
        }
    } else if (player.score == -1) {
        // Vorteil Vor, Punkt gewonnen
        player.score = 0
        opponent.score = 0
        addGameToPlayer(player, isServer)
    } else if (opponent.score == -1) {
        // Vorteil Rück, Punkt gewonnen
        opponent.score = 0
        player.score = 0
        addGameToPlayer(opponent, isServer)
    } else {
        player.score = when (player.score) {
            0 -> 15
            15 -> 30
            30 -> 40
            else -> player.score
        }
    }
}

fun addGameToPlayer(player: Player, isServer: Boolean) {
    player.gamesWon++
    if (player.gamesWon >= 6) {
        // Überprüfen, ob der Satz gewonnen wurde
        if (player.gamesWon - (player.gamesWon - player.gamesWon) >= 2) {
            addSetToPlayer(player)
        }
    }
}

fun addSetToPlayer(player: Player) {
    player.setsWon++
    // Logik zum Überprüfen des Satzes und des Matchs
    if (player.setsWon >= 3) {  // Für ein Best-of-5-Match
        endMatch(player)
    } else if (player.setsWon >= 2) {  // Für ein Best-of-3-Match
        endMatch(player)
    }
    // Reset Spiele nach dem Gewinn eines Satzes
    player.gamesWon = 0
    // Falls Match-Tiebreak aktiv ist, könnte hier auch ein zusätzlicher Match-Tiebreak hinzugefügt werden
}

fun endMatch(winner: Player) {
    // End-Match-Logik, z.B. Anzeigen des Gewinners
    println("${winner.name} hat das Match gewonnen!")
}
