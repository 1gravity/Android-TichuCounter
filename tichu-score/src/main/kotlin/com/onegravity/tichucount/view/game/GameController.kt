package com.onegravity.tichucount.view.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.funnydevs.hilt_conductor.annotations.ConductorEntryPoint
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.onegravity.tichucount.R
import com.onegravity.tichucount.databinding.ScoresBinding
import com.onegravity.tichucount.model.Game

import com.onegravity.tichucount.util.LOGGER_TAG
import com.onegravity.tichucount.util.Logger
import com.onegravity.tichucount.view.BaseController
import com.onegravity.tichucount.view.match.MATCH_UID
import com.onegravity.tichucount.viewmodel.GameViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

const val GAME_UID = "GAME_UID"

@ConductorEntryPoint
class GameController(args: Bundle) : BaseController() {

    private lateinit var binding: ScoresBinding

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var viewModel: GameViewModel

    private val matchUid = args.getInt(MATCH_UID)
    private val gameUid = args.getInt(GAME_UID)
    private val isNewGame = gameUid == 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ) = ScoresBinding.inflate(inflater).run {
        binding = this
        root
    }

    override fun onAttach(view: View) {
        super.onAttach(view)

        activity?.findViewById<Toolbar>(R.id.toolbar)
            ?.setNavigationOnClickListener { router.handleBack() }

        binding.btnDelete.visibility = if (isNewGame) View.GONE else View.VISIBLE

        viewModel.getGame(matchUid, gameUid)
            .doOnSubscribe { disposables.add(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { game -> bind(game) },
                { gameLoadError(it) }
            )
    }

    private fun gameLoadError(error: Throwable) {
        logger.e(LOGGER_TAG, "Failed to load game: ${error.message}")
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

        binding.btnNegative.setOnClickListener { router.popCurrentController() }

        binding.btnPositive.setOnClickListener { saveGame(game) }

        binding.btnDelete.setOnClickListener { deleteGame() }
    }

    private fun saveGame(game: Game) {
        viewModel.saveGame(game)
            .doOnSubscribe { disposables.add(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { router.popCurrentController() },
                {
                    logger.e(LOGGER_TAG, "Failed to save game", it)
                    Toast.makeText(binding.btnNegative.context, "", Toast.LENGTH_SHORT).show()
                }
            )
    }

    private fun deleteGame() {
        viewModel.deleteGame(matchUid, gameUid)
            .doOnSubscribe { disposables.add(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { router.popCurrentController() },
                {
                    logger.e(LOGGER_TAG, "Failed to delete game", it)
                    Toast.makeText(binding.btnNegative.context, "", Toast.LENGTH_SHORT).show()
                }
            )
    }

    override fun onDetach(view: View) {
        super.onDetach(view)

        if (!activity!!.isChangingConfigurations) {
            binding.viewpager.adapter = null
        }
        binding.tabLayout.setupWithViewPager(null)
    }

}
