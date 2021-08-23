package com.onegravity.tichucount.view.game

import android.widget.EditText
import androidx.core.view.children
import com.onegravity.tichucount.databinding.ScoreBinding

interface NumberPickerListener {
    fun onChanged(value: Int)
}

class NumberPicker(binding: ScoreBinding, listener: NumberPickerListener) {

    private val nrPicker = binding.scoreNumber

    private var lastValue = 0

    init {
        nrPicker.minValue = 0
        nrPicker.maxValue = 30
        nrPicker.value = 5
        nrPicker.wrapSelectorWheel = false

        nrPicker.setOnValueChangedListener { _, _, newValue ->
            lastValue = newValue.minus(5).times(5)
            listener.onChanged(lastValue)
        }

        nrPicker.setFormatter { value -> value.minus(5).times(5).toString() }
        initNumberPicker()
    }

    fun setValue(newValue: Int) {
        lastValue = newValue
        nrPicker.value = newValue.div(5).plus(5)
    }

    private fun initNumberPicker() {
        nrPicker.children.iterator().forEach {
            if (it is EditText) it.filters = arrayOfNulls(0)    // remove default input filter
        }
    }

    fun getValue() = lastValue

}
