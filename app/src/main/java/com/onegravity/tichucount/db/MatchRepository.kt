package com.onegravity.tichucount.db

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class MatchRepository @Inject constructor(private val db: TichuDatabase) {

    private val lastMatch = BehaviorSubject.create<MatchWithGames>()

    private val lastGame = BehaviorSubject.create<Game>()

    fun matches(): Flowable<List<MatchWithGames>> = db.match().getMatchesWithGames()
        .doOnNext { matches ->
            matches.lastOrNull()?.run {
                lastMatch.onNext(this)
                games.lastOrNull()?.run { lastGame.onNext(this) }
            }
        }

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