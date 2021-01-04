package com.onegravity.tichucount.view.match

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.onegravity.tichucount.R
import com.onegravity.tichucount.databinding.GamesBinding
import com.onegravity.tichucount.db.Game
import com.onegravity.tichucount.util.LOGGER_TAG
import com.onegravity.tichucount.util.Logger
import com.onegravity.tichucount.view.BaseController
import com.onegravity.tichucount.view.game.GameController
import com.onegravity.tichucount.viewmodel.MatchViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import toothpick.ktp.delegate.inject

class MatchController : BaseController() {

    private lateinit var binding: GamesBinding

    private val viewModel: MatchViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?)
            : View =
        GamesBinding.inflate(inflater).run {
            scope.inject(this@MatchController)
            binding = this
            // yes very hacky but don't care at the moment
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
            setHasOptionsMenu(true)
            root
        }

    override fun onAttach(view: View) {
        super.onAttach(view)

        binding.toolbarLayout.title = view.context.getString((R.string.app_name))
        binding.fab.setOnClickListener { newEntry() }

        viewModel.lastMatch()
            .doOnSubscribe { disposables.add(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {
                bind(view.context, it.match.team1, it.match.team2, it.games)
            }, {
                logger.e(LOGGER_TAG, "Failed to load last match", it)
            } )
    }

    private fun bind(context: Context, teamName1: String, teamName2: String, games: List<Game>) {
        val entries = arrayListOf(GameEntry(true, context.getString(R.string.header_round), teamName1, teamName2))
        games.foldIndexed(entries) { pos, list, match ->
            list.apply {
                val entry = GameEntry(false, pos.inc().toString(),
                    match.score_1.totalPoints.toString(),
                    match.score_2.totalPoints.toString())
                add(entry)
            }
        }
        val viewAdapter = MatchAdapter(entries)
        val viewManager = LinearLayoutManager(context)

        binding.gameList.recyclerView.apply {
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
            R.id.action_new_match -> {
                newMatch()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun newMatch() {
        viewModel.createMatch(appContext.getString(R.string.name_team_1), appContext.getString(R.string.name_team_2))
            .doOnSubscribe { disposables.add(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun newEntry() {
        val tx = createRouterTx(GameController())
        router.pushController(tx)
    }

}
