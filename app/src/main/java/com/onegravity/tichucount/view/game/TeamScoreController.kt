package com.onegravity.tichucount.view.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.Toolbar
import com.onegravity.tichucount.view.BaseController
import com.onegravity.tichucount.R
import com.onegravity.tichucount.databinding.TeamScoreBinding
import com.onegravity.tichucount.model.ScoreState
import com.onegravity.tichucount.viewmodel.GameViewModel
import toothpick.ktp.delegate.inject

const val TEAM_ARG = "TEAM_ARG"

class TeamScoreController(args: Bundle): BaseController() {

    private val team = args.getInt(TEAM_ARG)

    private val viewModel: GameViewModel by inject()

    private lateinit var binding: TeamScoreBinding

    private lateinit var numberPicker: NumberPicker

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ) = TeamScoreBinding.inflate(inflater).run {
        scope.inject(this@TeamScoreController)
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
        viewModel.game()

//        team.changes()
//            .subscribe { type ->
//                when (type) {
//                    ScoreType.TICHU -> {
//                        binding.scoreTichuWin.isChecked = team.tichu == ScoreState.WON
//                        binding.scoreTichuLoss.isChecked = team.tichu == ScoreState.LOST
//                    }
//                    ScoreType.BIG_TICHU -> {
//                        binding.scoreBigTichuWin.isChecked = team.bigTichu == ScoreState.WON
//                        binding.scoreBigTichuLoss.isChecked = team.bigTichu == ScoreState.LOST
//                    }
//                    ScoreType.DOUBLE_WIN -> {
//                        binding.scoreDoubleWin.isChecked = team.doubleWin
//                    }
//                    ScoreType.PLAYED_POINTS -> {
//                        numberPicker.setValue(team.playedPoints)
//                    }
//                    else -> { }
//                }
//                binding.scoreTotal.text = team.points().toString()
//            }

//        bindTichu(binding.scoreTichuWin, binding.scoreTichuLoss) { team.tichu = it }
//        bindTichu(binding.scoreBigTichuWin, binding.scoreBigTichuLoss) { team.bigTichu = it }
//        bindDoubleWin(binding)
//        bindPlayedPoints()
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
//            team.doubleWin = binding.scoreDoubleWin.isChecked
        }

    private fun bindPlayedPoints() =
        numberPicker.changed()
            .subscribe {
//                team.playedPoints = numberPicker.getValue()
            }

}
