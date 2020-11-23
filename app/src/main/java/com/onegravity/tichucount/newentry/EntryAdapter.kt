package com.onegravity.tichucount.newentry

import android.os.Bundle
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.viewpager2.RouterStateAdapter

class EntryAdapter(controller: Controller) : RouterStateAdapter(controller) {

    override fun configureRouter(router: Router, position: Int) {
        if (!router.hasRootController()) {
            val args = Bundle()
            val page = OneEntryController(args)
            router.setRoot(RouterTransaction.with(page))
        }
    }

    override fun getItemCount() = 2

}