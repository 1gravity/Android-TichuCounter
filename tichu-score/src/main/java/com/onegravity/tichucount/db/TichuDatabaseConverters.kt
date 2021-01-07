package com.onegravity.tichucount.db

import androidx.room.TypeConverter
import com.onegravity.tichucount.model.ScoreState

class TichuDatabaseConverters {

    @TypeConverter
    fun entryStateFromString(value: String?) = try {
        value?.let {
            ScoreState.valueOf(it)
        } ?: ScoreState.NOT_PLAYED
    } catch (e: IllegalArgumentException) {
        ScoreState.NOT_PLAYED
    }

    @TypeConverter
    fun entryStateToString(value: ScoreState?) =
        value?.name ?: ScoreState.NOT_PLAYED.name

}