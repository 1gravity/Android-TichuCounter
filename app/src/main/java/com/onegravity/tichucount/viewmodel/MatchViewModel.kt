package com.onegravity.tichucount.viewmodel

import com.onegravity.tichucount.db.Match
import com.onegravity.tichucount.db.MatchRepository
import com.onegravity.tichucount.db.MatchWithGames
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

sealed class MatchViewModelEvent

object NewGame : MatchViewModelEvent()
data class OpenGame(val gameUid: Int) : MatchViewModelEvent()
data class DeleteGame(val gameUid: Int) : MatchViewModelEvent()

class MatchViewModel @Inject constructor(
    private val repository: MatchRepository
) {

    private val events = PublishSubject.create<MatchViewModelEvent>()

    fun events(): Observable<MatchViewModelEvent> = events

    fun matches(): Observable<List<MatchWithGames>> = repository.matches()
        .subscribeOn(Schedulers.io())

    fun match(matchUid: Int): Observable<MatchWithGames> = repository.matches()
        .map { matches -> matches.first { it.match.uid == matchUid } }
        .subscribeOn(Schedulers.io())

    fun lastMatch(): Observable<MatchWithGames> = repository.lastMatch()
        .subscribeOn(Schedulers.io())

    fun createMatch(team1: String, team2: String): Single<Int> =
        repository.createMatch(team1, team2)
        .subscribeOn(Schedulers.io())

    fun deleteMatch(matchUid: Int): Completable = repository.deleteMatch(matchUid)
        .subscribeOn(Schedulers.io())

    fun gameSelected(gameUid: Int) {
        events.onNext(OpenGame(gameUid))
    }

}