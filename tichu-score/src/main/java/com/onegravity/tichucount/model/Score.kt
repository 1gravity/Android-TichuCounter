package com.onegravity.tichucount.model

import com.onegravity.tichucount.APP_SCOPE
import com.onegravity.tichucount.util.LOGGER_TAG
import com.onegravity.tichucount.util.Logger
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import java.io.Serializable

data class Score(
    private val initialTichu: ScoreState,
    private val initialGrandTichu: ScoreState,
    private val initialDoubleWin: Boolean,
    private val initialPlayedPoints: Int,
    val teamName: String
) : Serializable {

    private val logger: Logger by inject()

    init {
        KTP.openRootScope().openSubScope(APP_SCOPE).inject(this)
    }

    private val changed = BehaviorSubject.create<ScoreType>()

    fun changes(): Observable<ScoreType> = changed

    var tichu = initialTichu
        set(value) {
            if (field != value) {
                field = value
                logger.d(LOGGER_TAG, "${teamName}: ${field.name} changed to $value")
                validateTichu(value)
                changeDone(ScoreType.TICHU)
            }
        }

    private fun validateTichu(value: ScoreState) {
        // a team can't win both the Tichu and the Grand Tichu
        if (value == ScoreState.WON &&  grandTichu == ScoreState.WON) {
            grandTichu = ScoreState.NOT_PLAYED
        }
        // if a team loses the Tichu and the Grand Tichu it can't have a double win
        if (value == ScoreState.LOST &&  grandTichu == ScoreState.LOST) {
            doubleWin = false
        }
    }

    var grandTichu = initialGrandTichu
        set(value) {
            if (field != value) {
                field = value
                logger.d(LOGGER_TAG, "${teamName}: ${field.name} changed to $value")
                validateGrandTichu(value)
                changeDone(ScoreType.GRAND_TICHU)
            }
        }

    private fun validateGrandTichu(value: ScoreState) {
        // a team can't win both the Tichu and the Grand Tichu
        if (value == ScoreState.WON &&  tichu == ScoreState.WON) {
            tichu = ScoreState.NOT_PLAYED
        }
        // if a team loses the Tichu and the Grand Tichu it can't have a double win
        if (value == ScoreState.LOST &&  tichu == ScoreState.LOST) {
            doubleWin = false
        }
    }

    var doubleWin = initialDoubleWin
        set(value) {
            if (field != value) {
                field = value
                logger.d(LOGGER_TAG, "${teamName}: DOUBLE_WIN changed to $value")
                validateDoubleWin(value)
                changeDone(ScoreType.DOUBLE_WIN)
            }
        }

    private fun validateDoubleWin(value: Boolean) {
        if (value) {
            if (tichu == ScoreState.LOST && grandTichu == ScoreState.LOST) tichu = ScoreState.NOT_PLAYED
            playedPoints = 0
        } else {
            if (tichu == ScoreState.WON && grandTichu == ScoreState.WON) tichu = ScoreState.NOT_PLAYED
        }
    }

    var playedPoints = initialPlayedPoints
        set(value) {
            if (field != value) {
                field = value
                logger.d(LOGGER_TAG, "${teamName}: PLAYED_POINTS changed to $value")
                validatePlayedPoints(value)
                changeDone(ScoreType.PLAYED_POINTS)
            }
        }

    private fun validatePlayedPoints(value: Int) {
        if (value != 0) {
            doubleWin = false
        }
    }

    private fun changeDone(scoreType: ScoreType) {
        changed.onNext(scoreType)
    }

    fun points() = let {
        var points = 0

        if (tichu == ScoreState.WON) points += 100
        if (tichu == ScoreState.LOST) points -= 100
        if (grandTichu == ScoreState.WON) points += 200
        if (grandTichu == ScoreState.LOST) points -= 200
        if (doubleWin) points += 200

        points + playedPoints
    }

    init {
        validateTichu(tichu)
        validateGrandTichu(grandTichu)
        validateDoubleWin(doubleWin)
        validatePlayedPoints(playedPoints)
    }

}
