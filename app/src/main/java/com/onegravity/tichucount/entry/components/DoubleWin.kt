package com.onegravity.tichucount.entry.components

import com.jakewharton.rxbinding4.view.clicks
import com.onegravity.tichucount.databinding.OneEntryBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.Subject

class DoubleWin(
    private val binding: OneEntryBinding,
    update: Subject<Boolean>,
    won: Boolean = false
) : Component(update) {

    init {
        binding.scoreDoubleWin.isChecked = won
    }

    override fun changed(): Observable<Unit> = binding.scoreDoubleWin.clicks()

    override fun update() {
        if (binding.scoreTichuLoss.isChecked || binding.scoreBigTichuLoss.isChecked) {
            binding.scoreDoubleWin.isChecked = false
        } else if (binding.scoreTichuWin.isChecked && binding.scoreBigTichuWin.isChecked) {
            binding.scoreDoubleWin.isChecked = true
        }

    }

}
