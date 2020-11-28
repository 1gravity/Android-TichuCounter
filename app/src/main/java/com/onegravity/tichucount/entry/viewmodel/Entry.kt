package com.onegravity.tichucount.entry.viewmodel

import com.onegravity.tichucount.util.CountValve
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.io.Serializable

enum class EntryType {
    TICHU,
    BIG_TICHU,
    DOUBLE_WIN,
    PLAYED_POINTS,
}

data class Entry(
    private val initialTichu: EntryState,
    private val initialBigTichu: EntryState,
    private val initialDoubleWin: Boolean,
    private val initialPlayedPoints: Int,
    val name: String = "unknown"
) : Serializable {

    private val valve = CountValve(1)

    private val changed = BehaviorSubject.create<EntryType>()

    fun changes(): Observable<EntryType> = changed

    var tichu = initialTichu
        set(value) {
            if (field != value) {
                changeStart()
                field = value
                validateTichu()
                changeDone(EntryType.TICHU)
            }
        }

    private fun validateTichu() {
        if (tichu == EntryState.LOST) {
            doubleWin = false
        }
    }

    var bigTichu = initialBigTichu
        set(value) {
            if (field != value) {
                changeStart()
                field = value
                validateBigTichu()
                changeDone(EntryType.BIG_TICHU)
            }
        }

    private fun validateBigTichu() {
        if (bigTichu == EntryState.LOST) {
            doubleWin = false
        }
        if (bigTichu == EntryState.WON && tichu == EntryState.WON) {
            doubleWin = true
        }
    }

    var doubleWin = initialDoubleWin
        set(value) {
            if (field != value) {
                changeStart()
                field = value
                validateDoubleWin()
                changeDone(EntryType.DOUBLE_WIN)
            }
        }

    private fun validateDoubleWin() {
        if (doubleWin) {
            if (tichu == EntryState.LOST) tichu = EntryState.NOT_PLAYED
            if (bigTichu == EntryState.LOST) bigTichu = EntryState.NOT_PLAYED
            playedPoints = 0
        }
    }

    var playedPoints = initialPlayedPoints
        set(value) {
            if (field != value) {
                changeStart()
                field = value
                validatePlayedPoints()
                changeDone(EntryType.PLAYED_POINTS)
            }
        }

    private fun validatePlayedPoints() {
        if (playedPoints != 0) {
            doubleWin = false
        }
    }

    private fun changeStart() {
        valve.close()
    }

    private fun changeDone(entryType: EntryType) {
        valve.open()
        if (valve.isOpen()) {
            changed.onNext(entryType)
        }
    }

    fun points() = let {
        var points = 0

        if (tichu == EntryState.WON) points += 100
        if (tichu == EntryState.LOST) points -= 100
        if (bigTichu == EntryState.WON) points += 200
        if (bigTichu == EntryState.LOST) points -= 200
        if (doubleWin) points += 200

        points + playedPoints
    }

    init {
        validateTichu()
        validateBigTichu()
        validateDoubleWin()
        validatePlayedPoints()
    }

}
