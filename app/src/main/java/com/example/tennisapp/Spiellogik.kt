package com.example.tennisapp

import androidx.media3.common.Player

object Spiellogik {

    // Game Rules(set in TennisStartScreen)
    var numberOfSets = 3
    var setTieBreakEnabled = true
    var matchTieBreakEnabled = true
    var initialServer: Player? = null

    // Match State
    var currentServer: Player? = null
    var playerA: Player? = null
    var playerB: Player? = null
    var tieBreakScoreA: Int = 0
    var tieBreakScoreB: Int = 0

    // Tiebreak Modes
    var setTieBreakMode = false
    var matchTieBreakMode = falsefun initializeMatch(playerA: Player, playerB: Player) {
        this.playerA = playerA
        this.playerB = playerB
        currentServer = initialServer
        this.playerA.score = 0
        this.playerB.score = 0
        this.playerA.gamesWon = 0
        this.playerB.gamesWon = 0
        this.playerA.setsWon = 0
        this.playerB.setsWon = 0
        this.playerA.setScores = mutableListOf()
        this.playerB.setScores = mutableListOf()
        tieBreakScoreA = 0
        tieBreakScoreB = 0
        setTieBreakMode = false
        matchTieBreakMode = false
    }

    fun awardPoint(player: Player) {
        if (matchTieBreakMode) {
            awardMatchTieBreakPoint(player)
        } else if (setTieBreakMode) {
            awardSetTieBreakPoint(player)
        } else {
            awardGamePoint(player)
        }
    }

    private fun awardGamePoint(player: Player) {
        if (player == playerA) {
            playerA!!.score++
            if (playerA!!.score == 4 && playerB!!.score < 3) {
                awardGame(playerA!!)
            } else if (playerA!!.score == 4 && playerB!!.score == 3) {
                // Advantage Player A
            } else if (playerA!!.score == 5 && playerB!!.score == 3) {
                awardGame(playerA!!)
            } else if (playerA!!.score == 4 && playerB!!.score == 4) {
                // Deuce
                playerA!!.score = 3
                playerB!!.score = 3
            }
        } else {
            playerB!!.score++
            if (playerB!!.score == 4 && playerA!!.score < 3) {
                awardGame(playerB!!)
            } else if (playerB!!.score == 4 && playerA!!.score == 3) {
                // Advantage Player B
            } else if (playerB!!.score == 5 && playerA!!.score == 3) {
                awardGame(playerB!!)
            } else if (playerB!!.score == 4 && playerA!!.score == 4) {
                // Deuce
                playerA!!.score = 3
                playerB!!.score = 3
            }
        }

        // Check for tiebreak
        if (playerA!!.score == 3 && playerB!!.score == 3 && setTieBreakEnabled) {
            setTieBreakMode = true
        }
    }

    private fun awardSetTieBreakPoint(player: Player) {
        if (player == playerA) {
            tieBreakScoreA++
        } else {
            tieBreakScoreB++
        }

        if (tieBreakScoreA >= 7 && tieBreakScoreA >= tieBreakScoreB + 2) {
            awardGame(playerA!!)
            awardSet(playerA!!)
            setTieBreakMode = false
            tieBreakScoreA = 0
            tieBreakScoreB = 0
        } else if (tieBreakScoreB >= 7 && tieBreakScoreB >= tieBreakScoreA + 2) {
            awardGame(playerB!!)
            awardSet(playerB!!)
            setTieBreakMode = false
            tieBreakScoreA = 0
            tieBreakScoreB = 0
        }
    }

    private fun awardMatchTieBreakPoint(player: Player) {
        if (player == playerA) {
            tieBreakScoreA++
        } else {
            tieBreakScoreB++
        }

        if (tieBreakScoreA >= 10 && tieBreakScoreA >= tieBreakScoreB + 2) {
            awardGame(playerA!!)
            awardSet(playerA!!)
            matchTieBreakMode = false
            tieBreakScoreA = 0
            tieBreakScoreB = 0
        } else if (tieBreakScoreB >= 10 && tieBreakScoreB >= tieBreakScoreA + 2) {
            awardGame(playerB!!)
            awardSet(playerB!!)
            matchTieBreakMode = false
            tieBreakScoreA = 0
            tieBreakScoreB = 0
        }
    }

    private fun awardGame(player: Player) {
        if (player == playerA) {
            playerA!!.gamesWon++
            playerA!!.setScores.add(Pair(playerA!!.gamesWon, playerB!!.gamesWon))
            playerB!!.setScores.add(Pair(playerB!!.gamesWon, playerA!!.gamesWon))
        } else {
            playerB!!.gamesWon++
            playerA!!.setScores.add(Pair(playerA!!.gamesWon, playerB!!.gamesWon))
            playerB!!.setScores.add(Pair(playerB!!.gamesWon, playerA!!.gamesWon))
        }
        playerA!!.score = 0
        playerB!!.score = 0

        if ((playerA!!.gamesWon >= 6 && playerA!!.gamesWon >= playerB!!.gamesWon + 2) ||
            (setTieBreakMode && player == playerA && tieBreakScoreA >= 7 && tieBreakScoreA >= tieBreakScoreB + 2) ||
            (setTieBreakMode && player == playerB && tieBreakScoreB >= 7 && tieBreakScoreB >= tieBreakScoreA + 2)
        ) {
            awardSet(player)
            playerA!!.gamesWon = 0
            playerB!!.gamesWon = 0
        }

        if (matchTieBreakEnabled && numberOfSets == 3 && playerA!!.setsWon == 1 && playerB!!.setsWon == 1) {
            matchTieBreakMode = true
        } else if (matchTieBreakEnabled && numberOfSets == 5 && playerA!!.setsWon == 2 && playerB!!.setsWon == 2) {
            matchTieBreakMode = true
        }

        changeServer()
    }

    private fun awardSet(player: Player) {
        if (player == playerA) {
            playerA!!.setsWon++
        } else {
            playerB!!.setsWon++
        }

        if (numberOfSets == 3 && (playerA!!.setsWon == 2 || playerB!!.setsWon == 2)) {
            // Match won!
        } else if (numberOfSets == 5 && (playerA!!.setsWon == 3 || playerB!!.setsWon == 3)) {
            // Match won!
        }
    }

    private fun changeServer() {
        currentServer = if (currentServer == playerA) playerB else playerA
    }
}