package com.example.tennisapp

data class Player(
    val name: String,
    var score: Int,
    var gamesWon:List<Int>,
    var setsWon: List<Int>,
    var setTieBreakScore: List<Int>, // New property for set tie-break score
    var matchTieBreakScore: Int = 0, // New property for match tie-break score
    var isServing: Boolean = false
)
enum class PointStatus(val display: String) {
    LOVE("0"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40"),
    ADVANTAGE("AD"), DEUCE("40")
}


fun getPointStatus(score: Int): String {
    return when (score) {
        0 -> PointStatus.LOVE.display
        1 -> PointStatus.FIFTEEN.display
        2 -> PointStatus.THIRTY.display
        3 -> PointStatus.FORTY.display
        4 -> PointStatus.ADVANTAGE.display
        else -> "?"
    }
}


data class MatchSettings(
    val matchTieBreak: Boolean,
    val selectedSets: Int,
    val selectedSurface: String,
    val setTieBreak: Boolean
)