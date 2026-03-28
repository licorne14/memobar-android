package com.lk.memobar2.devtests

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lk.memobar2.R


private val TAG = "EditUI"

@Preview
@Composable
fun RenderMainUI() {
    val messages = listOf("Test", "Test 2", "Test 3", "Test 4\nTest 5", "Hallo")
    var text by remember { mutableStateOf("Hello") }
    var textSimple by remember { mutableStateOf("Start") }
    Column() {
        /*Card (modifier = Modifier.padding(8.dp).fillMaxWidth()) {
            Text("Compose test area - empty", modifier = Modifier.padding(16.dp))
        }*/
        ColorSchemeCard()
        Card (modifier = Modifier.padding(8.dp).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)) {
            Text("Just your regular card", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onSurface)
        }
        //FullAppEditView("This is a note")
        //ListNoteUI(messages)
        //EditNoteUI(PaddingValues(8.dp))
        // TestUI(text, textSimple, messages)
    }
}

@Composable
fun ColorSchemeCard() {
    OutlinedCard(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
        Text("surface - surfaceContainer - surfaceContainerLow - surfaceContainerHigh", modifier = Modifier.padding(4.dp))
        ColorRowFour(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surfaceContainer,
            MaterialTheme.colorScheme.surfaceContainerLow, MaterialTheme.colorScheme.surfaceContainerHigh)
        Text("onSurface - onSurfaceVariant - surfaceVariant - surfaceContainerLowest", modifier = Modifier.padding(4.dp))
        ColorRowFour(MaterialTheme.colorScheme.onSurface, MaterialTheme.colorScheme.onSurfaceVariant,
            MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.surfaceContainerLowest)
        Text("primaryFixed - primaryFixedDim - surfaceTint - surfaceBright", modifier = Modifier.padding(4.dp))
        ColorRowFour(MaterialTheme.colorScheme.primaryFixed, MaterialTheme.colorScheme.primaryFixedDim,
            MaterialTheme.colorScheme.surfaceTint, MaterialTheme.colorScheme.surfaceBright)
        Text("primary - primaryContainer - secondary - secondaryContainer", modifier = Modifier.padding(4.dp))
        ColorRowFour(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.secondaryContainer)
        Text("onPrimary - onPrimaryContainer - onSecondary - onSecondaryContainer", modifier = Modifier.padding(4.dp))
        ColorRowFour(MaterialTheme.colorScheme.onPrimary, MaterialTheme.colorScheme.onPrimaryContainer,
            MaterialTheme.colorScheme.onSecondary, MaterialTheme.colorScheme.onSecondaryContainer)
        /*Text("onTert - onTertContainer - onTertFixed - onTertFixedVariant")
        ColorRowFour(MaterialTheme.colorScheme.onTertiary, MaterialTheme.colorScheme.onTertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryFixed, MaterialTheme.colorScheme.onTertiaryFixedVariant)*/
    }
}

/*
* Call with:
* ColorRowFour(MaterialTheme.colorScheme, MaterialTheme.colorScheme,
                MaterialTheme.colorScheme, MaterialTheme.colorScheme)
* */
@Composable
fun ColorRowFour(color1: Color, color2: Color, color3: Color, color4: Color) {
    Row (modifier = Modifier.padding(8.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Box(modifier = Modifier.background(color1).padding(4.dp).weight(0.25f).height(16.dp))
        Box(modifier = Modifier.background(color2).padding(4.dp).weight(0.25f).height(16.dp))
        Box(modifier = Modifier.background(color3).padding(4.dp).weight(0.25f).height(16.dp))
        Box(modifier = Modifier.background(color4).padding(4.dp).weight(0.25f).height(16.dp))
    }
}

@Composable
fun ColorBox(color: Color) {
    Box(modifier = Modifier.background(color).padding(4.dp).fillMaxWidth(0.25f).height(16.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullAppEditView(note: String) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Edit") }, navigationIcon = {
                Icon(painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "Back")
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onClick(note) } ) {
                Icon(painter = painterResource(R.drawable.ic_save_note),
                    contentDescription = stringResource(R.string.dialog_edit_yes)
                )
            }
        },
        bottomBar = {
            BottomAppBar {
                Row {
                    MyButton("Abbrechen", "")
                    MyButton("Speichern", "")
                }
            }
        },
        modifier = Modifier.fillMaxHeight(0.4f)
    ) { paddingValues ->
        EditNoteUI(paddingValues, showButtons = false)
    }
}

@Composable
private fun TestUI(text: String, textSimple: String, messages: List<String> ) {
    var text1 = text
    var textSimple1 = textSimple
    Column(modifier = Modifier.padding(8.dp)) {
        OutlinedTextField(
            value = text1,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(0.5f),
            onValueChange = { text1 = it },
            label = { Text("Label") }
        )
        BasicTextField(
            value = textSimple1,
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.padding(8.dp),
            onValueChange = { textSimple1 = it }
        )
        TextList(messages)
        Row {
            MyButton("Speichern simpel", textSimple1)
            MyButton("Speichern", text1)
        }
    }
}

@Composable
fun EditNoteUI(paddingValues: PaddingValues, showButtons: Boolean = true){
    var text by remember { mutableStateOf("Hello") }
    Column(modifier = Modifier
        .padding(paddingValues)
        .fillMaxHeight(0.3f)) {
        OutlinedTextField(
            value = text,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(1.0f)
                .weight(1f),
            onValueChange = { text = it },
            // label = { Text("Notiz") }
        )
        if(showButtons) {
            Row(modifier = Modifier.align(Alignment.End)) {
                MyButton("Abbrechen", "")
                MyButton("Speichern", text)
            }
        }

    }
}

@Composable
fun ListNoteUI(messages: List<String> ) {
    Column(modifier = Modifier.padding(8.dp)) {
        TextList(messages)
    }
}

@Composable
fun TextList(messages: List<String>) {
    var checkedBox by remember { mutableStateOf(false) }
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(messages) { message ->
            Row {
                Text(
                    message,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
                Checkbox(checked = checkedBox, onCheckedChange = { checkedBox = it } )
            }
        }
    }
}

fun onCheck(newValue: Boolean, checkedBox: Boolean, message: String) {
    val textConversion = if(newValue) { "checked" } else { "unchecked" }
    Log.d(TAG, "onCheck: $message was $textConversion")
}

@Composable
fun MyButton(text: String, input: String) {
    Button(
        onClick = { onClick(input) },
        modifier = Modifier.padding(12.dp)
    ) {
        Text(text)
    }
}

private fun onClick(input: String) {
    Log.d(TAG, "onClick: clicked button and saved: $input")
}

