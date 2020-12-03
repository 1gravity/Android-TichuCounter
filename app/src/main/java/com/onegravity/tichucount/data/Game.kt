package com.onegravity.tichucount.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game")
data class Game(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    val matchUid: Int,
    @Embedded(prefix = "team1_") val team1: Entry,
    @Embedded(prefix = "team2_") val team2: Entry
)
