package com.onegravity.tichucount.view.matches

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onegravity.tichucount.R
import com.onegravity.tichucount.databinding.MainBinding
import com.onegravity.tichucount.db.MatchWithGames
import com.onegravity.tichucount.util.LOGGER_TAG
import com.onegravity.tichucount.view.BaseController
import com.onegravity.tichucount.view.match.MATCH_UID
import com.onegravity.tichucount.view.match.MatchController
import com.onegravity.tichucount.viewmodel.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import toothpick.ktp.delegate.inject

class MatchesController : BaseController() {

    private lateinit var binding: MainBinding

    private val viewModel: MatchesViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?)
            : View =
        MainBinding.inflate(inflater).run {
            scope.inject(this@MatchesController)
            binding = this
            setToolbar(binding.toolbar)
            setHasOptionsMenu(true)
            root
        }

    override fun onAttach(view: View) {
        super.onAttach(view)

        binding.toolbarLayout.title = appContext.getString((R.string.matches_title))
        binding.fab.setOnClickListener { newMatch() }

        viewModel.matches()
            .doOnSubscribe { disposables.add(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { bind(view.context, it) },
                { logger.e(LOGGER_TAG, "Failed to load matches", it) }
            )

        viewModel.events()
            .doOnSubscribe { disposables.add(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { dispatchEvents(it) },
                { logger.e(LOGGER_TAG, "Failed to process events", it) }
            )
    }

    private fun dispatchEvents(event: MatchesViewModelEvent) {
        when (event) {
            is DeleteMatches -> deleteMatches()
            is NewMatch -> newMatch()
            is OpenMatch -> openMatch(event.matchUid)
        }
    }

    private fun bind(context: Context, games: List<MatchWithGames>) {
        val header = MatchEntry(viewModel, true, 0,
            getString(R.string.match_nr),
            getString(R.string.name_team_1),
            getString(R.string.score),
            getString(R.string.name_team_2),
            getString(R.string.score)
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

    private fun deleteMatches() =
        viewModel.deleteMatches()
            .doOnSubscribe { disposables.add(it) }
            .subscribe(
                { /* do nothing */ },
                { logger.e(LOGGER_TAG, "Failed to delete matches", it) }
            )

    private fun newMatch() = shoWDialog(NewMatchDialog())

    private fun openMatch(matchUid: Int) {
        val args = Bundle().apply { putInt(MATCH_UID, matchUid) }
        push(MatchController(args))
    }

}

