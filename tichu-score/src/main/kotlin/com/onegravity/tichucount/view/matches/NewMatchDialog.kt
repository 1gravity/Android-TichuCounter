package com.onegravity.tichucount.view.matches

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.onegravity.tichucount.R
import com.onegravity.tichucount.util.LOGGER_TAG
import com.onegravity.tichucount.util.Logger
import com.onegravity.tichucount.util.launch
import com.onegravity.tichucount.viewmodel.MatchesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewMatchDialog : DialogFragment() {

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var viewModel: MatchesViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?) = context?.run {
        val layout = LayoutInflater.from(this).inflate(R.layout.new_match, null)

        AlertDialog.Builder(this)
            .setTitle(R.string.new_match_title)
            .setView(layout)
            .setPositiveButton(android.R.string.ok) { _, _ -> createMatch(layout) }
            .setNegativeButton(android.R.string.cancel) { _, _ -> dismiss()}
            .create()
    } ?: Dialog(requireContext())

    private fun createMatch(layout: View) {
        val name1 = layout.findViewById<EditText>(R.id.team_name_1).text.toString()
        val name2 = layout.findViewById<EditText>(R.id.team_name_2).text.toString()
        viewModel.launch(Dispatchers.IO) {
            try {
                viewModel.createMatch(name1, name2)
            } catch (e: Exception) {
                logger.e(LOGGER_TAG, "Failed to create new match", e)
            }
            launch(Dispatchers.Main.immediate) { dismiss() }
        }
    }
}
