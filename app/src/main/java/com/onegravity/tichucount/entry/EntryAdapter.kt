package com.onegravity.tichucount.entry

import android.os.Bundle
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.viewpager2.RouterStateAdapter
import com.onegravity.tichucount.entry.viewmodel.EntryViewModel

class EntryAdapter(private val viewModel: EntryViewModel, controller: Controller) : RouterStateAdapter(controller) {

    override fun configureRouter(router: Router, position: Int) {
        if (!router.hasRootController()) {
            val args = Bundle()
            val team = when (position) {
                0 -> viewModel.team1
                else -> viewModel.team2
            }
            args.putSerializable(TEAM_ARG, team)
            val page = OneEntryController(args)
            router.setRoot(RouterTransaction.with(page))
        }
    }

    override fun getItemCount() = 2

}