package com.onegravity.tichucount

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor.attachRouter
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.onegravity.tichucount.databinding.ActivityMainBinding

class TichuActivity : AppCompatActivity() {

    private lateinit var router: Router

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityMainBinding.inflate(LayoutInflater.from(this)).run {
            binding = this
            setContentView(root)
        }

        val container = binding.controllerContainer
        router = attachRouter(this, container, savedInstanceState)
        if (! router.hasRootController()) {
            router.setRoot(RouterTransaction.with(TichuController()))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

}