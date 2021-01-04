package com.onegravity.tichucount.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onegravity.tichucount.databinding.TichuEntryBinding

data class TichuEntry(val header: Boolean, val text1: String, val text2: String, val text3: String)

class TichuEntryViewHolder(
    val binding: TichuEntryBinding,
) : RecyclerView.ViewHolder(binding.root)

class TichuEntryAdapter(private val entries: List<TichuEntry>) :
    RecyclerView.Adapter<TichuEntryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TichuEntryBinding.inflate(LayoutInflater.from(parent.context)).run {
            TichuEntryViewHolder(this)
        }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: TichuEntryViewHolder, position: Int) {
        entries[position].run {
            holder.binding.entryText1.text = text1
            holder.binding.entryText2.text = text2
            holder.binding.entryText3.text = text3
        }
    }

    override fun getItemCount() = entries.size

}