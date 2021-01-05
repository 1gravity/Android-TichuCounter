package com.onegravity.tichucount.view.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.onegravity.tichucount.R
import com.onegravity.tichucount.databinding.ScoresBinding
import com.onegravity.tichucount.model.Game

import com.onegravity.tichucount.util.LOGGER_TAG
import com.onegravity.tichucount.view.BaseController
import com.onegravity.tichucount.view.match.MATCH_UID
import com.onegravity.tichucount.viewmodel.GameViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import toothpick.ktp.delegate.inject

const val GAME_UID = "GAME_UID"

open class GameController(args: Bundle): BaseController() {

    private lateinit var binding: ScoresBinding

    private val gameViewModel: GameViewModel by inject()

    private val matchUid = args.getInt(MATCH_UID)
    private val gameUid = args.getInt(GAME_UID)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ) = ScoresBinding.inflate(inflater).run {
        scope.inject(this@GameController)
        binding = this
        root
    }

    override fun onAttach(view: View) {
        super.onAttach(view)

        activity?.findViewById<Toolbar>(R.id.toolbar)
            ?.setNavigationOnClickListener { router.handleBack() }

        gameViewModel.game(matchUid, gameUid)
            .doOnSubscribe { disposables.add(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { game -> bind(game) },
                { gameLoadError(it) }
            )

        binding.btnNegative.setOnClickListener {
            router.popCurrentController()
        }

        binding.btnPositive.setOnClickListener {
            // to do
        }
    }

    private fun gameLoadError(error: Throwable) {
        logger.e(LOGGER_TAG, "Failed to load match: ${error.message}")
        router.popCurrentController()
    }

    private fun bind(game: Game) {
        binding.viewpager.adapter = ScoreAdapter(this@GameController, game)
        binding.viewpager.offscreenPageLimit = 1

        TabLayoutMediator(
            binding.tabLayout, binding.viewpager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = if (position == 0) game.name1 else game.name2
        }.attach()
    }

    override fun onDetach(view: View) {
        super.onDetach(view)

        if (!activity!!.isChangingConfigurations) {
            binding.viewpager.adapter = null
        }
        binding.tabLayout.setupWithViewPager(null)
    }

}
