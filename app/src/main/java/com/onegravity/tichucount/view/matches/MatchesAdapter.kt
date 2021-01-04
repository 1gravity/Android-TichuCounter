package com.onegravity.tichucount.view.matches

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onegravity.tichucount.databinding.MatchEntryBinding

data class MatchEntry(val header: Boolean, val text1: String,
                      val text2: String, val text3: String,
                      val text4: String, val text5: String)

class MatchViewHolder(
    val binding: MatchEntryBinding,
) : RecyclerView.ViewHolder(binding.root)

class MatchesAdapter(private val entries: List<MatchEntry>) :
    RecyclerView.Adapter<MatchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MatchEntryBinding.inflate(LayoutInflater.from(parent.context)).run {
            MatchViewHolder(this)
        }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        entries[position].run {
            holder.binding.entryText1.text = text1
            holder.binding.entryText2.text = text2
            holder.binding.entryText3.text = text3
            holder.binding.entryText4.text = text4
            holder.binding.entryText5.text = text5
        }
    }

    override fun getItemCount() = entries.size

}