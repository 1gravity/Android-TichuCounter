package com.onegravity.tichucount.model

import com.onegravity.tichucount.APP_SCOPE
import com.onegravity.tichucount.util.LOGGER_TAG
import com.onegravity.tichucount.util.Logger
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import java.util.concurrent.atomic.AtomicBoolean

data class Game(val name1: String, val name2: String, val score1: Score, val score2: Score) {

    private var valve1 = AtomicBoolean(true)
    private var valve2 = AtomicBoolean(true)

    private val logger: Logger by inject()

    init {
        KTP.openRootScope().openSubScope(APP_SCOPE).inject(this)

        score1.changes().filter { valve1.get() }
            .subscribe {
                logger.d(LOGGER_TAG, "${score1.teamName}: $it changed")
                resolveDependencies(valve2, score1, score2)
            }

        score2.changes().filter { valve2.get() }
            .subscribe {
                logger.d(LOGGER_TAG, "${score2.teamName}: $it changed")
                resolveDependencies(valve1, score2, score1)
            }

        // initial/one-time dependency resolution
        resolveDependencies(valve1, score1, score2)
    }

    private fun resolveDependencies(sourceValve: AtomicBoolean, source: Score, dest: Score) {
        sourceValve.set(false)

        if (source.tichu == ScoreState.WON || source.bigTichu == ScoreState.WON || source.doubleWin) {
            if (dest.tichu == ScoreState.WON) dest.tichu = ScoreState.NOT_PLAYED
            if (dest.bigTichu == ScoreState.WON) dest.bigTichu = ScoreState.NOT_PLAYED
            dest.doubleWin = false
        }

        dest.playedPoints = when (source.doubleWin) {
            true ->  0
            false -> 100 - source.playedPoints
        }

        sourceValve.set(true)
    }

}
