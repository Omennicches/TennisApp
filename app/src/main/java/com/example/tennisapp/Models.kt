package com.example.tennisapp

data class Player(
    var name: String,
    var score: Int = 0,
    var gamesWon: Int = 0,
    var setsWon: Int = 0,
    var tiebreakPoints: Int = 0
)