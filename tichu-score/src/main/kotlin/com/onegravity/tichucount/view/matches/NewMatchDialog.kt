package com.onegravity.tichucount.view.matches

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.onegravity.tichucount.R
import com.onegravity.tichucount.util.LOGGER_TAG
import com.onegravity.tichucount.util.Logger
import com.onegravity.tichucount.viewmodel.MatchesViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
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
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val name1 = layout.findViewById<EditText>(R.id.team_name_1).text.toString()
                val name2 = layout.findViewById<EditText>(R.id.team_name_2).text.toString()
                viewModel.createMatch(name1, name2)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { dismiss() },
                        {
                            logger.e(LOGGER_TAG, "Failed to create new match", it)
                            dismiss()
                        }
                    )
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> dismiss()}
            .create()
    } ?: Dialog(requireContext())

}
