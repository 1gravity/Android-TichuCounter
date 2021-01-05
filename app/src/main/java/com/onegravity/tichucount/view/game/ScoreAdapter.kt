package com.onegravity.tichucount.view.game

import android.os.Bundle
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.viewpager2.RouterStateAdapter
import com.onegravity.tichucount.model.Game

class ScoreAdapter(controller: Controller, private val game: Game) : RouterStateAdapter(controller) {

    override fun configureRouter(router: Router, position: Int) {
        if (!router.hasRootController()) {
            val score = if (position == 0) game.score1 else game.score2
            val args = Bundle().apply { putSerializable(SCORE_ARG, score) }
            val page = ScoreController(args)
            router.setRoot(RouterTransaction.with(page))
        }
    }

    override fun getItemCount() = 2

}