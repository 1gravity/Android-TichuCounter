package com.onegravity.tichucount.view.matches

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.funnydevs.hilt_conductor.annotations.ConductorEntryPoint
import com.onegravity.tichucount.R
import com.onegravity.tichucount.databinding.MainBinding
import com.onegravity.tichucount.db.MatchWithGames
import com.onegravity.tichucount.util.LOGGER_TAG
import com.onegravity.tichucount.util.Logger
import com.onegravity.tichucount.util.launch
import com.onegravity.tichucount.view.BaseController
import com.onegravity.tichucount.view.match.MATCH_UID
import com.onegravity.tichucount.view.match.MatchController
import com.onegravity.tichucount.viewmodel.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ConductorEntryPoint
class MatchesController : BaseController() {

    private lateinit var binding: MainBinding

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var appContext: Context

    @Inject
    lateinit var viewModel: MatchesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        return MainBinding.inflate(inflater).run {
            binding = this
            setToolbar(binding.toolbar)
            setHasOptionsMenu(true)
            root
        }
    }

    override fun onAttach(view: View) {
        super.onAttach(view)

        binding.toolbarLayout.title = appContext.getString((R.string.matches_title))
        binding.fab.setOnClickListener { newMatch() }

        viewModel.launch {
            launch { loadMatches(view) }
            launch { processEvents() }
        }
    }

    private suspend fun loadMatches(view: View) {
        viewModel.matches()
            .catch { logger.e(LOGGER_TAG, "Failed to load matches", it) }
            .collect {
                bind(view.context, it)
            }
    }

    private suspend fun processEvents() {
        viewModel.events()
            .catch { logger.e(LOGGER_TAG, "Failed to process events", it) }
            .collect { event -> dispatchEvents(event) }
    }

    private fun bind(context: Context, games: List<MatchWithGames>) {
        val header = MatchEntry(viewModel, true, 0,
            appContext.getString(R.string.match_nr),
            appContext.getString(R.string.name_team_1),
            appContext.getString(R.string.score),
            appContext.getString(R.string.name_team_2),
            appContext.getString(R.string.score)
        )
        val entries = games.foldIndexed(arrayListOf(header)) { pos, list, match ->
            val entry = MatchEntry(viewModel, false,
                match.match.uid,
                pos.inc().toString(),
                match.match.team1,
                match.match.score1.toString(),
                match.match.team2,
                match.match.score2.toString()
            )
            list.apply { add(entry) }
        }

        val viewAdapter = MatchesAdapter(entries)
        val viewManager = GridLayoutManager(context, 5, RecyclerView.VERTICAL, false)

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

    private fun dispatchEvents(event: MatchesViewModelEvent) {
        when (event) {
            is DeleteMatches -> deleteMatches()
            is NewMatch -> newMatch()
            is OpenMatch -> openMatch(event.matchUid)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_matches, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_matches -> {
                deleteMatches()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteMatches() {
        viewModel.launch {
            try {
                viewModel.deleteMatches()
            } catch (e: Exception) {
                logger.e(LOGGER_TAG, "Failed to delete matches", e)
            }
        }
    }

    private fun newMatch() = showDialog(NewMatchDialog())

    private fun openMatch(matchUid: Int) {
        val args = Bundle().apply { putInt(MATCH_UID, matchUid) }
        push(MatchController(args))
    }

}

