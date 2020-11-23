package com.onegravity.tichucount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor.attachRouter
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.onegravity.tichucount.databinding.ActivityMainBinding
import com.onegravity.tichucount.newentry.EntryController

class TichuActivity : AppCompatActivity() {

    private lateinit var router: Router

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityMainBinding.inflate(LayoutInflater.from(this)).run {
            binding = this
            setContentView(root)
        }

        val container = binding.tichuMain.controllerContainer
        router = attachRouter(this, container, savedInstanceState)
        if (! router.hasRootController()) {
            router.setRoot(RouterTransaction.with(TichuController()))
        }

        setSupportActionBar(binding.toolbar)

        binding.toolbarLayout.title = title

        binding.fab.setOnClickListener { newEntry() }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
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

    private fun createRouterTx(controller: Controller) =
        RouterTransaction.with(controller)
            .tag(controller.javaClass.simpleName)
            .pushChangeHandler(FadeChangeHandler())
            .popChangeHandler(FadeChangeHandler())

}