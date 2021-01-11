package com.onegravity.tichucount

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor.attachRouter
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.onegravity.tichucount.databinding.ActivityMainBinding
import com.onegravity.tichucount.view.matches.MatchesController

class TichuActivity : AppCompatActivity() {

    private lateinit var router: Router

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityMainBinding.inflate(LayoutInflater.from(this)).run {
            binding = this
            setContentView(root)

            router = attachRouter(this@TichuActivity, controllerContainer, savedInstanceState)
            if (! router.hasRootController()) {
                router.setRoot(RouterTransaction.with(MatchesController()))
            }
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

}