package com.onegravity.tichucount

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import kotlin.math.log

class TichuDialog: DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.tichu_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nrPicker = view.findViewById<NumberPicker>(R.id.score_number)
        nrPicker.minValue = 0
        nrPicker.maxValue = 30
        nrPicker.value = 5
        nrPicker.wrapSelectorWheel = false
        nrPicker.setOnValueChangedListener { npPicker, oldValue, newValue ->
            val value = newValue.minus(5).times(5)
            Log.i(view.context.getString(R.string.app_name), "Value: $value")
        }

        nrPicker.setFormatter { value -> value.minus(5).times(5).toString() }

        View
    }

}