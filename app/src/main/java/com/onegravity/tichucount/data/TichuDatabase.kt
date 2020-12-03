package com.onegravity.tichucount.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.onegravity.tichucount.entry.viewmodel.EntryState

@Database(
    entities = [Match::class, Game::class, Entry::class],
    version = 1
)
@TypeConverters(TichuDatabaseConverters::class)
abstract class TichuDatabase : RoomDatabase() {
    abstract fun match(): MatchDao
}
