package com.onegravity.tichucount.db

import com.onegravity.tichucount.util.LOGGER_TAG
import com.onegravity.tichucount.util.Logger
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.lang.Exception
import javax.inject.Inject

class MatchRepository @Inject constructor(
    private val db: TichuDatabase,
    private val logger: Logger
) {

    private val matches = BehaviorSubject.create<List<MatchWithGames>>()

    private val matchesByUid = HashMap<Int, MatchWithGames>()

    private val lastMatch = BehaviorSubject.create<MatchWithGames>()

    private val lastGame = BehaviorSubject.create<Game>()

    init {
        db.match().getMatchesWithGames()
            .doOnNext { listOfMatches ->
                matchesByUid.clear()
                listOfMatches.forEach { matchesByUid[it.match.uid] = it }

                matches.onNext(listOfMatches)
                listOfMatches.lastOrNull()?.run {
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

    fun createMatch(team1: String, team2: String): Single<Int> =
        Single.create { emitter ->
            val match = Match(0, team1, team2, 0, 0)
            val uid = db.match().insert(match)
            emitter.onSuccess(uid.toInt())
        }

    fun deleteMatches(): Completable = Completable.create { emitter ->
        db.match().deleteGames()
        db.match().deleteMatches()
        emitter.onComplete()
    }

    fun deleteMatch(matchUid: Int): Completable = Completable.create { emitter ->
        matchesByUid[matchUid]?.run {
            db.match().deleteGames(matchUid)
            db.match().deleteMatch(matchUid)
            emitter.onComplete()
        } ?: run {
            emitter.onError(Exception("match with $matchUid not found"))
        }
    }

}