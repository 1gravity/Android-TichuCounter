package com.onegravity.tichucount.entry.viewmodel

import android.content.Context
import com.onegravity.tichucount.APP_SCOPE
import com.onegravity.tichucount.R
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject

class EntryViewModel {

    private val context: Context by inject()

    init {
        KTP.openRootScope().openSubScope(APP_SCOPE).inject(this)
    }

    private val team1 = Entry(
        EntryState.NOT_PLAYED,
        EntryState.NOT_PLAYED,
        false,
        0,
        context.getString(R.string.header_team_1)
    )

    private val team2 = Entry(
        EntryState.NOT_PLAYED,
        EntryState.NOT_PLAYED,
        false,
        0,
        context.getString(R.string.header_team_2)
    )

    val game = Game(team1, team2)

}