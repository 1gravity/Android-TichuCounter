package com.onegravity.tichucount.entry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.bluelinelabs.conductor.Controller
import com.onegravity.tichucount.R
import com.onegravity.tichucount.databinding.OneEntryBinding
import com.onegravity.tichucount.entry.viewmodel.Entry
import com.onegravity.tichucount.entry.viewmodel.EntryState
import com.onegravity.tichucount.entry.viewmodel.EntryType

const val TEAM_ARG = "TEAM_ARG"

class OneEntryController(args: Bundle): Controller() {

    private val team = args.getSerializable(TEAM_ARG) as Entry

    private lateinit var binding: OneEntryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ) = OneEntryBinding.inflate(inflater).run {
        binding = this
        root
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
                        binding.scoreBigTichuWin.isChecked = team.tichu == EntryState.WON
                        binding.scoreBigTichuLoss.isChecked = team.tichu == EntryState.LOST
                    }
                    EntryType.DOUBLE_WIN ->
                        binding.scoreDoubleWin.isChecked = team.doubleWin
                    EntryType.PLAYED_POINTS -> {
                        // tbd
                    }
                }
            }

        bindTichu(binding)
    }

    private fun bindTichu(binding: OneEntryBinding) {
        binding.scoreTichuWin.setOnClickListener {
            team.tichu = when(binding.scoreTichuWin.isChecked) {
                true -> EntryState.WON
                else -> EntryState.NOT_PLAYED
            }
        }

        binding.scoreTichuLoss.setOnClickListener {
            team.tichu = when(binding.scoreTichuLoss.isChecked) {
                true -> EntryState.LOST
                else -> EntryState.NOT_PLAYED
            }
        }
    }

    override fun onAttach(view: View) {
        super.onAttach(view)

        bindView(binding)

        activity?.findViewById<Toolbar>(R.id.toolbar)
            ?.setNavigationOnClickListener { router.handleBack() }
    }

}
