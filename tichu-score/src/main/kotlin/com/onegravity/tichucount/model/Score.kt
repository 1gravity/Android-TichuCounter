package com.onegravity.tichucount.model

import com.onegravity.tichucount.util.LOGGER_TAG
import com.onegravity.tichucount.util.Logger
import java.io.Serializable

interface ScoreListener {
    fun onChanged(type: ScoreType)
}

data class Score(
    private val initialTichu: ScoreState,
    private val initialGrandTichu: ScoreState,
    private val initialDoubleWin: Boolean,
    private val initialPlayedPoints: Int,
    val teamName: String,
    private val logger: Logger
) : Serializable {

    private val listeners = ArrayList<ScoreListener>()

    fun addListener(listener: ScoreListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: ScoreListener) {
        listeners.remove(listener)
    }

    var tichu = initialTichu
        set(value) {
            if (field != value) {
                logger.d(LOGGER_TAG, "${teamName}: TICHU changed from $field to $value")
                field = value
                validateTichu(value)
                listeners.forEach { it.onChanged(ScoreType.TICHU) }
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
                logger.d(LOGGER_TAG, "${teamName}: GRAND_TICHU changed from $field to $value")
                field = value
                validateGrandTichu(value)
                listeners.forEach { it.onChanged(ScoreType.GRAND_TICHU) }
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
                logger.d(LOGGER_TAG, "${teamName}: DOUBLE_WIN changed from $field to $value")
                field = value
                validateDoubleWin(value)
                listeners.forEach { it.onChanged(ScoreType.DOUBLE_WIN) }
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
                logger.d(LOGGER_TAG, "${teamName}: PLAYED_POINTS changed from $field to $value")
                field = value
                validatePlayedPoints(value)
                listeners.forEach { it.onChanged(ScoreType.PLAYED_POINTS) }
            }
        }

    private fun validatePlayedPoints(value: Int) {
        if (value != 0) {
            doubleWin = false
        }
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
