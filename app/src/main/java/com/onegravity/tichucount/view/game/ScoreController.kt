package com.onegravity.tichucount.view.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.Toolbar
import com.onegravity.tichucount.R
import com.onegravity.tichucount.databinding.TeamScoreBinding
import com.onegravity.tichucount.model.Score
import com.onegravity.tichucount.model.ScoreState
import com.onegravity.tichucount.model.ScoreType
import com.onegravity.tichucount.view.BaseController

const val SCORE_ARG = "SCORE_ARG"

class ScoreController(args: Bundle): BaseController() {

    private val score = args.getSerializable(SCORE_ARG) as Score

    private lateinit var binding: TeamScoreBinding

    private lateinit var numberPicker: NumberPicker

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ) = TeamScoreBinding.inflate(inflater).run {
        scope.inject(this@ScoreController)
        binding = this
        numberPicker = NumberPicker(binding)
        root
    }

    override fun onEnterStarted(view: View?) {
        super.onEnterStarted(view)
        bindView(binding)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)

        activity?.findViewById<Toolbar>(R.id.toolbar)
            ?.setNavigationOnClickListener { router.handleBack() }
    }

    private fun bindView(binding: TeamScoreBinding) {
        score.changes()
            .subscribe { type ->
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
                    else -> { }
                }
                binding.scoreTotal.text = score.points().toString()
            }

        bindTichu(binding.scoreTichuWin, binding.scoreTichuLoss) { score.tichu = it }
        bindTichu(binding.scoreBigTichuWin, binding.scoreBigTichuLoss) { score.bigTichu = it }
        bindDoubleWin(binding)
        bindPlayedPoints()
    }

    private fun bindTichu(win: AppCompatCheckBox, loss: AppCompatCheckBox, assign: (state: ScoreState) -> Unit) {
        win.setOnClickListener {
            when(win.isChecked) {
                true -> ScoreState.WON
                else -> ScoreState.NOT_PLAYED
            }.run { assign(this) }
        }

        loss.setOnClickListener {
            when(loss.isChecked) {
                true -> ScoreState.LOST
                else -> ScoreState.NOT_PLAYED
            }.run { assign(this) }
        }
    }

    private fun bindDoubleWin(binding: TeamScoreBinding) =
        binding.scoreDoubleWin.setOnClickListener {
            score.doubleWin = binding.scoreDoubleWin.isChecked
        }

    private fun bindPlayedPoints() =
        numberPicker.changed()
            .subscribe {
                score.playedPoints = numberPicker.getValue()
            }

}
