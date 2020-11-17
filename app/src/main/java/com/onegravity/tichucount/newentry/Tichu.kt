package com.onegravity.tichucount.newentry

import com.jakewharton.rxbinding4.view.clicks
import com.onegravity.tichucount.databinding.NewEntryBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.Subject

class Tichu(private val binding: NewEntryBinding, update: Subject<Boolean>) : Component(update) {

    override fun changes(): Observable<Unit> =
        binding.scoreTichuWin.clicks().mergeWith(binding.scoreTichuLoss.clicks())

    override fun update() {
        if (binding.scoreDoubleWin.isChecked) {
            binding.scoreTichuLoss.isChecked = false
        }
    }

}