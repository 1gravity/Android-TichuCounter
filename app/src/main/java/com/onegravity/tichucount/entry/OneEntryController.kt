package com.onegravity.tichucount.entry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.Toolbar
import com.onegravity.tichucount.BaseController
import com.onegravity.tichucount.R
import com.onegravity.tichucount.databinding.OneEntryBinding
import com.onegravity.tichucount.entry.viewmodel.Entry
import com.onegravity.tichucount.entry.viewmodel.EntryState
import com.onegravity.tichucount.entry.viewmodel.EntryType

const val TEAM_ARG = "TEAM_ARG"

class OneEntryController(args: Bundle): BaseController() {

    private val team = args.getSerializable(TEAM_ARG) as Entry

    private lateinit var binding: OneEntryBinding

    private lateinit var numberPicker: NumberPicker

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ) = OneEntryBinding.inflate(inflater).run {
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

    private fun bindView(binding: OneEntryBinding) {
        team.changes()
            .subscribe { type ->
                when (type) {
                    EntryType.TICHU -> {
                        binding.scoreTichuWin.isChecked = team.tichu == EntryState.WON
                        binding.scoreTichuLoss.isChecked = team.tichu == EntryState.LOST
                    }
                    EntryType.BIG_TICHU -> {
                        binding.scoreBigTichuWin.isChecked = team.bigTichu == EntryState.WON
                        binding.scoreBigTichuLoss.isChecked = team.bigTichu == EntryState.LOST
                    }
                    EntryType.DOUBLE_WIN -> {
                        binding.scoreDoubleWin.isChecked = team.doubleWin
                    }
                    EntryType.PLAYED_POINTS -> {
                        numberPicker.setValue(team.playedPoints)
                    }
                    else -> { }
                }
                binding.scoreTotal.text = team.points().toString()
            }

        bindTichu(binding.scoreTichuWin, binding.scoreTichuLoss) { team.tichu = it }
        bindTichu(binding.scoreBigTichuWin, binding.scoreBigTichuLoss) { team.bigTichu = it }
        bindDoubleWin(binding)
        bindPlayedPoints()
    }

    private fun bindTichu(win: AppCompatCheckBox, loss: AppCompatCheckBox, assign: (state: EntryState) -> Unit) {
        win.setOnClickListener {
            when(win.isChecked) {
                true -> EntryState.WON
                else -> EntryState.NOT_PLAYED
            }.run { assign(this) }
        }

        loss.setOnClickListener {
            when(loss.isChecked) {
                true -> EntryState.LOST
                else -> EntryState.NOT_PLAYED
            }.run { assign(this) }
        }
    }

    private fun bindDoubleWin(binding: OneEntryBinding) =
        binding.scoreDoubleWin.setOnClickListener {
            team.doubleWin = binding.scoreDoubleWin.isChecked
        }

    private fun bindPlayedPoints() =
        numberPicker.changed()
            .subscribe {
                team.playedPoints = numberPicker.getValue()
            }

}
