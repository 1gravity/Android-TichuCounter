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
import com.onegravity.tichucount.view.BaseController
import com.onegravity.tichucount.view.game.GameController
import com.onegravity.tichucount.viewmodel.MatchViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import toothpick.ktp.delegate.inject

const val MATCH_UID = "MATCH_UID"

class MatchController(args: Bundle) : BaseController() {

    private lateinit var binding: GamesBinding

    private val viewModel: MatchViewModel by inject()

    private val matchUid = args.getInt(MATCH_UID)

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

        viewModel.matches()
            .doOnSubscribe { disposables.add(it) }
            .map { matches -> matches.first { it.match.uid == matchUid } }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( { match ->
                bind(view.context, match.match.team1, match.match.team2, match.games)
            }, {
                logger.e(LOGGER_TAG, "Failed to load last match", it)
            } )
    }

    private fun bind(context: Context, teamName1: String, teamName2: String, games: List<Game>) {
        binding.toolbarLayout.title = appContext.getString(R.string.match_title, teamName1, teamName2)
        binding.fab.setOnClickListener { newGame() }

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

    private fun newGame() {
        push(GameController())
    }

}
