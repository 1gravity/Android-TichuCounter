package com.onegravity.tichucount

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.onegravity.tichucount.databinding.TichuMainBinding
import com.onegravity.tichucount.newentry.EntryController

class TichuController : BaseController() {

    private lateinit var binding: TichuMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?)
            : View =
        TichuMainBinding.inflate(inflater).run {
            binding = this
            // yes very hacky but don't care at the moment
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
            setHasOptionsMenu(true)
            root
        }

    override fun onAttach(view: View) {
        super.onAttach(view)

        binding.toolbarLayout.title = view.context.getString((R.string.app_name))
        binding.fab.setOnClickListener { newEntry() }

        val viewManager = LinearLayoutManager(view.context)
        val entry1 = TichyEntry(true, "Count", "Team 1", "Team 2")
        val entry2 = TichyEntry(false, "#1", "60", "40")

        val entries = arrayListOf(entry1, entry2)
        val viewAdapter = TichyEntryAdapter(entries)

        binding.tichuScores.recyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_scrolling, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_new_game -> {
                newGame()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun newGame() {}

    private fun newEntry() {
        val tx = createRouterTx(EntryController())
        router.pushController(tx)
    }

}
