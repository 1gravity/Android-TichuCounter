package com.onegravity.tichucount.entry

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
import com.onegravity.tichucount.entry.viewmodel.EntryViewModel

open class EntryController: Controller() {

    private lateinit var binding: EntryBinding

    private lateinit var viewModel: EntryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ) = EntryBinding.inflate(inflater).run {
        binding = this
        viewModel = EntryViewModel(container.context)
        bindView(container.context, this)
        root
    }

    private fun bindView(context: Context, binding: EntryBinding) {
        binding.viewpager.adapter = EntryAdapter(viewModel, this@EntryController)

        TabLayoutMediator(
            binding.tabLayout, binding.viewpager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = when (position) {
                0 -> context.getString(R.string.header_team_1)
                else -> context.getString(R.string.header_team_2)
            }
        }.attach()
    }

    override fun onAttach(view: View) {
        super.onAttach(view)

        activity?.findViewById<Toolbar>(R.id.toolbar)
            ?.setNavigationOnClickListener { router.handleBack() }
    }

    override fun onDetach(view: View) {
        super.onDetach(view)

        if (!activity!!.isChangingConfigurations) {
            binding.viewpager.adapter = null
        }
        binding.tabLayout.setupWithViewPager(null)
    }

}