package com.lk.memobar2.main

import android.os.Build
import android.util.Log
import com.lk.memobar2.database.MemoEntity
import java.lang.StringBuilder

object Utils {

    const val DIALOG_TITLE_RESOURCE = "dialogTitleResource"
    const val MEMO_KEY = "memo"
    const val PREF_KEY_NOTIFICATION = "pref_notification"

    fun isBuildVersionGreaterThan(version: Int): Boolean
            = Build.VERSION.SDK_INT >= version

    fun getNotificationStringFromList(memos: List<MemoEntity>): String {
        val text = StringBuilder()
        var shortenedText: String = ""
        val maxTextLength: Int = 75
        for(memo in memos) {
            shortenedText = if (memo.content.length >= maxTextLength) {
                val tempText = memo.content.substring(0,maxTextLength)
                // make sure we actually get the last space before that so we got complete words
                val indexSpace = tempText.lastIndexOf(' ')
                // TODO improve error handling here, this should not only check for spaces but also for new lines
                // had a list that contained plenty new lines but no spaces and it crashed it
                when {
                    indexSpace == -1 -> {
                        Log.e(
                            "Utils",
                            "getNotificationStringFromList: error: last space found is -1. Temp text: $tempText"
                        )
                        tempText + "..."
                    }
                    else -> tempText.substring(0, indexSpace) + " ..."
                }
            } else {
                memo.content
            }
            text.append(shortenedText).append("\n")
        }
        return text.toString().trim('\n')
    }

}