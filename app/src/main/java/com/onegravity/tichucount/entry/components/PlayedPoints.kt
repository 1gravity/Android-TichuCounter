package com.onegravity.tichucount.entry.components

import com.onegravity.tichucount.databinding.OneEntryBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.Subject

class PlayedPoints(
    private val binding: OneEntryBinding,
    update: Subject<Boolean>,
    playedPoints: Int = 0
) : Component(update) {

    private val np = NumberPicker(binding)

    init {
        np.setValue(playedPoints)
    }

    override fun changed(): Observable<Unit> = np.changed().map { true }

    override fun update() {
        if (binding.scoreDoubleWin.isChecked) {
            np.setValue(0)
            np.setEnable(false)
        } else {
            np.setEnable(true)
        }
    }

    fun getPoints() = np.getValue()

}
