package com.onegravity.tichucount.entry.components

import com.jakewharton.rxbinding4.view.clicks
import com.onegravity.tichucount.databinding.OneEntryBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.Subject

class BigTichu(
    private val binding: OneEntryBinding,
    update: Subject<Boolean>,
    won: Boolean = false,
    lost: Boolean = false
) : Component(update) {

    init {
        binding.scoreBigTichuWin.isChecked = won
        binding.scoreBigTichuLoss.isChecked = lost
    }

    override fun changed(): Observable<Unit> =
        binding.scoreBigTichuWin.clicks()
            .doOnNext {
                if (binding.scoreBigTichuWin.isChecked) {
                    binding.scoreBigTichuLoss.isChecked = false
                }
            }
            .mergeWith(
                binding.scoreBigTichuLoss.clicks()
                .doOnNext {
                    if (binding.scoreBigTichuLoss.isChecked) {
                        binding.scoreBigTichuWin.isChecked = false
                    }
                }
            )

    override fun update() {
        if (binding.scoreDoubleWin.isChecked) {
            binding.scoreBigTichuLoss.isChecked = false
        }
    }

}