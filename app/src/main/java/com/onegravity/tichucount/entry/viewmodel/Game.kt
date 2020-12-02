package com.onegravity.tichucount.entry.viewmodel

import com.onegravity.tichucount.APP_SCOPE
import com.onegravity.tichucount.util.LOGGER_TAG
import com.onegravity.tichucount.util.Logger
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import java.util.concurrent.atomic.AtomicBoolean

data class Game(val team1: Entry, val team2: Entry) {

    private var valve1 = AtomicBoolean(true)
    private var valve2 = AtomicBoolean(true)

    private val logger: Logger by inject()

    init {
        KTP.openRootScope().openSubScope(APP_SCOPE).inject(this)

        team1.changes().filter { valve1.get() }
            .subscribe {
                logger.d(LOGGER_TAG, "${team1.name}: $it changed")
                resolveDependencies(valve2, team1, team2)
            }

        team2.changes().filter { valve2.get() }
            .subscribe {
                logger.d(LOGGER_TAG, "${team2.name}: $it changed")
                resolveDependencies(valve1, team2, team1)
            }

        // initial/one-time dependency resolution
        resolveDependencies(valve1, team1, team2)
    }

    private fun resolveDependencies(sourceValve: AtomicBoolean, source: Entry, dest: Entry) {
        sourceValve.set(false)

        if (source.tichu == EntryState.WON || source.bigTichu == EntryState.WON || source.doubleWin) {
            if (dest.tichu == EntryState.WON) dest.tichu = EntryState.NOT_PLAYED
            if (dest.bigTichu == EntryState.WON) dest.bigTichu = EntryState.NOT_PLAYED
            dest.doubleWin = false
        }

        dest.playedPoints = when (source.doubleWin) {
            true ->  0
            false -> 100 - source.playedPoints
        }

        sourceValve.set(true)
    }

}
