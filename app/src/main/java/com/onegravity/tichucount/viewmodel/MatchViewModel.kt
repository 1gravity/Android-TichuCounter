package com.onegravity.tichucount.viewmodel

import android.content.Context
import com.onegravity.tichucount.R
import com.onegravity.tichucount.db.MatchRepository
import com.onegravity.tichucount.db.MatchWithGames
import com.onegravity.tichucount.model.Game
import com.onegravity.tichucount.model.Score
import com.onegravity.tichucount.model.ScoreState
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MatchViewModel @Inject constructor(
    private val context: Context,
    private val repository: MatchRepository
) {

    private val team1 = Score(
        ScoreState.NOT_PLAYED,
        ScoreState.NOT_PLAYED,
        false,
        0,
        context.getString(R.string.name_team_1)
    )

    private val team2 = Score(
        ScoreState.NOT_PLAYED,
        ScoreState.NOT_PLAYED,
        false,
        0,
        context.getString(R.string.name_team_2)
    )

    val game = Game(team1, team2)

    fun matches(): Flowable<List<MatchWithGames>> = repository.matches()
        .subscribeOn(Schedulers.io())

    fun newMatch() = repository.newMatch()
        .subscribeOn(Schedulers.io())

}