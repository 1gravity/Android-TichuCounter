package com.onegravity.tichucount.view.game

import android.widget.EditText
import androidx.core.view.children
import com.onegravity.tichucount.databinding.ScoreBinding
import io.reactivex.rxjava3.subjects.BehaviorSubject


class NumberPicker(binding: ScoreBinding) {

    private val nrPicker = binding.scoreNumber

    private var lastValue = 0

    private val changed = BehaviorSubject.create<Boolean>()

    init {
        nrPicker.minValue = 0
        nrPicker.maxValue = 30
        nrPicker.value = 5
        nrPicker.wrapSelectorWheel = false
        nrPicker.setOnValueChangedListener { _, _, newValue ->
            lastValue = newValue.minus(5).times(5)
            changed.onNext(true)
        }
        nrPicker.setFormatter { value -> value.minus(5).times(5).toString() }
        initNumberPicker()
    }

    fun setValue(newValue: Int) {
        lastValue = newValue
        nrPicker.value = newValue.div(5).plus(5)
    }

//    fun setEnable(enabled: Boolean) {
//        nrPicker.isEnabled = enabled
//    }

    private fun initNumberPicker() {
        nrPicker.children.iterator().forEach {
            if (it is EditText) it.filters = arrayOfNulls(0)    // remove default input filter
        }
    }

    fun changed(): BehaviorSubject<Boolean> = changed

    fun getValue() = lastValue

}
