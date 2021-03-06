package com.onegravity.tichucount.db

import androidx.room.Embedded
import androidx.room.Relation

data class MatchWithGames(
    @Embedded
    val match: Match,
    @Relation(
        parentColumn = "uid",
        entityColumn = "matchUid"
    )
    val games: List<Game>
)
