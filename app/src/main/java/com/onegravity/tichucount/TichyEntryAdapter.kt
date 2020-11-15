package com.onegravity.tichucount

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

data class TichyEntry(val header: Boolean, val text1: String, val text2: String, val text3: String)

class TichuEntryViewHolder(
    private val rootLayout: LinearLayoutCompat,
    val text1: TextView = rootLayout.findViewById(R.id.entry_text_1),
    val text2: TextView = rootLayout.findViewById(R.id.entry_text_2),
    val text3: TextView = rootLayout.findViewById(R.id.entry_text_3),
) : RecyclerView.ViewHolder(rootLayout)

class TichyEntryAdapter(private val entries: List<TichyEntry>) :
    RecyclerView.Adapter<TichuEntryViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TichuEntryViewHolder {
        // create a new view
        val rootLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.tichu_entry, parent, false) as LinearLayoutCompat
        return TichuEntryViewHolder(rootLayout)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: TichuEntryViewHolder, position: Int) {
//        holder.text1.text = "Hello 1"
//        holder.text2.text = "Hello 2"
//        holder.text3.text = "Hello 3"
    }

    override fun getItemCount() = entries.size

}