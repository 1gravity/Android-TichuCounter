package com.onegravity.tichucount.newentry

import com.onegravity.tichucount.databinding.NewEntryBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.Subject

class TotalPoints(private val binding: NewEntryBinding, update: Subject<Boolean>) : Component(update) {

    init {
        binding.scoreTotal.text = "0"
    }

    override fun changes(): Observable<Unit> = Observable.empty()

    override fun update() {
        var points = 0

        if (binding.scoreTichuWin.isChecked) points += 100
        if (binding.scoreBigTichuWin.isChecked) points += 200
        if (binding.scoreDoubleWin.isChecked) points += 200
//        points += binding..getValue()

        binding.scoreTotal.text = points.toString()
    }

}
