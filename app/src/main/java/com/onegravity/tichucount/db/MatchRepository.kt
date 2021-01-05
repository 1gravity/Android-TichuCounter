package com.onegravity.tichucount.db

import com.onegravity.tichucount.util.LOGGER_TAG
import com.onegravity.tichucount.util.Logger
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class MatchRepository @Inject constructor(
    private val db: TichuDatabase,
    private val logger: Logger
) {

    private val matches = BehaviorSubject.create<List<MatchWithGames>>()

    private val lastMatch = BehaviorSubject.create<MatchWithGames>()

    private val lastGame = BehaviorSubject.create<Game>()

    init {
        db.match().getMatchesWithGames()
            .doOnNext {
                matches.onNext(it)
                it.lastOrNull()?.run {
                    lastMatch.onNext(this)
                    games.lastOrNull()?.run { lastGame.onNext(this) }
                }
            }
            .subscribeOn(Schedulers.io())
            .doOnError {
                logger.e(LOGGER_TAG, "error while retrieving matches, it")
            }
            .subscribe()
    }

    fun matches(): Observable<List<MatchWithGames>> = matches

    fun lastMatch(): Observable<MatchWithGames> = lastMatch

    fun lastGame(): Observable<Game> = lastGame

    fun createMatch(team1: String, team2: String): Single<Match> =
        Single.create { emitter ->
            val match = Match(0, team1, team2, 0, 0)
            db.match().insert(match)
            emitter.onSuccess(match)
        }

    fun deleteMatches(): Completable = Completable.create { emitter ->
        db.match().deleteGames()
        db.match().deleteMatches()
        emitter.onComplete()
    }

}