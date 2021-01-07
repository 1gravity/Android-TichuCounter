package com.onegravity.tichucount.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.onegravity.tichucount.model.ScoreState

@Entity(tableName = "score")
data class Score(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "tichu") val tichu: ScoreState,
    @ColumnInfo(name = "grand_tichu") val grandTichu: ScoreState,
    @ColumnInfo(name = "double_win") val doubleWin: Boolean,
    @ColumnInfo(name = "played_points") val playedPoints: Int,
    @ColumnInfo(name = "total_points") val totalPoints: Int,
)