package com.onegravity.tichucount.newentry

import com.jakewharton.rxbinding4.view.clicks
import com.onegravity.tichucount.databinding.NewEntryBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.Subject

class DoubleWin(private val binding: NewEntryBinding, update: Subject<Boolean>) : Component(update) {

    override fun changes(): Observable<Unit> = binding.scoreDoubleWin.clicks()

    override fun update() {
        if (binding.scoreTichuLoss.isChecked || binding.scoreBigTichuLoss.isChecked) {
            binding.scoreDoubleWin.isChecked = false
        } else if (binding.scoreTichuWin.isChecked && binding.scoreBigTichuWin.isChecked) {
            binding.scoreDoubleWin.isChecked = true
        }

    }

}
