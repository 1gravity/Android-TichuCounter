package com.onegravity.tichucount.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Match::class, Game::class, Score::class],
    version = 1
)
@TypeConverters(TichuDatabaseConverters::class)
abstract class TichuDatabase : RoomDatabase() {
    abstract fun match(): MatchDao
}
