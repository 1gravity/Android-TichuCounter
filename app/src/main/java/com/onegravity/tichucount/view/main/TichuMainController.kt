package com.onegravity.tichucount.view.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.onegravity.tichucount.R
import com.onegravity.tichucount.db.Game
import com.onegravity.tichucount.databinding.TichuMainBinding
import com.onegravity.tichucount.view.BaseController
import com.onegravity.tichucount.view.match.MatchController
import com.onegravity.tichucount.viewmodel.MatchViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject

class TichuMainController : BaseController() {

    private lateinit var binding: TichuMainBinding

    private val viewModel: MatchViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?)
            : View =
        TichuMainBinding.inflate(inflater).run {
            KTP.openRootScope().openSubScope(com.onegravity.tichucount.APP_SCOPE).inject(this@TichuMainController)
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

        viewModel.matches()
            .doOnSubscribe { subscriptions.add(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                it.lastOrNull()?.run {
                    bind(view.context, match.team1, match.team2, games)
                }
            }
    }

    private fun bind(context: Context, teamName1: String, teamName2: String, games: List<Game>) {
        val entries = arrayListOf(TichuEntry(true, context.getString(R.string.header_round), teamName1, teamName2))
        games.foldIndexed(entries) { pos, list, match ->
            list.apply {
                val entry = TichuEntry(false, pos.inc().toString(),
                    match.score_1.totalPoints.toString(),
                    match.score_2.totalPoints.toString())
                add(entry)
            }
        }
        val viewAdapter = TichuEntryAdapter(entries)
        val viewManager = LinearLayoutManager(context)

        binding.tichuScores.recyclerView.apply {
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
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_new_game -> {
                newGame()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun newGame() {
        viewModel.newMatch()
            .doOnSubscribe { disposables.add(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun newEntry() {
        val tx = createRouterTx(MatchController())
        router.pushController(tx)
    }

}
