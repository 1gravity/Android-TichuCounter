package com.onegravity.tichucount.view.matches

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.onegravity.tichucount.APP_SCOPE
import com.onegravity.tichucount.R
import com.onegravity.tichucount.databinding.MatchesBinding
import com.onegravity.tichucount.db.MatchWithGames
import com.onegravity.tichucount.util.LOGGER_TAG
import com.onegravity.tichucount.util.Logger
import com.onegravity.tichucount.view.BaseController
import com.onegravity.tichucount.viewmodel.MatchViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject

class MatchesController : BaseController() {

    private lateinit var binding: MatchesBinding

    private val viewModel: MatchViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?)
            : View =
        MatchesBinding.inflate(inflater).run {
            scope.inject(this@MatchesController)
            binding = this
            setToolbar(binding.toolbar)
            setHasOptionsMenu(true)
            root
        }

    override fun onAttach(view: View) {
        super.onAttach(view)

        binding.toolbarLayout.title = appContext.getString((R.string.matches_title))
        binding.fab.setOnClickListener { newEntry() }

        viewModel.matches()
            .doOnSubscribe { subscriptions.add(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { bind(view.context, it) },
                { logger.e(LOGGER_TAG, "Failed to load matches", it) }
            )
    }

    private fun bind(context: Context, games: List<MatchWithGames>) {
        val title = MatchEntry(true,
            getString(R.string.match_nr),
            getString(R.string.name_team_1),
            getString(R.string.score),
            getString(R.string.name_team_2),
            getString(R.string.score)
        )
        val entries = games.foldIndexed(arrayListOf(title)) { pos, list, match ->
            list.apply {
                val entry = MatchEntry(false, pos.inc().toString(),
                    match.match.team1,
                    match.match.score1.toString(),
                    match.match.team2,
                    match.match.score2.toString())
                add(entry)
            }
        }

        val viewAdapter = MatchesAdapter(entries)
        val viewManager = LinearLayoutManager(context)

        binding.matchList.recyclerView.apply {
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

    private fun newEntry() = shoWDialog(NewMatchDialog())

    private fun deleteMatches() =
        viewModel.deleteMatches()
            .doOnSubscribe { disposables.add(it) }
            .subscribe()

}

