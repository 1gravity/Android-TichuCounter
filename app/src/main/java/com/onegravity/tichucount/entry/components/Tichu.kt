package com.onegravity.tichucount.entry.components

import com.jakewharton.rxbinding4.view.clicks
import com.onegravity.tichucount.databinding.OneEntryBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.Subject

class Tichu(
    private val binding: OneEntryBinding,
    update: Subject<Boolean>,
    won: Boolean = false,
    lost: Boolean = false
) : Component(update) {

    init {
        binding.scoreTichuWin.isChecked = won
        binding.scoreTichuLoss.isChecked = lost
    }

    override fun changed(): Observable<Unit> =
        binding.scoreTichuWin.clicks()
            .doOnNext {
                if (binding.scoreTichuWin.isChecked) {
                    binding.scoreTichuLoss.isChecked = false
                }
            }
            .mergeWith(
                binding.scoreTichuLoss.clicks()
                    .doOnNext {
                        if (binding.scoreTichuLoss.isChecked) {
                            binding.scoreTichuWin.isChecked = false
                        }
                    }
            )

    override fun update() {
        if (binding.scoreDoubleWin.isChecked) {
            binding.scoreTichuLoss.isChecked = false
        }
    }

}