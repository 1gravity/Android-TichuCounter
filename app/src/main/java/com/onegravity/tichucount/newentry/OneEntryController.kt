package com.onegravity.tichucount.newentry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.appcompat.widget.Toolbar
import com.bluelinelabs.conductor.Controller
import com.onegravity.tichucount.R
import com.onegravity.tichucount.databinding.OneEntryBinding
import com.onegravity.tichucount.newentry.components.*
import io.reactivex.rxjava3.subjects.BehaviorSubject

class OneEntryController(@Nullable args: Bundle): Controller(args) {

    private val update = BehaviorSubject.create<Boolean>()

    private lateinit var tichu: Tichu
    private lateinit var bigTichu: BigTichu
    private lateinit var doubleWin: DoubleWin
    private lateinit var playedPoints: PlayedPoints
    private lateinit var totalPoints: TotalPoints

    private lateinit var components: ArrayList<Component>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ) = OneEntryBinding.inflate(inflater).run {
        bindView(this)
        root
    }

    private fun bindView(binding: OneEntryBinding) {
        tichu = Tichu(binding, update)
        bigTichu = BigTichu(binding, update)
        doubleWin = DoubleWin(binding, update)
        playedPoints = PlayedPoints(binding, update)
        totalPoints = TotalPoints(binding, update, playedPoints)
        components = arrayListOf(tichu, bigTichu, doubleWin, playedPoints, totalPoints)
        totalPoints.update()
    }

    override fun onAttach(view: View) {
        super.onAttach(view)

        activity?.findViewById<Toolbar>(R.id.toolbar)
            ?.setNavigationOnClickListener { router.handleBack() }

        components.forEach { it.start() }
    }

    override fun onDetach(view: View) {
        super.onDetach(view)

        components.forEach { it.stop() }
    }

}
