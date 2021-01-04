package com.onegravity.tichucount.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game")
data class Game(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    val matchUid: Int,
    @Embedded(prefix = "team_1_") val score_1: Score,
    @Embedded(prefix = "team_2_") val score_2: Score
)
