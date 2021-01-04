package com.onegravity.tichucount.view.match

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onegravity.tichucount.databinding.GameEntryBinding

data class GameEntry(val header: Boolean, val text1: String, val text2: String, val text3: String)

class GameEntryViewHolder(
    val binding: GameEntryBinding,
) : RecyclerView.ViewHolder(binding.root)

class MatchAdapter(private val entries: List<GameEntry>) :
    RecyclerView.Adapter<GameEntryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GameEntryBinding.inflate(LayoutInflater.from(parent.context)).run {
            GameEntryViewHolder(this)
        }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: GameEntryViewHolder, position: Int) {
        entries[position].run {
            holder.binding.entryText1.text = text1
            holder.binding.entryText2.text = text2
            holder.binding.entryText3.text = text3
        }
    }

    override fun getItemCount() = entries.size

}