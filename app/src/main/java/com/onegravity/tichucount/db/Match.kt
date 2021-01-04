package com.onegravity.tichucount.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A Match is the top level entity.
 * I consists of multiple Games with Scores for each team.
 */
@Entity(tableName = "match")
data class Match(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "name_team_1") val team1: String,
    @ColumnInfo(name = "name_team_2") val team2: String,
    @ColumnInfo(name = "score_team_1") val score1: Int,
    @ColumnInfo(name = "score_team_2") val score2: Int,
)