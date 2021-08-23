package com.onegravity.tichucount.view.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.funnydevs.hilt_conductor.annotations.ConductorEntryPoint
import com.onegravity.tichucount.databinding.ScoreBinding
import com.onegravity.tichucount.model.Score
import com.onegravity.tichucount.model.ScoreListener
import com.onegravity.tichucount.model.ScoreState
import com.onegravity.tichucount.model.ScoreType
import com.onegravity.tichucount.view.BaseController
import com.onegravity.tichucount.viewmodel.ScoreViewModel
import javax.inject.Inject

const val SCORE_ARG = "SCORE_ARG"

@ConductorEntryPoint
class ScoreController(args: Bundle) : BaseController(), NumberPickerListener, ScoreListener {

    private val score = args.getSerializable(SCORE_ARG) as Score

    @Inject
    lateinit var viewModel: ScoreViewModel

    private lateinit var binding: ScoreBinding

    private lateinit var numberPicker: NumberPicker

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ) = ScoreBinding.inflate(inflater).run {
        binding = this
        numberPicker = NumberPicker(binding, this@ScoreController)
        root
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        bindView()
    }

    private fun bindView() {
        // initialization of the ui
        update(ScoreType.TICHU)
        update(ScoreType.GRAND_TICHU)
        update(ScoreType.DOUBLE_WIN)
        update(ScoreType.PLAYED_POINTS)

        score.addListener(this)

        bindTichu(binding.scoreTichuWin, binding.scoreTichuLoss) { score.tichu = it }
        bindTichu(binding.scoreGrandTichuWin, binding.scoreGrandTichuLoss) { score.grandTichu = it }
        bindDoubleWin()
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        score.removeListener(this)
    }

    private fun update(type: ScoreType) {
        when (type) {
            ScoreType.TICHU -> {
                binding.scoreTichuWin.isChecked = score.tichu == ScoreState.WON
                binding.scoreTichuLoss.isChecked = score.tichu == ScoreState.LOST
            }
            ScoreType.GRAND_TICHU -> {
                binding.scoreGrandTichuWin.isChecked = score.grandTichu == ScoreState.WON
                binding.scoreGrandTichuLoss.isChecked = score.grandTichu == ScoreState.LOST
            }
            ScoreType.DOUBLE_WIN -> binding.scoreDoubleWin.isChecked = score.doubleWin
            ScoreType.PLAYED_POINTS -> numberPicker.setValue(score.playedPoints)
        }
        binding.scoreTotal.text = score.points().toString()
    }

    private fun bindTichu(win: CheckBox, loss: CheckBox, assign: (state: ScoreState) -> Unit) {
        win.setOnClickListener {
            val state = if (win.isChecked) ScoreState.WON else ScoreState.NOT_PLAYED
            assign(state)
        }

        loss.setOnClickListener {
            val state = if (loss.isChecked) ScoreState.LOST else ScoreState.NOT_PLAYED
            assign(state)
        }
    }

    private fun bindDoubleWin() =
        binding.scoreDoubleWin.setOnClickListener {
            score.doubleWin = binding.scoreDoubleWin.isChecked
        }

    // NumberPickerListener
    override fun onChanged(value: Int) {
        score.playedPoints = value
    }

    // ScoreListener
    override fun onChanged(type: ScoreType) {
        update(type)
    }

}
