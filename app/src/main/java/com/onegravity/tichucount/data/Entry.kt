package com.onegravity.tichucount.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.onegravity.tichucount.entry.viewmodel.EntryState

@Entity(tableName = "entry")
data class Entry(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "tichu") val tichu: EntryState,
    @ColumnInfo(name = "big_tichu") val bigTichu: EntryState,
    @ColumnInfo(name = "double_win") val doubleWin: Boolean,
    @ColumnInfo(name = "played_points") val playedPoints: Int,
    @ColumnInfo(name = "total_points") val totalPoints: Int,
)