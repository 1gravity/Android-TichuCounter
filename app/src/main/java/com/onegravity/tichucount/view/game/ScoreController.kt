package com.onegravity.tichucount.view.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.onegravity.tichucount.databinding.ScoreBinding
import com.onegravity.tichucount.model.Score
import com.onegravity.tichucount.model.ScoreState
import com.onegravity.tichucount.model.ScoreType
import com.onegravity.tichucount.view.BaseController

const val SCORE_ARG = "SCORE_ARG"

class ScoreController(args: Bundle): BaseController() {

    private val score = args.getSerializable(SCORE_ARG) as Score

    private lateinit var binding: ScoreBinding

    private lateinit var numberPicker: NumberPicker

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ) = ScoreBinding.inflate(inflater).run {
        scope.inject(this@ScoreController)
        binding = this
        numberPicker = NumberPicker(binding)
        root
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        bindView()
    }

    private fun bindView() {
        // initialization of the ui
        update(ScoreType.TICHU)
        update(ScoreType.BIG_TICHU)
        update(ScoreType.DOUBLE_WIN)
        update(ScoreType.PLAYED_POINTS)

        score.changes().subscribe { update(it) }

        bindTichu(binding.scoreTichuWin, binding.scoreTichuLoss) { score.tichu = it }
        bindTichu(binding.scoreBigTichuWin, binding.scoreBigTichuLoss) { score.bigTichu = it }
        bindDoubleWin()
        bindPlayedPoints()
    }

    private fun update(type: ScoreType) {
        when (type) {
            ScoreType.TICHU -> {
                binding.scoreTichuWin.isChecked = score.tichu == ScoreState.WON
                binding.scoreTichuLoss.isChecked = score.tichu == ScoreState.LOST
            }
            ScoreType.BIG_TICHU -> {
                binding.scoreBigTichuWin.isChecked = score.bigTichu == ScoreState.WON
                binding.scoreBigTichuLoss.isChecked = score.bigTichu == ScoreState.LOST
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

    private fun bindPlayedPoints() =
        numberPicker.changed()
            .subscribe {
                score.playedPoints = numberPicker.getValue()
            }

}
