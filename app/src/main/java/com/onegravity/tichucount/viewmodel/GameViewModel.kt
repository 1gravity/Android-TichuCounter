package com.onegravity.tichucount.viewmodel

import com.onegravity.tichucount.db.MatchRepository
import com.onegravity.tichucount.model.Game
import com.onegravity.tichucount.model.Score
import com.onegravity.tichucount.model.ScoreState
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class GameViewModel @Inject constructor(private val repository: MatchRepository) {

    private lateinit var theGame: Game

    fun game(matchUid: Int, gameUid: Int): Single<Game> = repository.match(matchUid)
        .map { match ->
            val game = match.games.firstOrNull { it.uid == gameUid }
            game?.run {
                val score1 = Score(game.score_1.tichu, game.score_1.bigTichu, game.score_1.doubleWin, game.score_1.playedPoints, match.match.team1)
                val score2 = Score(game.score_2.tichu, game.score_2.bigTichu, game.score_2.doubleWin, game.score_2.playedPoints, match.match.team2)
                theGame = Game(match.match.team1, match.match.team2, score1, score2)
            } ?:run {
                // game doesn't exist -> create an empty one
                val score1 = Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, match.match.team1)
                val score2 = Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, match.match.team2)
                theGame = Game(match.match.team1, match.match.team2, score1, score2)
            }
            theGame
        }
        .subscribeOn(Schedulers.io())

}