package com.lk.memobar2.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.lk.memobar2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteUIScreen(
    inputText: String,
    appBarTitleResource: Int,
    memoEditActions: IMemoEditActions,
    showButtons: Boolean = true
) {
    val textFieldValue = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(
        TextFieldValue(
            inputText,
            TextRange(inputText.length)
        )
    )}
    val focusRequester = remember { FocusRequester() }
    Scaffold(topBar = {
            TopAppBar(title = {
                Text(stringResource(appBarTitleResource), fontWeight = Themes.AppBarFontWeight)
                },
                colors = Themes.getTopBarColors()
            ) },
            containerColor = Themes.getAppBackgroundColor()
        ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .imePadding()) {
            Column (modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
                OutlinedTextField(
                    value = textFieldValue.value,
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth(1.0f)
                        .weight(1f)
                        .focusRequester(focusRequester),
                    placeholder = { Text(stringResource(R.string.dialog_edit_placeholder)) },
                    onValueChange = { textFieldValue.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Sentences, autoCorrectEnabled = true),
                    shape = RoundedCornerShape(CornerSize(14.dp)),
                    colors = Themes.getOutlinedTextFieldColors()
                )
                if (showButtons) {
                    Row(modifier = Modifier.align(Alignment.End)) {
                        MyButton(stringResource(R.string.dialog_cancel), "", memoEditActions::onCancelListener)
                        MyButton(
                            stringResource(R.string.dialog_edit_yes),
                            textFieldValue.value.text,
                            memoEditActions::onClickListener
                        )
                    }
                }
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            }

        }
    }
}

@Composable
fun MyButton(text: String, input: String, listener: (String) -> Unit) {
    FilledTonalButton (
        onClick = { listener(input) },
        modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 0.dp),
    ) {
        Text(text)
    }
}
