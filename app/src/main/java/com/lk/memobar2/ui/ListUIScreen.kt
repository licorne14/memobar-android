package com.lk.memobar2.ui

import android.util.Log
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lk.memobar2.R
import com.lk.memobar2.database.MemoEntity
import com.lk.memobar2.devtests.ColorSchemeCard
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListUIScreen (
    memoListActions: IMemoListActions,
    memosState: StateFlow<List<MemoEntity>>,
    openSettings: () -> Unit = {}
) {
    val memos by memosState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var showColorCard by rememberSaveable { mutableStateOf(false) }
    var showListCards by rememberSaveable { mutableStateOf(true) }

    Scaffold (
        topBar = {
            TopAppBar(title = {
                    Text(stringResource(R.string.app_name), fontWeight = Themes.AppBarFontWeight)
                },
                scrollBehavior = scrollBehavior,
                colors = Themes.getTopBarColors(),
                actions = {
                    OverflowMenu(showColorCard, onCardVisibilityChanged = { showColorCard = !showColorCard },
                        showListCards, onListCardVisChanged = {showListCards = !showListCards}, onSettings = openSettings)
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton (onClick = { memoListActions.onNewListener() } ) {
                Icon(painterResource(R.drawable.ic_add_memo),stringResource(R.string.dialog_edit_yes))
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = Themes.getAppBackgroundColor()
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column {
                if (showColorCard) {
                    ColorSchemeCard()
                }
                LazyColumn(
                    contentPadding = PaddingValues(top = 0.dp, start = 6.dp, end = 6.dp, bottom = 88.dp),
                ) {
                    items(memos, key = {
                        memo -> memo.id
                    }) { memo ->
                        MemoRow(memo, memoListActions, showListCards)
                    }
                }
            }
        }
    }
}

@Composable
fun MemoRow(memo: MemoEntity, memoListActions: IMemoListActions, showListCards: Boolean) {
    var showMenu by rememberSaveable { mutableStateOf(false) }
    var menuMemoId by rememberSaveable { mutableIntStateOf(-1) }
    val cardSettings = Themes.getCardSettings(showListCards)
    Card(modifier = cardSettings.cardPadding,
        colors = CardDefaults.cardColors(containerColor = cardSettings.cardColor),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row (
            modifier = Modifier
                .combinedClickable(
                    onClick = { memoListActions.onEditMemo(memo.id) },
                    onLongClick = {
                        showMenu = true
                        menuMemoId = memo.id
                    })
                .padding(horizontal = 1.dp)
        ) {
            Text(
                text = memo.content,
                modifier = cardSettings.textPadding
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                lineHeight = TextUnit(1.2f, TextUnitType.Em),
                color = cardSettings.textColor
            )
            Checkbox(
                checked = memo.isActive,
                onCheckedChange = { memoListActions.onCheckBoxChecked(memo.id) },
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            DropdownMenu(
                expanded = showMenu && menuMemoId == memo.id,
                offset = DpOffset(64.dp, 0.dp),
                modifier = Modifier.padding(start = 4.dp),
                onDismissRequest = { showMenu = false },
                containerColor = Themes.getDropdownMenuBackground()
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.menu_delete)) },
                    onClick = {
                        showMenu = false
                        menuMemoId = -1
                        memoListActions.onDeleteMemo(memo.id)
                    }
                )
            }
        }
    }
}

@Composable
fun OverflowMenu(showColorCard: Boolean, onCardVisibilityChanged: () -> Unit, showListCards: Boolean,
                 onListCardVisChanged: () -> Unit, onSettings: () -> Unit) {
    var showOverflowMenu by rememberSaveable { mutableStateOf(false) }

    IconButton(onClick = { showOverflowMenu = !showOverflowMenu }) {
        Icon(painterResource(R.drawable.ic_menu_more), stringResource(R.string.menu))
    }
    DropdownMenu(
        expanded = showOverflowMenu, onDismissRequest = { showOverflowMenu = false },
        modifier = Modifier.padding(start = 4.dp),
        containerColor = Themes.getDropdownMenuBackground()
    ) {
        val colorCardLabel = if (showColorCard) stringResource(R.string.menu_color_card_hide) else stringResource(R.string.menu_color_card_show)
        val listCardLabel = if (showListCards) stringResource(R.string.menu_list_card_hide) else stringResource(R.string.menu_list_card_show)
        DropdownMenuItem(
            text = { Text(colorCardLabel) },
            onClick = {
                onCardVisibilityChanged()
                showOverflowMenu = false
            })
        DropdownMenuItem(
            text = { Text(listCardLabel) },
            onClick = {
                onListCardVisChanged()
                showOverflowMenu = false
            })
        DropdownMenuItem(
            text = { Text(stringResource(R.string.menu_backup)) },
            onClick = {
                Log.d("ListUIScreen", "ListUIScreen: click for backup")
                showOverflowMenu = false
            })
        DropdownMenuItem(
            text = { Text(stringResource(R.string.settings)) },
            onClick = {
                Log.d("ListUIScreen", "ListUIScreen: click for settings")
                onSettings()
                showOverflowMenu = false
            })
    }
}