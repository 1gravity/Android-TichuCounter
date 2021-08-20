package com.onegravity.tichucount.view.match

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.view.*
import com.funnydevs.hilt_conductor.annotations.ConductorEntryPoint
import com.onegravity.tichucount.R
import com.onegravity.tichucount.databinding.MainBinding
import com.onegravity.tichucount.db.MatchWithGames
import com.onegravity.tichucount.util.LOGGER_TAG
import com.onegravity.tichucount.util.Logger
import com.onegravity.tichucount.util.launch
import com.onegravity.tichucount.view.BaseController
import com.onegravity.tichucount.view.game.GAME_UID
import com.onegravity.tichucount.view.game.GameController
import com.onegravity.tichucount.viewmodel.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

const val MATCH_UID = "MATCH_UID"

@ConductorEntryPoint
class MatchController(args: Bundle) : BaseController() {

    private lateinit var binding: MainBinding

    @Inject
    lateinit var appContext: Context

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var viewModel: MatchViewModel

    private val matchUid = args.getInt(MATCH_UID)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?)
            : View =
        MainBinding.inflate(inflater).run {
            binding = this
            setToolbar(binding.toolbar)
            setHasOptionsMenu(true)
            root
        }

    override fun onAttach(view: View) {
        super.onAttach(view)

        setToolbar(binding.toolbar)

        viewModel.launch {
            launch { loadMatch(view, matchUid) }
            launch { processEvents() }
        }
    }

    private suspend fun loadMatch(view: View, matchUid: Int) {
        viewModel.match(matchUid)
            .catch { gameLoadError(it) }
            .collect { bind(view.context, it) }
    }

    private suspend fun processEvents() {
        viewModel.events()
            .catch { logger.e(LOGGER_TAG, "Failed to process events", it) }
            .collect { event -> dispatchEvents(event) }
    }

    private fun gameLoadError(error: Throwable) {
        logger.e(LOGGER_TAG, "Failed to load match: ${error.message}")
        router.popCurrentController()
    }

    private fun dispatchEvents(event: MatchViewModelEvent) {
        when (event) {
            is NewGame -> newGame()
            is OpenGame -> openGame(event.gameUid)
            is DeleteGame -> { /* todo */ }
        }
    }

    private fun bind(context: Context, match: MatchWithGames) {
        appContext.getString(R.string.match_title, match.match.team1, match.match.team2)
            .run { binding.toolbarLayout.title = this }
        binding.fab.setOnClickListener { newGame() }

        val header = GameEntry(viewModel, header = true, footer = false, gameUid = 0,
            gameNr = context.getString(R.string.game_nr), team1Score = match.match.team1, team2Score = match.match.team2
        )

        val entries =  match.games.foldIndexed(arrayListOf(header)) { pos, list, game ->
            val entry = GameEntry(viewModel, header = false, footer = false,
                gameUid = game.uid,
                gameNr = pos.inc().toString(),
                team1Score = game.score_1.totalPoints.toString(),
                team2Score = game.score_2.totalPoints.toString()
            )
            list.apply { add(entry) }
        }

        GameEntry(viewModel, header = false, footer = true, gameUid = 0,
            gameNr = "", team1Score = match.match.score1.toString(), team2Score = match.match.score2.toString()
        ).also { entries.add(it) }

        val viewAdapter = MatchAdapter(entries)
        val viewManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)

        binding.list.recyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_match, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_match -> {
                deleteMatch()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteMatch() {
        viewModel.launch {
            try {
                viewModel.deleteMatch(matchUid)
            } catch (e: Exception) {
                logger.e(LOGGER_TAG, "Failed to delete match $matchUid", e)
            }
        }
    }

    private fun openGame(gameUid: Int) {
        val args = Bundle().apply {
            putInt(MATCH_UID, matchUid)
            putInt(GAME_UID, gameUid)
        }
        push(GameController(args))
    }

    private fun newGame() {
        val args = Bundle().apply { putInt(MATCH_UID, matchUid) }
        push(GameController(args))
    }

}
