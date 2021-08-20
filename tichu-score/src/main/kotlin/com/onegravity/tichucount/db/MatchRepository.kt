package com.onegravity.tichucount.db

import com.onegravity.tichucount.util.LOGGER_TAG
import com.onegravity.tichucount.util.Logger
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Exception
import javax.inject.Inject

@DelicateCoroutinesApi
class MatchRepository @Inject constructor(
    private val db: TichuDatabase,
    private val logger: Logger
) {

    private val matchesOld = BehaviorSubject.create<List<MatchWithGames>>()

    private val matches = MutableStateFlow<List<MatchWithGames>>(emptyList())

    private val matchesByUid = HashMap<Int, MatchWithGames>()

    init {
        GlobalScope.launch {
            db.match()
                .getMatchesWithGames()
                .flowOn(Dispatchers.IO)
                .catch {
                    logger.e(LOGGER_TAG, "error while retrieving matches, it")
                }
                .collect { listOfMatches ->
                    logger.d(LOGGER_TAG, "Retrieved matches $listOfMatches")
                    matchesByUid.clear()
                    listOfMatches.forEach { matchesByUid[it.match.uid] = it }
                    matchesOld.onNext(listOfMatches)
                    matches.emit(listOfMatches)
                }
        }
    }

    fun getMatches(): Flow<List<MatchWithGames>> = matches

    fun getMatchesOld(): Observable<List<MatchWithGames>> = matchesOld

    fun getMatch(matchUid: Int): Single<MatchWithGames> = Single.create { emitter ->
        matchesByUid[matchUid]?.run {
            emitter.onSuccess(this)
        } ?:run {
            emitter.onError(NoSuchElementException("Didn't find match with uid $matchUid"))
        }
    }

    fun createMatch(team1: String, team2: String): Single<Int> = Single.create { emitter ->
        try {
            val match = Match(0, team1, team2, 1000,    0, 0)
            val uid = db.match().upsert(match)
            emitter.onSuccess(uid.toInt())
        } catch (e: Exception) {
            emitter.onError(e)
        }
    }

    fun deleteMatches(): Completable = Completable.create { emitter ->
        try {
            db.match().deleteMatches()
            emitter.onComplete()
        } catch (e: Exception) {
            emitter.onError(e)
        }
    }

    fun deleteMatch(matchUid: Int): Completable = Completable.create { emitter ->
        try {
            db.match().deleteMatch(matchUid)
            emitter.onComplete()
        } catch (e: Exception) {
            emitter.onError(e)
        }
    }

    fun deleteGame(matchUid: Int, gameUid: Int): Completable = Completable.create { emitter ->
        try {
            db.match().deleteGame(matchUid, gameUid)
            emitter.onComplete()
        } catch (e: Exception) {
            emitter.onError(e)
        }
    }

    fun saveGame(game: Game): Completable = Completable.create { emitter ->
        try {
            db.match().upsert(game)
            emitter.onComplete()
        } catch (e: Exception) {
            emitter.onError(e)
        }
    }

}