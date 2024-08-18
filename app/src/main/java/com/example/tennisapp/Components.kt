package com.example.tennisapp

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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            // Berechne die Schriftgröße mit einem Minimum und Maximum
            val calculatedFontSize = (80 / label.length).dp.toSp().value
            calculatedFontSize.coerceIn(15f, 20f).sp
        }
        Text(
            text = label,
            color = textColor,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            textAlign = TextAlign.Center,
            softWrap = true,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 1.dp)
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
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
                if (selectedPlayer != null) {
                    addPointToPlayer(selectedPlayer, opponent, selectedPlayer == server, isTiebreak, false, false, 2, 1)  // Parameter anpassen
                }
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
