package com.example.tennisapp

import androidx.navigation.NavController

object Spiellogik {

    // Game Rules (set in TennisStartScreen)
    var numberOfSets = 3
    var setTieBreakEnabled = true
    var matchTieBreakEnabled = true
    var initialServer: Player? = null

    // Match State
    var currentServer: Player? = null

    // Tiebreak Modes
    var setTieBreakMode = false
    var matchTieBreakMode = false

    fun initializeMatch(navController: NavController) {
        val playerA = navController.previousBackStackEntry?.savedStateHandle?.get<Player>("playerA")!!
        val playerB = navController.previousBackStackEntry?.savedStateHandle?.get<Player>("playerB")!!

        currentServer = initialServer
        playerA.score = 0
        playerB.score = 0
        playerA.gamesWon = playerA.gamesWon
        playerB.gamesWon = playerB.gamesWon
        playerA.setsWon= playerA.setsWon
        playerB.setsWon = playerB.setsWon
        playerA.setTieBreakScore = playerA.setTieBreakScore
        playerB.setTieBreakScore = playerB.setTieBreakScore
        playerA.matchTieBreakScore = playerA.matchTieBreakScore
        playerB.matchTieBreakScore = playerB.matchTieBreakScore
        setTieBreakMode = false
        matchTieBreakMode = false
    }

    fun awardPoint(player: Player, navController: NavController) {
        val playerA = navController.previousBackStackEntry?.savedStateHandle?.get<Player>("playerA")!!
        val playerB = navController.previousBackStackEntry?.savedStateHandle?.get<Player>("playerB")!!

        if (matchTieBreakMode) {
            awardMatchTieBreakPoint(player, playerA, playerB)
        } else if (setTieBreakMode) {
            awardSetTieBreakPoint(player, playerA, playerB)
        } else {
            awardGamePoint(player, playerA, playerB)
        }

        // Update the saved state handle with the modified players
        navController.previousBackStackEntry?.savedStateHandle?.set("playerA", playerA)
        navController.previousBackStackEntry?.savedStateHandle?.set("playerB", playerB)
    }

    private fun awardGamePoint(player: Player, playerA: Player, playerB: Player) {
        if (player == playerA) {
            playerA.score++
            when {
                playerA.score == 4 && playerB.score < 3 -> awardGame(playerA, playerA, playerB)
                playerA.score == 4 && playerB.score == 3 -> { /* Advantage Player A */ }
                playerA.score == 5 && playerB.score == 3 -> awardGame(playerA, playerA, playerB)
                playerA.score == 4 && playerB.score == 4 -> {
                    playerA.score = 3
                    playerB.score = 3
                }
            }
        } else {
            playerB.score++
            when {
                playerB.score == 4 && playerA.score < 3 -> awardGame(playerB, playerA, playerB)
                playerB.score == 4 && playerA.score == 3 -> { /* Advantage Player B */ }
                playerB.score == 5 && playerA.score == 3 -> awardGame(playerB, playerA, playerB)
                playerB.score == 4 && playerA.score == 4 -> {
                    playerA.score = 3
                    playerB.score = 3
                }
            }
        }

        if (playerA.score == 3 && playerB.score == 3 && setTieBreakEnabled) {
            setTieBreakMode = true
        }
    }

    private fun awardSetTieBreakPoint(player: Player, playerA: Player, playerB: Player) {
        if (player == playerA) {
            playerA.setTieBreakScore
        } else {
            playerB.setTieBreakScore
        }

        if (playerA.setTieBreakScore >= 7 && playerA.setTieBreakScore >= playerB.setTieBreakScore + 2) {
            awardGame(playerA, playerA, playerB)
            awardSet(playerA, playerA, playerB)
            setTieBreakMode = false
            playerA.setTieBreakScore = 0
            playerB.setTieBreakScore = 0
        } else if (playerB.setTieBreakScore >= 7 && playerB.setTieBreakScore >= playerA.setTieBreakScore + 2) {
            awardGame(playerB, playerA, playerB)
            awardSet(playerB, playerA, playerB)
            setTieBreakMode = false
            playerA.setTieBreakScore = 0
            playerB.setTieBreakScore = 0
        }
    }

    private fun awardMatchTieBreakPoint(player: Player, playerA: Player, playerB: Player) {
        if (player == playerA) {
            playerA.matchTieBreakScore++
        } else {
            playerB.matchTieBreakScore++
        }

        if (playerA.matchTieBreakScore >= 10 && playerA.matchTieBreakScore >= playerB.matchTieBreakScore + 2) {
            awardGame(playerA, playerA, playerB)
            awardSet(playerA, playerA, playerB)
            matchTieBreakMode = false
            playerA.matchTieBreakScore = 0
            playerB.matchTieBreakScore = 0
        } else if (playerB.matchTieBreakScore >= 10 && playerB.matchTieBreakScore >= playerA.matchTieBreakScore + 2) {
            awardGame(playerB, playerA, playerB)
            awardSet(playerB, playerA, playerB)
            matchTieBreakMode = false
            playerA.matchTieBreakScore = 0
            playerB.matchTieBreakScore = 0
        }
    }

    private fun awardGame(player: Player, playerA: Player, playerB: Player) {
        if (player == playerA) {
            playerA.gamesWon++
        } else {
            playerB.gamesWon++
        }

        if ((playerA.gamesWon >= 6 && playerA.gamesWon >= playerB.gamesWon + 2) ||
            (setTieBreakMode && player == playerA && playerA.setTieBreakScore >= 7 && playerA.setTieBreakScore >= playerB.setTieBreakScore + 2) ||
            (setTieBreakMode && player == playerB && playerB.setTieBreakScore >= 7 && playerB.setTieBreakScore >= playerA.setTieBreakScore + 2)
        ) {
            awardSet(player, playerA, playerB)
            playerA.gamesWon = 0
            playerB.gamesWon = 0
        }

        if (matchTieBreakEnabled && numberOfSets == 3 && playerA.setsWon == 1 && playerB.setsWon == 1) {
            matchTieBreakMode = true
        } else if (matchTieBreakEnabled && numberOfSets == 5 && playerA.setsWon == 2 && playerB.setsWon == 2) {
            matchTieBreakMode = true
        }

        changeServer(playerA, playerB)
    }

    private fun awardSet(player: Player, playerA: Player, playerB: Player) {
        if (player == playerA) {
            playerA.setsWon
        } else {
            playerB.setsWon
        }
    }

    private fun changeServer(playerA: Player, playerB: Player) {
        currentServer = if (currentServer == playerA) playerB else playerA
        playerA.isServing = !playerA.isServing
        playerB.isServing = !playerB.isServing
    }
}