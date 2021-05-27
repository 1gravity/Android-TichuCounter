package com.onegravity.tichucount.viewmodel

import com.onegravity.tichucount.db.MatchRepository
import com.onegravity.tichucount.model.Game
import com.onegravity.tichucount.model.Score
import com.onegravity.tichucount.model.ScoreState
import com.onegravity.tichucount.util.Logger
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class GameViewModel @Inject constructor(
    private val logger: Logger,
    private val repository: MatchRepository
) {

    private var theGame: com.onegravity.tichucount.db.Game? = null

    fun getGame(matchUid: Int, gameUid: Int): Single<Game> =
        repository.getMatch(matchUid)
            .map { match ->
                theGame = match.games.firstOrNull { it.uid == gameUid }
                theGame?.run {
                    val score1 = Score(score_1.tichu, score_1.grandTichu, score_1.doubleWin, score_1.playedPoints, match.match.team1, logger)
                    val score2 = Score(score_2.tichu, score_2.grandTichu, score_2.doubleWin, score_2.playedPoints, match.match.team2, logger)
                    Game(matchUid, match.match.team1, match.match.team2, score1, score2, logger)
                } ?: run {
                    // game doesn't exist -> create an empty one
                    val score1 = Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, match.match.team1, logger)
                    val score2 = Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, match.match.team2, logger)
                    Game(matchUid, match.match.team1, match.match.team2, score1, score2, logger)
                }
            }
            .subscribeOn(Schedulers.io())

    fun saveGame(game: Game): Completable {
        val game2Save = theGame?.run {
            // update existing game
            val score1 = getScore(score_1.uid, game.score1)
            val score2 = getScore(score_2.uid, game.score2)
            com.onegravity.tichucount.db.Game(uid, matchUid, score1, score2)
        } ?: run {
            // create new game
            val score1 = getScore(0, game.score1)
            val score2 = getScore(0, game.score2)
            com.onegravity.tichucount.db.Game(0, game.matchUid, score1, score2)
        }

        return repository.saveGame(game2Save)
            .subscribeOn(Schedulers.io())
            .doOnComplete { theGame = game2Save }
    }

    fun deleteGame(matchUid: Int, gameUid: Int): Completable =
        repository.deleteGame(matchUid, gameUid)
            .subscribeOn(Schedulers.io())

    private fun getScore(scoreUid: Int, score: Score) =
        com.onegravity.tichucount.db.Score(
            scoreUid,
            score.tichu,
            score.grandTichu,
            score.doubleWin,
            score.playedPoints,
            score.points()
        )

}