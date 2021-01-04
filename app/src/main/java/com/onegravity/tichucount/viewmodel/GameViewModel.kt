package com.onegravity.tichucount.viewmodel

import android.content.Context
import com.onegravity.tichucount.R
import com.onegravity.tichucount.db.MatchRepository
import com.onegravity.tichucount.model.Game
import com.onegravity.tichucount.model.Score
import com.onegravity.tichucount.model.ScoreState
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class GameViewModel @Inject constructor(
    context: Context,
    private val repository: MatchRepository
) {

    private var score1 = Score(
        ScoreState.NOT_PLAYED,
        ScoreState.NOT_PLAYED,
        false,
        0,
        context.getString(R.string.name_team_1)
    )

    private var score2 = Score(
        ScoreState.NOT_PLAYED,
        ScoreState.NOT_PLAYED,
        false,
        0,
        context.getString(R.string.name_team_2)
    )

    private var game = Game(score1, score2)

//    fun matches(): Flowable<List<MatchWithGames>> = repository.matches()
//        .subscribeOn(Schedulers.io())
//
    fun game() = repository.lastGame()
        .doOnNext {
//            it.games
            score1.reset()
            score2.reset()
            game = Game(score1, score2)
        }
        .subscribeOn(Schedulers.io())

}