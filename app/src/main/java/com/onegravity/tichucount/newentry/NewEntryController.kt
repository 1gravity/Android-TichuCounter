package com.onegravity.tichucount.newentry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.bluelinelabs.conductor.Controller
import com.onegravity.tichucount.R
import com.onegravity.tichucount.databinding.NewEntryBinding
import io.reactivex.rxjava3.subjects.BehaviorSubject

class NewEntryController : Controller() {

    private lateinit var binding: NewEntryBinding

    private val update = BehaviorSubject.create<Boolean>()

    private lateinit var tichu: Tichu
    private lateinit var bigTichu: BigTichu
    private lateinit var doubleWin: DoubleWin
    private lateinit var playedPoints: PlayedPoints
    private lateinit var totalPoints: TotalPoints

    private lateinit var components: ArrayList<Component>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?)
            : View =
        NewEntryBinding.inflate(inflater).run {
            binding = this
            tichu = Tichu(binding, update)
            bigTichu = BigTichu(binding, update)
            doubleWin = DoubleWin(binding, update)
            playedPoints = PlayedPoints(binding, update)
            totalPoints = TotalPoints(binding, update)
            components = arrayListOf(tichu, bigTichu, doubleWin, playedPoints, totalPoints)
            root
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
