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

@Composable
fun TennisStartScreen() {
    // Zustand für die Auswahl
    var selectedSets by remember { mutableStateOf(1) }
    var selectedServer by remember { mutableStateOf("Spieler A") }
    var matchtieBreak by remember { mutableStateOf(true) }
    var matchStarted by remember { mutableStateOf(false) }
    var selectedSurface by remember { mutableStateOf("Sand") }
    var setTieBreak by remember { mutableStateOf(true) }

    val backgroundImage = when (selectedSurface) {
        "Sand" -> R.drawable.tennisplatz_clay
        "Grass" -> R.drawable.tennisplatz_grass
        "Hartplatz" -> R.drawable.tennisplatz_compund_ace
        else -> R.drawable.tennisplatz_clay
    }

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
                SelectionButton(label = "Spieler A", selected = selectedServer == "Spieler A") { selectedServer = "Spieler A" }
                Spacer(modifier = Modifier.width(20.dp))
                SelectionButton(label = "Spieler B", selected = selectedServer == "Spieler B") { selectedServer = "Spieler B" }
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
//Baut die Selection Buutons
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TennisAppTheme {
        TennisStartScreen()
    }
}
