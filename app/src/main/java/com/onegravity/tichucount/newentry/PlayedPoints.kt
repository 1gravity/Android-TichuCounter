package com.onegravity.tichucount.newentry

import com.onegravity.tichucount.databinding.NewEntryBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.Subject

class PlayedPoints(private val binding: NewEntryBinding, update: Subject<Boolean>) : Component(update) {

    private val np = NumberPicker(binding)

    init {
        np.setValue(0)
    }

    override fun changes(): Observable<Unit> = np.getValue().map { true }

    override fun update() {
        if (binding.scoreDoubleWin.isChecked) {
            np.setValue(0)
        }
    }

}
