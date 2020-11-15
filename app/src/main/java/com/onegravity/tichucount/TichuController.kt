package com.onegravity.tichucount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluelinelabs.conductor.Controller
import com.onegravity.tichucount.databinding.TichuScoresBinding

class TichuController : Controller() {

    private lateinit var binding: TichuScoresBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?)
            : View =
        TichuScoresBinding.inflate(inflater).run {
            binding = this
            this.root
        }

    override fun onAttach(view: View) {
        super.onAttach(view)

        val viewManager = LinearLayoutManager(view.context)
        val entry1 = TichyEntry(true, "Count", "Team 1", "Team 2")
        val entry2 = TichyEntry(false, "#1", "60", "40")

        val entries = arrayListOf(entry1, entry2)
        val viewAdapter = TichyEntryAdapter(entries)

        binding.recyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }

}
