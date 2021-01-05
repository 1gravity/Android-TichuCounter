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
data class MatchOpen(val matchUid: Int) : MatchViewModelEvent()

class MatchViewModel @Inject constructor(
    private val repository: MatchRepository
) {

    private val events = PublishSubject.create<MatchViewModelEvent>()

    fun events(): Observable<MatchViewModelEvent> = events

    fun matches(): Observable<List<MatchWithGames>> = repository.matches()
        .subscribeOn(Schedulers.io())

    fun lastMatch(): Observable<MatchWithGames> = repository.lastMatch()
        .subscribeOn(Schedulers.io())

    fun createMatch(team1: String, team2: String): Single<Match> =
        repository.createMatch(team1, team2)
        .subscribeOn(Schedulers.io())

    fun deleteMatches(): Completable = repository.deleteMatches()
        .subscribeOn(Schedulers.io())

    fun matchSelected(matchUid: Int) {
        events.onNext(MatchOpen(matchUid))
    }

}