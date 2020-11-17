package com.onegravity.tichucount.newentry

import com.onegravity.tichucount.databinding.NewEntryBinding
import io.reactivex.rxjava3.subjects.BehaviorSubject

class NumberPicker(binding: NewEntryBinding) {

    private val nrPicker = binding.scoreNumber

    private val value = BehaviorSubject.create<Int>()

    init {
        nrPicker.minValue = 0
        nrPicker.maxValue = 30
        nrPicker.value = 5
        nrPicker.wrapSelectorWheel = false
        nrPicker.setOnValueChangedListener { npPicker, oldValue, newValue ->
            value.onNext(newValue.minus(5).times(5))
        }
        nrPicker.setFormatter { value -> value.minus(5).times(5).toString() }
    }

    fun setValue(newValue: Int) {
        nrPicker.value = newValue.div(5).plus(5)
    }

    fun getValue() = value

}