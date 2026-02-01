package com.lk.memobar2.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Erstellt von Lena am 26/04/2019.
 */
@Database(entities = [MemoEntity::class], version = 2)
abstract class MemoDatabase: RoomDatabase() {

    abstract fun memoDao(): DAOMemos

    companion object {

        @Volatile
        private var INSTANCE: MemoDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE memos ADD COLUMN importance INTEGER NOT NULL DEFAULT(0)")
                // database.execSQL("UPDATE memos SET importance = 0")
            }
        }

        fun getInstance(context: Context): MemoDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                MemoDatabase::class.java, "notes.db")
                .addMigrations(MIGRATION_1_2)
                .build()

    }

}