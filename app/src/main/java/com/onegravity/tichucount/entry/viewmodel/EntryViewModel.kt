package com.onegravity.tichucount.entry.viewmodel

import android.content.Context
import com.onegravity.tichucount.R

class EntryViewModel(context: Context) {

    val team1 = Entry(
        EntryState.NOT_PLAYED,
        EntryState.NOT_PLAYED,
        false,
        0,
        context.getString(R.string.header_team_1)
    )

    val team2 = Entry(
        EntryState.NOT_PLAYED,
        EntryState.NOT_PLAYED,
        false,
        0,
        context.getString(R.string.header_team_2)
    )

    val game = Game(team1, team2)

}