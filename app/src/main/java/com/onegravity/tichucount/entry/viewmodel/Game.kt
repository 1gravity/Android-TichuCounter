package com.onegravity.tichucount.entry.viewmodel

import com.onegravity.tichucount.util.Valve
import com.onegravity.tichucount.util.ValveState
import hu.akarnokd.rxjava3.operators.ObservableTransformers

data class Game(val team1: Entry, val team2: Entry) {

    private val valve1 = Valve(ValveState.OPENED)
    private val valve2 = Valve(ValveState.OPENED)

    init {
        team1.changes()
            .compose(ObservableTransformers.valve(valve1.isOpen()))
            .subscribe {
                resolveDependencies(valve1, team1, team2)
            }

        team2.changes()
            .compose(ObservableTransformers.valve(valve1.isOpen()))
            .subscribe {
                resolveDependencies(valve2, team2, team1)
            }

        resolveDependencies(valve1, team1, team2)
    }

    private fun resolveDependencies(sourceValve: Valve, source: Entry, dest: Entry) {
        sourceValve.close()

        if (source.tichu == EntryState.WON || source.bigTichu == EntryState.WON || source.doubleWin) {
            if (dest.tichu == EntryState.WON) dest.tichu = EntryState.NOT_PLAYED
            if (dest.bigTichu == EntryState.WON) dest.bigTichu = EntryState.NOT_PLAYED
            dest.doubleWin = false
        }

        when (source.doubleWin || dest.doubleWin) {
            true -> dest.playedPoints = 0
            false -> dest.playedPoints = 100 - source.playedPoints
        }

        sourceValve.open()
    }
}