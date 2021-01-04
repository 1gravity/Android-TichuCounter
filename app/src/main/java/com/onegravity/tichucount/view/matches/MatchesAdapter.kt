package com.onegravity.tichucount.view.matches

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onegravity.tichucount.databinding.MatchEntryBinding
import com.onegravity.tichucount.databinding.MatchEntryHeaderBinding

data class MatchEntry(val header: Boolean, val text1: String,
                      val text2: String, val text3: String,
                      val text4: String, val text5: String)

class MatchViewHolderHeader(val binding: MatchEntryHeaderBinding) :
    RecyclerView.ViewHolder(binding.root)

class MatchViewHolder(val binding: MatchEntryBinding) :
    RecyclerView.ViewHolder(binding.root)

class MatchesAdapter(private val entries: List<MatchEntry>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return if (position < 5) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType == 0) {
            true -> MatchEntryHeaderBinding.inflate(inflater).run { MatchViewHolderHeader(this) }
            else -> MatchEntryBinding.inflate(inflater).run { MatchViewHolder(this) }
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val row = position.div(5)
        entries[row].run {
            val text = when (position.rem(5)) {
                0 -> text1
                1 -> text2
                2 -> text3
                3 -> text4
                else -> text5
            }
            when (holder) {
                is MatchViewHolderHeader,  -> holder.binding.matchText
                is MatchViewHolder -> holder.binding.matchText
                else -> null
            }?.text = text
        }
    }

    override fun getItemCount() = entries.size.times(5)

}