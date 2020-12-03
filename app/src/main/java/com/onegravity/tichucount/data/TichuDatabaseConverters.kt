package com.onegravity.tichucount.data

import androidx.room.TypeConverter
import com.onegravity.tichucount.entry.viewmodel.EntryState

class TichuDatabaseConverters {

    @TypeConverter
    fun entryStateFromString(value: String?) = try {
        value?.let {
            EntryState.valueOf(it)
        } ?: EntryState.NOT_PLAYED
    } catch (e: IllegalArgumentException) {
        EntryState.NOT_PLAYED
    }

    @TypeConverter
    fun entryStatetoString(value: EntryState?) =
        value?.name ?: EntryState.NOT_PLAYED.name

}