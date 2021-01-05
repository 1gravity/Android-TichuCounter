package com.onegravity.tichucount.view.match

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.view.*
import com.onegravity.tichucount.R
import com.onegravity.tichucount.databinding.MainBinding
import com.onegravity.tichucount.db.MatchWithGames
import com.onegravity.tichucount.util.LOGGER_TAG
import com.onegravity.tichucount.view.BaseController
import com.onegravity.tichucount.view.game.GAME_UID
import com.onegravity.tichucount.view.game.GameController
import com.onegravity.tichucount.viewmodel.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import toothpick.ktp.delegate.inject

const val MATCH_UID = "MATCH_UID"

class MatchController(args: Bundle) : BaseController() {

    private lateinit var binding: MainBinding

    private val viewModel: MatchViewModel by inject()

    private val matchUid = args.getInt(MATCH_UID)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?)
            : View =
        MainBinding.inflate(inflater).run {
            scope.inject(this@MatchController)
            binding = this
            setToolbar(binding.toolbar)
            setHasOptionsMenu(true)
            root
        }

    override fun onAttach(view: View) {
        super.onAttach(view)

        setToolbar(binding.toolbar)

        viewModel.match(matchUid)
            .doOnSubscribe { disposables.add(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { bind(view.context, it) },
                { gameLoadError(it) }
            )

        viewModel.events()
            .doOnSubscribe { disposables.add(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { dispatchEvents(it) },
                { logger.e(LOGGER_TAG, "Failed to process events", it) }
            )
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

        val title = GameEntry(viewModel, true, 0,
            context.getString(R.string.game_nr), match.match.team1, match.match.team2
        )

        val entries =  match.games.foldIndexed(arrayListOf(title)) { pos, list, game ->
            val entry = GameEntry(viewModel, false,
                game.uid,
                pos.inc().toString(),
                game.score_1.totalPoints.toString(),
                game.score_2.totalPoints.toString())
            list.apply { add(entry) }
        }
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

    private fun deleteMatch() =
        viewModel.deleteMatch(matchUid)
            .doOnSubscribe { disposables.add(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { /* do nothing because the bind operation will terminate the Controller when it can't find the match */ },
                { logger.e(LOGGER_TAG, "Failed to delete match", it) }
            )

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
