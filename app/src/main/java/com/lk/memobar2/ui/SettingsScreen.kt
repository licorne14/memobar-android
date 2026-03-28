package com.lk.memobar2.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lk.memobar2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SettingsScreen () {
    Scaffold(
        topBar = {
            TopAppBar (
                title = { Text(stringResource(R.string.settings))},
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(painterResource(R.drawable.ic_arrow_back), stringResource(R.string.dialog_cancel))
                    }
                },
                colors = Themes.getTopBarColors()
            )
        },
        containerColor = Themes.getAppBackgroundColor()
    ) { paddingValues ->
        var uiCards by remember { mutableStateOf(true) }
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(modifier = Modifier.padding(16.dp)) {
                SettingsRow(label = stringResource(R.string.settings_theme)) {
                    SingleChoiceSegmentedButton()
                }
                SettingsRow(label = stringResource(R.string.settings_ui_cards)) {
                    Switch(checked = uiCards, onCheckedChange = { uiCards = it } )
                }
                Card (modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Themes.getListCardColor())) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(6.dp)) {
                        Icon(painterResource(R.drawable.ic_warning), contentDescription = "Attention",
                            modifier = Modifier.padding(end = 8.dp, start = 8.dp), tint = MaterialTheme.colorScheme.primary)
                        Text("These settings aren't functional yet.")
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsRow(label: String, action: @Composable (RowScope.() -> Unit)) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.weight(1f))
        action()
    }
}

@Composable
fun SingleChoiceSegmentedButton(modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Light", "Auto", "Dark")

    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = { selectedIndex = index },
                selected = index == selectedIndex,
                label = { Text(label) }
            )
        }
    }
}