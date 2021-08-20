package com.onegravity.tichucount.view.game

import android.widget.EditText
import androidx.core.view.children
import com.onegravity.tichucount.databinding.ScoreBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NumberPicker(binding: ScoreBinding, scope: CoroutineScope) {

    private val nrPicker = binding.scoreNumber

    private var lastValue = 0

    private val changed = MutableStateFlow(false)

    init {
        nrPicker.minValue = 0
        nrPicker.maxValue = 30
        nrPicker.value = 5
        nrPicker.wrapSelectorWheel = false
        nrPicker.setOnValueChangedListener { _, _, newValue ->
            lastValue = newValue.minus(5).times(5)
            scope.launch { changed.emit(true) }
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

    fun changed(): Flow<Boolean> = changed

    fun getValue() = lastValue

}
