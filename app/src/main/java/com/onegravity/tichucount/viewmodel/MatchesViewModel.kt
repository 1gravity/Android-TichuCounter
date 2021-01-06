package com.onegravity.tichucount.viewmodel

import com.onegravity.tichucount.db.MatchRepository
import com.onegravity.tichucount.db.MatchWithGames
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

sealed class MatchesViewModelEvent

object DeleteMatches : MatchesViewModelEvent()
object NewMatch : MatchesViewModelEvent()
data class OpenMatch(val matchUid: Int) : MatchesViewModelEvent()

class MatchesViewModel @Inject constructor(private val repository: MatchRepository) {

    private val events = PublishSubject.create<MatchesViewModelEvent>()

    fun events(): Observable<MatchesViewModelEvent> = events

    fun matches(): Observable<List<MatchWithGames>> = repository.getMatches()
        .subscribeOn(Schedulers.io())

    fun createMatch(team1: String, team2: String): Single<Int> =
        repository.createMatch(team1, team2)
            .subscribeOn(Schedulers.io())
            .doOnSuccess { events.onNext(OpenMatch(it)) }

    fun deleteMatches(): Completable = repository.deleteMatches()
        .subscribeOn(Schedulers.io())

    fun matchSelected(matchUid: Int) {
        events.onNext(OpenMatch(matchUid))
    }

}