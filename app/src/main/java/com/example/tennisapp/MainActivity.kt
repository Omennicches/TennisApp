package com.example.tennisapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tennisapp.ui.theme.TennisAppTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TennisAppTheme {
                TennisStartScreen()
            }
        }
    }
}
data class Player(
    var name: String,
    var score: Int = 0,
    var gamesWon: Int = 0,
    var setsWon: Int = 0,
    var tiebreakPoints: Int = 0
)

//Baut den Screen in dem der Spielstand erfasst wird
@Composable
fun TennisStartScreen() {
    // Zustand für die Auswahl
    var selectedSets by remember { mutableIntStateOf(1) }
    var selectedServer by remember { mutableStateOf("Spieler A") }
    var matchtieBreak by remember { mutableStateOf(true) }
    var matchStarted by remember { mutableStateOf(false) }
    var selectedSurface by remember { mutableStateOf("Sand") }
    var setTieBreak by remember { mutableStateOf(true) }

    // Player Variables
    var playerA by remember { mutableStateOf(Player("Spieler A")) }
    var playerB by remember { mutableStateOf(Player("Spieler B")) }

    val backgroundImage = when (selectedSurface) {
        "Sand" -> R.drawable.tennisplatz_clay
        "Grass" -> R.drawable.tennisplatz_grass
        "Hartplatz" -> R.drawable.tennisplatz_compund_ace
        else -> R.drawable.tennisplatz_clay
    }

    if (matchStarted) {
        MatchScreen(
            playerA = playerA,
            playerB = playerB,
            selectedSurface = selectedSurface,
            setTieBreak = setTieBreak,
            selectedServer = selectedServer,
            matchtieBreak = matchtieBreak,
            onBack = { matchStarted = false }
        )
    } else {
        // Layout
        Box(modifier = Modifier.fillMaxSize()) {

            // Hintergrundbild
            Image(
                painter = painterResource(id = backgroundImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Untergrund
                Text("Untergrund:", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Row {
                    SelectionButton(label = "Sand", selected = selectedSurface == "Sand") { selectedSurface = "Sand" }
                    Spacer(modifier = Modifier.width(16.dp))
                    SelectionButton(label = "Grass", selected = selectedSurface == "Grass") { selectedSurface = "Grass" }
                    Spacer(modifier = Modifier.width(16.dp))
                    SelectionButton(label = "Hartplatz", selected = selectedSurface == "Hartplatz") { selectedSurface = "Hartplatz" }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Auswahl der Anzahl der Sätze
                Text("Anzahl der Sätze:", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Row {
                    SelectionButton(label = "1", selected = selectedSets == 1) { selectedSets = 1 }
                    Spacer(modifier = Modifier.width(16.dp))
                    SelectionButton(label = "3", selected = selectedSets == 3) { selectedSets = 3 }
                    Spacer(modifier = Modifier.width(16.dp))
                    SelectionButton(label = "5", selected = selectedSets == 5) { selectedSets = 5 }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Auswahl für Match Tie-Break
                Text("Match Tie Break:", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Row {
                    SelectionButton(label = "Ja", selected = matchtieBreak) { matchtieBreak = true }
                    Spacer(modifier = Modifier.width(16.dp))
                    SelectionButton(label = "Nein", selected = !matchtieBreak) { matchtieBreak = false }
                }

                Spacer(modifier = Modifier.height(10.dp))

                //Auswahl Satz Tie Break
                Text("Satz Tiebreak:", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Row {
                    SelectionButton(label = "Ja", selected = setTieBreak) { setTieBreak = true }
                    Spacer(modifier = Modifier.width(16.dp))
                    SelectionButton(label = "Nein", selected = !setTieBreak) { setTieBreak = false }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Auswahl des Aufschlägers
                Text("Aufschläger:", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Row {
                    SelectionButton(label = playerA.name, selected = selectedServer == playerA.name) { selectedServer = playerA.name }
                    Spacer(modifier = Modifier.width(20.dp))
                    SelectionButton(label = playerB.name, selected = selectedServer == playerB.name) { selectedServer = playerB.name }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Start Match Button
                Button(
                    onClick = { matchStarted = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .clip(CircleShape),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) {
                    Text("Match Starten!", fontSize = 35.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

//Baut die Selection Buttons
@Composable
fun SelectionButton(label: String, selected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondaryContainer
    val textColor = if (selected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSecondaryContainer

    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        val density = LocalDensity.current
        val fontSize = with(density) {
            // Calculate the font size based on the label length
            // Maximum font size is 20.sp, minimum font size is 10.sp
            (80 / label.length).dp.toSp().value.coerceIn(15f, 25f).sp
        }
        Text(
            text = label,
            color = textColor,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            maxLines = 2,  // Allow up to 2 lines of text
            textAlign = TextAlign.Center,
            softWrap = true, //enables text wrapping without breaking words
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 1.dp)
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
    }
}

// MatchScreen composable function
@Composable
fun MatchScreen(
    playerA: Player,
    playerB: Player,
    selectedSurface: String,
    setTieBreak: Boolean,
    selectedServer: String,
    matchtieBreak: Boolean,
    onBack: () -> Unit
) {
    val backgroundColor =
        if (playerA.score > playerB.score) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondaryContainer
    val server = if (playerA.name == selectedServer) playerA else playerB
    val receiver = if (server == playerA) playerB else playerA
    val backgroundImage = when (selectedSurface) {
        "Sand" -> R.drawable.tennisplatz_clay
        "Grass" -> R.drawable.tennisplatz_grass
        "Hartplatz" -> R.drawable.tennisplatz_compund_ace
        else -> R.drawable.tennisplatz_clay
    }

    // State to control the visibility and selection of buttons
    var showPointButtons by remember { mutableStateOf(false) }
    var selectedPlayer by remember { mutableStateOf<Player?>(null) }
    val selectedPoint = remember { mutableStateOf<String?>(null) }
    val selectedStroke = remember { mutableStateOf<String?>(null) }
    val selectedForm = remember { mutableStateOf<String?>(null) }

    // Reset showPointButtons state when selectedPlayer is null
    LaunchedEffect(selectedPlayer) {
        if (selectedPlayer == null) {
            showPointButtons = false
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
        // Hintergrundbild
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Back Button
        IconButton(
            onClick = { onBack() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Zurück",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Spieler A
            PlayerScoreCard(player = playerA, isServing = playerA.name == selectedServer)

            // Spieler B
            PlayerScoreCard(player = playerB, isServing = playerB.name == selectedServer)

            Spacer(modifier = Modifier.height(80.dp))

            // Spieler Punktet Buttons
            PlayerPointButtons(
                playerA = playerA,
                playerB = playerB,
                selectedPlayer = selectedPlayer,
                onSelectPlayer = { player ->
                    // Handle player selection
                    selectedPlayer = player
                    showPointButtons = true
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Punktet Buttons (bedingte Anzeige)
            if (showPointButtons && selectedPlayer != null) {
                PointButtons(
                    selectedPlayer = selectedPlayer,
                    server = server,
                    opponent = if (server == playerA) playerB else playerA,
                    isTiebreak = setTieBreak,
                    onSelectedPlayerChange = { selectedPlayer = it },
                    showPointButtons = showPointButtons,  // Pass the state reference
                    selectedPoint = selectedPoint,  // Pass the state reference
                    selectedStroke = selectedStroke,  // Pass the state reference
                    selectedForm = selectedForm  // Pass the state reference
                )
            }
        }
    }
}



@Composable
fun PlayerScoreCard(player: Player, isServing: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface)
            .clip(CircleShape)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = player.name,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = player.score.toString(),
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (isServing) {
            Icon(
                painter = painterResource(id = R.drawable.tennisball),
                contentDescription = "Serving",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun PlayerPointButtons(
    playerA: Player,
    playerB: Player,
    selectedPlayer: Player?,
    onSelectPlayer: (Player) -> Unit
) {
    // State zur Speicherung des aktuell ausgewählten Buttons
    val selectedButton = remember { mutableStateOf<Player?>(null) }

    // Funktion zum Aktualisieren der Auswahl
    val handleButtonClick: (Player) -> Unit = { player ->
        selectedButton.value = player
        onSelectPlayer(player)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),  // Padding für die ganze Zeile
        horizontalArrangement = Arrangement.Center
    ) {
        listOf(playerA, playerB).forEach { player ->
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(120.dp)  // Setze die Größe des Buttons
                    .background(
                        color = if (selectedButton.value == player)
                            MaterialTheme.colorScheme.tertiary
                        else
                            MaterialTheme.colorScheme.tertiaryContainer
                    )
                    .clickable {
                        handleButtonClick(player)
                    },
                contentAlignment = Alignment.Center
            ) {
                val density = LocalDensity.current
                val fontSize = with(density) {
                    val maxFontSize = 40.sp.toPx()  // Maximum Schriftgröße in Pixel
                    val minFontSize = 15.sp.toPx()  // Minimum Schriftgröße in Pixel
                    val textLength = player.name.length
                    val buttonSize = 80.dp.toPx()
                    val sizeRatio = buttonSize / textLength
                    (sizeRatio / 2).coerceIn(minFontSize, maxFontSize).toSp()
                }

                Text(
                    text = "${player.name} Punktet",
                    color = if (selectedButton.value == player)
                        MaterialTheme.colorScheme.onTertiary
                    else
                        MaterialTheme.colorScheme.onTertiaryContainer,
                    fontSize = fontSize,  // Setze die Schriftgröße automatisch
                    fontWeight = FontWeight.Bold,
                    maxLines = 3,  // Erlaube bis zu 3 Zeilen Text
                    textAlign = TextAlign.Center,
                    softWrap = true, // Erlaubt Textumbruch ohne Worttrennung
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
        }
    }

    // Reset the state when ConfirmButton is pressed
    LaunchedEffect(selectedPlayer) {
        if (selectedPlayer == null) {
            selectedButton.value = null
        }
    }
}


@Composable
fun PointButtons(
    selectedPlayer: Player?,
    server: Player,
    opponent: Player,
    isTiebreak: Boolean,
    onSelectedPlayerChange: (Player?) -> Unit,
    showPointButtons: Boolean,
    selectedPoint: MutableState<String?>,
    selectedStroke: MutableState<String?>,
    selectedForm: MutableState<String?>
) {
    // Punkte-Buttons, die angezeigt werden sollen
    val allPoints = listOf("Ass", "Netz Fehler", "Aus", "Doppel Aufpraller")
    val regularPoints = listOf("Aufschlagsfehler", "Netz Fehler", "Aus", "Doppel Aufpraller")

    // State zur Speicherung des ausgewählten Punktes
    // selectedPoint is already provided via parameter

    // Überprüfen, ob der ausgewählte Spieler der Aufschläger ist
    val pointsToDisplay = if (selectedPlayer == server) {
        allPoints
    } else {
        regularPoints
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),  // Padding für die ganze Zeile
        horizontalArrangement = Arrangement.Center
    ) {
        pointsToDisplay.forEach { point ->
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(80.dp)  // Setze die Größe des Buttons
                    .background(
                        color = if (selectedPoint.value == point) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondaryContainer
                    )
                    .clickable {
                        selectedPoint.value = point
                    },
                contentAlignment = Alignment.Center
            ) {
                val density = LocalDensity.current
                val fontSize = with(density) {
                    // Berechne die Schriftgröße basierend auf der Button-Größe
                    (80 / point.length).dp.toSp().value.coerceIn(12f, 20f).sp
                }

                Text(
                    text = point,
                    color = if (selectedPoint.value == point) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,  // Erlaube bis zu 2 Zeilen Text
                    textAlign = TextAlign.Center,
                    softWrap = true, // Erlaubt Textumbruch ohne Worttrennung
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
        }
    }

    // Überprüfen, ob der Bestätigungsbutton angezeigt werden soll
    if (selectedPoint.value == "Ass" || selectedPoint.value == "Aufschlagsfehler") {
        ConfirmButton(
            selectedPlayer,
            server,
            opponent,
            isTiebreak,
            onSelectedPlayerChange,
            showPointButtons,
            selectedPoint,
            selectedStroke,
            selectedForm
        )
    } else if (selectedPoint.value in listOf("Netz Fehler", "Aus", "Doppel Aufpraller")) {
        StrokeButtons(
            onStrokeSelect = { stroke ->
                // Handle stroke selection
                println("Selected Stroke: $stroke")
            },
            selectedPlayer = selectedPlayer,
            server = server,
            opponent = opponent,
            isTiebreak = isTiebreak,
            onSelectedPlayerChange = onSelectedPlayerChange,
            showPointButtons = showPointButtons,
            selectedPoint = selectedPoint,
            selectedStroke = selectedStroke,
            selectedForm = selectedForm
        )
    }
}


@Composable
fun StrokeButtons(
    onStrokeSelect: (String) -> Unit,
    selectedPlayer: Player?,
    server: Player,
    opponent: Player,
    isTiebreak: Boolean,
    onSelectedPlayerChange: (Player?) -> Unit,
    showPointButtons: Boolean,
    selectedPoint: MutableState<String?>,
    selectedStroke: MutableState<String?>,
    selectedForm: MutableState<String?>
) {
    // State für den ausgewählten Stroke innerhalb der StrokeButtons
    // selectedStroke is already provided via parameter

    val strokeOptions = listOf("Vorhand", "Rückhand")

    // Stroke Buttons Row
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp) // Mehr Abstand zu den Buttons oben
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        strokeOptions.forEach { stroke ->
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(80.dp)
                    .background(
                        color = if (selectedStroke.value == stroke) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondaryContainer
                    )
                    .clickable {
                        // Stroke auswählen
                        selectedStroke.value = stroke
                        onStrokeSelect(stroke)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stroke,
                    color = if (selectedStroke.value == stroke) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    // Check if FormButtons should be displayed based on the selected stroke
    if (selectedStroke.value != null) {
        FormButtons(
            onFormSelect = { form ->
                // Handle form selection
                println("Selected Form: $form")
            },
            selectedPlayer = selectedPlayer,
            server = server,
            opponent = opponent,
            isTiebreak = isTiebreak,
            onSelectedPlayerChange = onSelectedPlayerChange,
            showPointButtons = showPointButtons,
            selectedPoint = selectedPoint,
            selectedStroke = selectedStroke,
            selectedForm = selectedForm
        )
    }
}


@Composable
fun FormButtons(
    onFormSelect: (String) -> Unit,
    selectedPlayer: Player?,
    server: Player,
    opponent: Player,
    isTiebreak: Boolean,
    onSelectedPlayerChange: (Player?) -> Unit,
    showPointButtons: Boolean,
    selectedPoint: MutableState<String?>,
    selectedStroke: MutableState<String?>,
    selectedForm: MutableState<String?>
) {
    // State für die ausgewählte Form-Option
    // selectedForm is already provided via parameter

    val formOptions = listOf("Formfehler Ja", "Formfehler Nein")

    // Container, der sowohl die Form-Buttons als auch den Confirm-Button enthält
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween // Verteile den Platz zwischen den Buttons
    ) {
        // Form Buttons Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp), // Padding für die Zeile
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            formOptions.forEach { form ->
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(80.dp)
                        .background(
                            color = if (selectedForm.value == form) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondaryContainer
                        )
                        .clickable {
                            // Form auswählen
                            selectedForm.value = form
                            onFormSelect(form)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = form,
                        color = if (selectedForm.value == form) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSecondaryContainer,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        textAlign = TextAlign.Center,
                        softWrap = true, // Erlaubt Textumbruch ohne Worttrennung
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    )
                }
            }
        }

        // Bestätigungsbutton immer am unteren Rand der Column
        if (selectedForm.value != null) {
            ConfirmButton(
                selectedPlayer,
                server,
                opponent,
                isTiebreak,
                onSelectedPlayerChange,
                showPointButtons,
                selectedPoint,
                selectedStroke,
                selectedForm
            )
        }
    }
}


@Composable
fun ConfirmButton(
    selectedPlayer: Player?,
    server: Player,
    opponent: Player,
    isTiebreak: Boolean,
    onSelectedPlayerChange: (Player?) -> Unit,
    showPointButtons: Boolean,
    selectedPoint: MutableState<String?>,
    selectedStroke: MutableState<String?>,
    selectedForm: MutableState<String?>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 1.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
            onClick = {
                // Vergeben des Punktes an den ausgewählten Spieler
                if (selectedPlayer != null) {
                    addPointToPlayer(selectedPlayer, opponent, selectedPlayer == server, isTiebreak)
                }
                // Zurücksetzen des ausgewählten Spielers und UI-States
                onSelectedPlayerChange(null)
                selectedPoint.value = null
                selectedStroke.value = null
                selectedForm.value = null
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 1.dp)
                .clip(CircleShape),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )
        ) {
            Text("Bestätigen", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}


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
