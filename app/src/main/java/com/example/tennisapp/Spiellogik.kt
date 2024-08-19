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
        val playerA =
            navController.previousBackStackEntry?.savedStateHandle?.get<Player>("playerA")!!
        val playerB =
            navController.previousBackStackEntry?.savedStateHandle?.get<Player>("playerB")!!

        currentServer = initialServer
        playerA.score = 0
        playerB.score = 0
        playerA.gamesWonCurrentSet = 0
        playerB.gamesWonCurrentSet = 0
        playerA.currentSetsWon = 0
        playerB.currentSetsWon = 0
        playerA.currentTieBreakScore = 0
        playerB.currentTieBreakScore = 0
        playerA.currentMatchTieBreakScore = 0
        playerB.currentMatchTieBreakScore = 0
        playerA.gamesWon = emptyList()
        playerB.gamesWon = emptyList()
        playerA.setsWon = emptyList()
        playerB.setsWon = emptyList()
        playerA.setTieBreakScore = emptyList()
        playerB.setTieBreakScore = emptyList()
        playerA.matchTieBreakScore = emptyList()
        playerB.matchTieBreakScore = emptyList()
        setTieBreakMode = false
        matchTieBreakMode = false
    }

    fun awardPoint(player: Player, navController: NavController) {
        val playerA =
            navController.previousBackStackEntry?.savedStateHandle?.get<Player>("playerA")!!
        val playerB =
            navController.previousBackStackEntry?.savedStateHandle?.get<Player>("playerB")!!

        if (matchTieBreakMode) {
            awardMatchTieBreakPoint(player, playerA, playerB)
        } else if (setTieBreakMode) {
            awardSetTieBreakPoint(player, playerA, playerB)
        } else {
            awardGamePoint(player, playerA, playerB)
        }

        navController.previousBackStackEntry?.savedStateHandle?.set("playerA", playerA)
        navController.previousBackStackEntry?.savedStateHandle?.set("playerB", playerB)
    }

    private fun awardGamePoint(player: Player, playerA: Player, playerB: Player) {
        if (player == playerA) {
            playerA.score++
        } else {
            playerB.score++
        }

        when {
            playerA.score == 4 && playerB.score < 3 -> awardGame(playerA, playerA, playerB)
            playerA.score == 4 && playerB.score == 3 -> { /* Advantage Player A */
            }

            playerA.score == 5 && playerB.score == 3 -> awardGame(playerA, playerA, playerB)
            playerA.score == 4 && playerB.score == 4 -> {
                playerA.score = 3
                playerB.score = 3
            }

            playerB.score == 4 && playerA.score < 3 -> awardGame(playerB, playerA, playerB)
            playerB.score == 4 && playerA.score == 3 -> { /* Advantage Player B */
            }

            playerB.score == 5 && playerA.score == 3 -> awardGame(playerB, playerA, playerB)
            playerB.score == 4 && playerA.score == 4 -> {
                playerA.score = 3
                playerB.score = 3
            }
        }

        if (playerA.score == 3 && playerB.score == 3 && setTieBreakEnabled) {
            setTieBreakMode = true
        }
    }

    private fun awardSetTieBreakPoint(player: Player, playerA: Player, playerB: Player) {
        if (player == playerA) {
            playerA.currentTieBreakScore++
        } else {
            playerB.currentTieBreakScore++
        }

        if (playerA.currentTieBreakScore >= 7 && playerA.currentTieBreakScore >= playerB.currentTieBreakScore + 2) {
            awardGame(playerA, playerA, playerB)
            awardSet(playerA, playerA, playerB)
            setTieBreakMode = false
            playerA.setTieBreakScore = playerA.setTieBreakScore + playerA.currentTieBreakScore
            playerB.setTieBreakScore = playerB.setTieBreakScore + playerB.currentTieBreakScore
            playerA.currentTieBreakScore = 0
            playerB.currentTieBreakScore = 0
        } else if (playerB.currentTieBreakScore >= 7 && playerB.currentTieBreakScore >= playerA.currentTieBreakScore + 2) {
            awardGame(playerB, playerA, playerB)
            awardSet(playerB, playerA, playerB)
            setTieBreakMode = false
            playerA.setTieBreakScore = playerA.setTieBreakScore + playerA.currentTieBreakScore
            playerB.setTieBreakScore = playerB.setTieBreakScore + playerB.currentTieBreakScore
            playerA.currentTieBreakScore = 0
            playerB.currentTieBreakScore = 0
        }
    }

    private fun awardMatchTieBreakPoint(player: Player, playerA: Player, playerB: Player) {
        if (player == playerA) {
            playerA.currentMatchTieBreakScore++
        } else {
            playerB.currentMatchTieBreakScore++
        }

        if (playerA.currentMatchTieBreakScore >= 10 && playerA.currentMatchTieBreakScore >= playerB.currentMatchTieBreakScore + 2) {
            awardGame(playerA, playerA, playerB)
            awardSet(playerA, playerA, playerB)
            matchTieBreakMode = false
            playerA.matchTieBreakScore =
                playerA.matchTieBreakScore + playerA.currentMatchTieBreakScore
            playerB.matchTieBreakScore =
                playerB.matchTieBreakScore + playerB.currentMatchTieBreakScore
            playerA.currentMatchTieBreakScore = 0
            playerB.currentMatchTieBreakScore = 0
        } else if (playerB.currentMatchTieBreakScore >= 10 && playerB.currentMatchTieBreakScore >= playerA.currentMatchTieBreakScore + 2) {
            awardGame(playerB, playerA, playerB)
            awardSet(playerB, playerA, playerB)
            matchTieBreakMode = false
            playerA.matchTieBreakScore =
                playerA.matchTieBreakScore + playerA.currentMatchTieBreakScore
            playerB.matchTieBreakScore =
                playerB.matchTieBreakScore + playerB.currentMatchTieBreakScore
            playerA.currentMatchTieBreakScore = 0
            playerB.currentMatchTieBreakScore = 0
        }
    }

    private fun awardGame(player: Player, playerA: Player, playerB: Player) {
        if (player == playerA) {
            playerA.gamesWonCurrentSet++
        } else {
            playerB.gamesWonCurrentSet++
        }

        if ((playerA.gamesWonCurrentSet >= 6 && playerA.gamesWonCurrentSet >= playerB.gamesWonCurrentSet + 2) ||
            (setTieBreakMode && player == playerA && playerA.currentTieBreakScore >= 7 && playerA.currentTieBreakScore >= playerB.currentTieBreakScore + 2) ||
            (setTieBreakMode && player == playerB && playerB.currentTieBreakScore >= 7 && playerB.currentTieBreakScore >= playerA.currentTieBreakScore + 2)
        ) {
            awardSet(player, playerA, playerB)
        }

        if (matchTieBreakEnabled && numberOfSets == 3 && playerA.currentSetsWon == 1 && playerB.currentSetsWon == 1) {
            matchTieBreakMode = true
        } else if (matchTieBreakEnabled && numberOfSets == 5 && playerA.currentSetsWon == 2 && playerB.currentSetsWon == 2) {
            matchTieBreakMode = true
        }

        playerA.score = 0
        playerB.score = 0

        changeServer(playerA, playerB)
    }

    private fun awardSet(player: Player, playerA: Player, playerB: Player) {
        if (player == playerA) {
            playerA.currentSetsWon++
        } else {
            playerB.currentSetsWon++
        }

        playerA.gamesWon = playerA.gamesWon + playerA.gamesWonCurrentSet
        playerB.gamesWon = playerB.gamesWon + playerB.gamesWonCurrentSet
        playerA.gamesWonCurrentSet = 0
        playerB.gamesWonCurrentSet = 0

        if ((playerA.currentSetsWon >= (numberOfSets / 2) + 1) ||
            (playerB.currentSetsWon >= (numberOfSets / 2) + 1)
        ) {
            // Match finished
            playerA.setsWon = playerA.setsWon + playerA.currentSetsWon
            playerB.setsWon = playerB.setsWon + playerB.currentSetsWon
            playerA.currentSetsWon =0
            playerB.currentSetsWon = 0
        }
    }

    private fun changeServer(playerA: Player, playerB: Player) {
        currentServer = if (currentServer == playerA) playerB else playerA
        playerA.isServing = !playerA.isServing
        playerB.isServing = !playerB.isServing
    }
}