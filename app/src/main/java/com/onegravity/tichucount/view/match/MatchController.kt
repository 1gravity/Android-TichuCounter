package com.onegravity.tichucount.view.match

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.bluelinelabs.conductor.Controller
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.onegravity.tichucount.R
import com.onegravity.tichucount.databinding.EntryBinding
import com.onegravity.tichucount.viewmodel.MatchViewModel
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject

open class MatchController: Controller() {

    private lateinit var binding: EntryBinding

    private val viewModel: MatchViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ) = EntryBinding.inflate(inflater).run {
        KTP.openRootScope().openSubScope(com.onegravity.tichucount.APP_SCOPE).inject(this@MatchController)
        binding = this
        bindView(container.context, this)
        root
    }

    private fun bindView(context: Context, binding: EntryBinding) {
        binding.viewpager.adapter = TeamScoreAdapter(viewModel, this@MatchController)
        binding.viewpager.offscreenPageLimit = 1

        TabLayoutMediator(
            binding.tabLayout, binding.viewpager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = when (position) {
                0 -> context.getString(R.string.name_team_1)
                else -> context.getString(R.string.name_team_2)
            }
        }.attach()
    }

    override fun onAttach(view: View) {
        super.onAttach(view)

        activity?.findViewById<Toolbar>(R.id.toolbar)
            ?.setNavigationOnClickListener { router.handleBack() }

        binding.btnNegative.setOnClickListener {
            router.popCurrentController()
        }

        binding.btnPositive.setOnClickListener {
            viewModel.game
        }
    }

    override fun onDetach(view: View) {
        super.onDetach(view)

        if (!activity!!.isChangingConfigurations) {
            binding.viewpager.adapter = null
        }
        binding.tabLayout.setupWithViewPager(null)
    }

}
