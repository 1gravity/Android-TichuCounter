package com.onegravity.tichucount.view.matches

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onegravity.tichucount.databinding.MatchEntryBinding
import com.onegravity.tichucount.databinding.MatchEntryHeaderBinding
import com.onegravity.tichucount.viewmodel.MatchViewModel

data class MatchEntry(
    private val viewModel: MatchViewModel,
    val header: Boolean,
    val matchUid: Int, val matchNr: String,
    val team1Name: String, val team1Score: String,
    val team2Name: String, val team2Score: String
) {
    fun onClick() {
        if (! header) {
            viewModel.matchSelected(matchUid)
        }
    }
}

class MatchViewHolderHeader(val binding: MatchEntryHeaderBinding) :
    RecyclerView.ViewHolder(binding.root)

class MatchViewHolder(val binding: MatchEntryBinding) :
    RecyclerView.ViewHolder(binding.root)

class MatchesAdapter(private val entries: List<MatchEntry>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int) =
        entries[position.div(5)].run {
            if (this.header) 0 else 1
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType == 0) {
            true -> MatchEntryHeaderBinding.inflate(inflater).run { MatchViewHolderHeader(this) }
            else -> MatchEntryBinding.inflate(inflater).run { MatchViewHolder(this) }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val row = position.div(5)
        entries[row].let { entry ->
            val text = when (position.rem(5)) {
                0 -> entry.matchNr
                1 -> entry.team1Name
                2 -> entry.team1Score
                3 -> entry.team2Name
                else -> entry.team2Score
            }
            when (holder) {
                is MatchViewHolderHeader,  -> holder.binding.matchText
                is MatchViewHolder -> holder.binding.matchText
                else -> null
            }?.run {
                this.text = text
                this.setOnClickListener { entry.onClick() }
            }
        }
    }

    override fun getItemCount() = entries.size.times(5)

}