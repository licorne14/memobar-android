package com.lk.memobar2.database

import android.text.format.DateFormat
import androidx.room.*
import java.io.Serializable
import java.util.*

/**
 * Erstellt von Lena am 26/04/2019.
 */
@Entity(tableName = "memos")
class MemoEntity : Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "memo_id")
    var id: Int = 0

    @ColumnInfo(name = "content")
    var content: String = ""

    @ColumnInfo(name = "isActive")
    var isActive: Boolean = false

    @ColumnInfo(name = "lastUpdated")
    var lastUpdated: String = ""

    @ColumnInfo(name = "importance", defaultValue = "0")
    var importance: Int = 0
    // 0 -> default importance, -1 -> less important

    override fun toString(): String {
        return "{id: $id, content: \"$content\", isActive: $isActive, lastUpdated: $lastUpdated," +
            " importance $importance}"
    }

    fun setCurrentTimeStamp(){
        lastUpdated = DateFormat.format("yyyy/MM/dd HH:mm:ss", Date().time).toString()
    }
}