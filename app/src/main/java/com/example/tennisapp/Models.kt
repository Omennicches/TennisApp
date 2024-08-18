package com.example.tennisapp

data class Player(
    val name: String,
    var score: Int,
    var setScores: MutableList<Pair<Int, Int>> = mutableListOf(), // Liste von Paaren (z.B. Satzpunktestand)
    var gamesWon: Int,
    var setsWon: Int,
    var tiebreakPoints: Int = 0,
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