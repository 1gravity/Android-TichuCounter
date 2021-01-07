package com.onegravity.tichucount.model

import com.onegravity.tichucount.APP_SCOPE
import com.onegravity.tichucount.util.LOGGER_TAG
import com.onegravity.tichucount.util.Logger
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import java.util.concurrent.atomic.AtomicBoolean

data class Game(
    val matchUid: Int,
    val name1: String,
    val name2: String,
    val score1: Score,
    val score2: Score
) {

    private var valve1 = AtomicBoolean(true)
    private var valve2 = AtomicBoolean(true)

    private val logger: Logger by inject()

    init {
        KTP.openRootScope().openSubScope(APP_SCOPE).inject(this)

        score1.changes().filter { valve1.get() }
            .subscribe {
                logger.d(LOGGER_TAG, "${score1.teamName}: $it changed")
                resolveDependencies(it, valve2, score1, score2)
            }

        score2.changes().filter { valve2.get() }
            .subscribe {
                logger.d(LOGGER_TAG, "${score2.teamName}: $it changed")
                resolveDependencies(it, valve1, score2, score1)
            }

        // initial/one-time dependency resolution
        resolveDependencies(ScoreType.TICHU, valve1, score1, score2)
        resolveDependencies(ScoreType.GRAND_TICHU, valve1, score1, score2)
        resolveDependencies(ScoreType.DOUBLE_WIN, valve1, score1, score2)

        resolveDependencies(ScoreType.TICHU, valve2, score2, score1)
        resolveDependencies(ScoreType.GRAND_TICHU, valve2, score2, score1)
        resolveDependencies(ScoreType.DOUBLE_WIN, valve2, score2, score1)

        if (! score1.doubleWin && ! score2.doubleWin) {
            resolveDependencies(ScoreType.PLAYED_POINTS, valve1, score1, score2)
            resolveDependencies(ScoreType.PLAYED_POINTS, valve2, score2, score1)
        }
    }

    private fun resolveDependencies(scoreType: ScoreType, sourceValve: AtomicBoolean, source: Score, dest: Score) {
        sourceValve.set(false)

        when (scoreType) {
            ScoreType.TICHU ->
                if (source.tichu == ScoreState.WON) {
                    sourceWon(dest)
                    if (! source.doubleWin) dest.playedPoints = 100 - source.playedPoints
                }
            ScoreType.GRAND_TICHU ->
                if (source.grandTichu == ScoreState.WON) {
                    sourceWon(dest)
                    if (! source.doubleWin) dest.playedPoints = 100 - source.playedPoints
                }
            ScoreType.DOUBLE_WIN -> if (source.doubleWin) {
                sourceWon(dest)
                dest.playedPoints = 0
            }
            ScoreType.PLAYED_POINTS -> dest.playedPoints = 100 - source.playedPoints
        }

        sourceValve.set(true)
    }

    private fun sourceWon(dest: Score) {
        if (dest.tichu == ScoreState.WON) dest.tichu = ScoreState.NOT_PLAYED
        if (dest.grandTichu == ScoreState.WON) dest.grandTichu = ScoreState.NOT_PLAYED
        dest.doubleWin = false
    }

}
