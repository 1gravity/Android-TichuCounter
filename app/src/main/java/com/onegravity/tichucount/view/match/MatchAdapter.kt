package com.onegravity.tichucount.view.match

import com.onegravity.tichucount.view.ListAdapter
import com.onegravity.tichucount.view.ListEntry
import com.onegravity.tichucount.viewmodel.MatchViewModel

class GameEntry(
    private val viewModel: MatchViewModel,
    header: Boolean,
    private val gameUid: Int,
    val gameNr: String,
    val team1Score: String,
    val team2Score: String
) : ListEntry(header) {
    override fun onClick() {
        if (! header) {
            viewModel.gameSelected(gameUid)
        }
    }
}

class MatchAdapter(entries: List<GameEntry>) : ListAdapter<GameEntry>(entries) {

    override fun nrOfColumns() = 3

    override fun itemText(entry: GameEntry, pos: Int) = when (pos) {
        0 -> entry.gameNr
        1 -> entry.team1Score
        else -> entry.team2Score
    }

}