package com.onegravity.tichucount.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "match")
data class Match(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name_team_1") val team1: String,
    @ColumnInfo(name = "name_team_2") val team2: String,
    @ColumnInfo(name = "score_team_1") val score1: Int,
    @ColumnInfo(name = "score_team_2") val score12: Int,
)