package com.onegravity.tichucount.newentry

import com.jakewharton.rxbinding4.view.clicks
import com.onegravity.tichucount.databinding.NewEntryBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.Subject

class BigTichu(private val binding: NewEntryBinding, update: Subject<Boolean>) : Component(update) {

    override fun changes(): Observable<Unit> =
        binding.scoreBigTichuWin.clicks().mergeWith(binding.scoreBigTichuLoss.clicks())

    override fun update() {
        if (binding.scoreDoubleWin.isChecked) {
            binding.scoreBigTichuLoss.isChecked = false
        }
    }

}