package com.onegravity.tichucount.view.game

import android.os.Bundle
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.viewpager2.RouterStateAdapter

class TeamScoreAdapter(controller: Controller) : RouterStateAdapter(controller) {

    override fun configureRouter(router: Router, position: Int) {
        if (!router.hasRootController()) {
            val args = Bundle()
            args.putSerializable(TEAM_ARG, position + 1)

            val page = TeamScoreController(args)
            router.setRoot(RouterTransaction.with(page))
        }
    }

    override fun getItemCount() = 2

}