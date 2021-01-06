package com.onegravity.tichucount.view.matches

import com.onegravity.tichucount.view.ListAdapter
import com.onegravity.tichucount.view.ListEntry
import com.onegravity.tichucount.viewmodel.MatchesViewModel

class MatchEntry(
    private val viewModel: MatchesViewModel,
    header: Boolean,
    val matchUid: Int,
    val matchNr: String,
    val team1Name: String,
    val team1Score: String,
    val team2Name: String,
    val team2Score: String
) : ListEntry(header) {
    override fun onClick() {
        if (! header) {
            viewModel.matchSelected(matchUid)
        }
    }
}

class MatchesAdapter(entries: List<MatchEntry>) : ListAdapter<MatchEntry>(entries) {

    override fun nrOfColumns() = 5

    override fun itemText(entry: MatchEntry, pos: Int) = when (pos) {
        0 -> entry.matchNr
        1 -> entry.team1Name
        2 -> entry.team1Score
        3 -> entry.team2Name
        else -> entry.team2Score
    }

}
