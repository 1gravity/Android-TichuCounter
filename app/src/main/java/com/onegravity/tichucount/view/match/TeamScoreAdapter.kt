package com.onegravity.tichucount.view.match

import android.os.Bundle
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.viewpager2.RouterStateAdapter
import com.onegravity.tichucount.viewmodel.MatchViewModel

class TeamScoreAdapter(private val viewModel: MatchViewModel, controller: Controller)
    : RouterStateAdapter(controller) {

    override fun configureRouter(router: Router, position: Int) {
        if (!router.hasRootController()) {
            val team1 = if (position == 0) viewModel.game.score1 else viewModel.game.score2

            val args = Bundle()
            args.putSerializable(TEAM_ARG, team1)

            val page = TeamScoreController(args)
            router.setRoot(RouterTransaction.with(page))
        }
    }

    override fun getItemCount() = 2

}