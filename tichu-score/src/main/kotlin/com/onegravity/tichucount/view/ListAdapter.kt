package com.onegravity.tichucount.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onegravity.tichucount.databinding.ListEntryBinding
import com.onegravity.tichucount.databinding.ListEntryFooterBinding
import com.onegravity.tichucount.databinding.ListEntryHeaderBinding

abstract class ListEntry(val header: Boolean, val footer: Boolean) {
    abstract fun onClick()
}

class EntryViewHolderHeader(val binding: ListEntryHeaderBinding) : RecyclerView.ViewHolder(binding.root)

class EntryViewHolderFooter(val binding: ListEntryFooterBinding) : RecyclerView.ViewHolder(binding.root)

class EntryViewHolder(val binding: ListEntryBinding) : RecyclerView.ViewHolder(binding.root)

abstract class ListAdapter<E : ListEntry>(private val entries: List<E>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    abstract fun nrOfColumns(): Int

    override fun getItemViewType(position: Int) =
        entries[position.div(nrOfColumns())].run {
            when {
                header -> 0
                footer -> 1
                else -> 2
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> ListEntryHeaderBinding.inflate(inflater).run { EntryViewHolderHeader(this) }
            1 -> ListEntryFooterBinding.inflate(inflater).run { EntryViewHolderFooter(this) }
            else -> ListEntryBinding.inflate(inflater).run { EntryViewHolder(this) }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val row = position.div(nrOfColumns())
        entries[row].let { entry ->
            val text = itemText(entry, position.rem(nrOfColumns())).replaceFirstChar { Character.toUpperCase(it) }
            when (holder) {
                is EntryViewHolderHeader -> holder.binding.entryText
                is EntryViewHolderFooter -> holder.binding.entryText
                is EntryViewHolder -> holder.binding.entryText
                else -> null
            }?.run {
                this.text = text
                if (! entry.header && ! entry.footer) {
                    setOnClickListener { entry.onClick() }
                }
            }
        }
    }

    abstract fun itemText(entry: E, pos: Int): String

    override fun getItemCount() = entries.size.times(nrOfColumns())

}