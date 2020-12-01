package com.onegravity.tichucount.entry

import com.onegravity.tichucount.databinding.OneEntryBinding
import io.reactivex.rxjava3.subjects.BehaviorSubject

class NumberPicker(binding: OneEntryBinding) {

    private val nrPicker = binding.scoreNumber

    private var lastValue = 0

    private val changed = BehaviorSubject.create<Boolean>()

    init {
        nrPicker.minValue = 0
        nrPicker.maxValue = 30
        nrPicker.value = 5
        nrPicker.wrapSelectorWheel = false
        nrPicker.setOnValueChangedListener { npPicker, oldValue, newValue ->
            lastValue = newValue.minus(5).times(5)
            changed.onNext(true)
        }
        nrPicker.setFormatter { value -> value.minus(5).times(5).toString() }
    }

    fun setValue(newValue: Int) {
        lastValue = newValue
        nrPicker.value = newValue.div(5).plus(5)
    }

    fun setEnable(enabled: Boolean) {
        nrPicker.isEnabled = enabled
    }

    fun changed(): BehaviorSubject<Boolean> = changed

    fun getValue() = lastValue

}