package com.onegravity.tichucount.newentry.components

import com.onegravity.tichucount.databinding.OneEntryBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.Subject

class TotalPoints(private val binding: OneEntryBinding,
                  update: Subject<Boolean>,
                  private val playedPoints: PlayedPoints
) : Component(update) {

    init {
        binding.scoreTotal.text = "0"
    }

    override fun changed(): Observable<Unit> = Observable.empty()

    public override fun update() {
        var points = 0

        if (binding.scoreTichuWin.isChecked) points += 100
        if (binding.scoreTichuLoss.isChecked) points -= 100
        if (binding.scoreBigTichuWin.isChecked) points += 200
        if (binding.scoreBigTichuLoss.isChecked) points -= 200
        if (binding.scoreDoubleWin.isChecked) points += 200
        points += playedPoints.getPoints()

        binding.scoreTotal.text = points.toString()
    }

}
