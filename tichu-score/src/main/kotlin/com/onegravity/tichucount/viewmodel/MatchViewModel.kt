package com.onegravity.tichucount.viewmodel

import androidx.lifecycle.ViewModel
import com.onegravity.tichucount.db.MatchRepository
import com.onegravity.tichucount.db.MatchWithGames
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

sealed class MatchViewModelEvent

object NewGame : MatchViewModelEvent()
data class OpenGame(val gameUid: Int) : MatchViewModelEvent()
data class DeleteGame(val gameUid: Int) : MatchViewModelEvent()

class MatchViewModel @Inject constructor(
    private val repository: MatchRepository
): ViewModel() {

    private val events = PublishSubject.create<MatchViewModelEvent>()

    fun events(): Observable<MatchViewModelEvent> = events

    fun match(matchUid: Int): Observable<MatchWithGames> = repository.getMatchesOld()
        .map { matches -> matches.first { it.match.uid == matchUid } }
        .subscribeOn(Schedulers.io())

    fun deleteMatch(matchUid: Int): Completable = repository.deleteMatch(matchUid)
        .subscribeOn(Schedulers.io())

    fun gameSelected(gameUid: Int) {
        events.onNext(OpenGame(gameUid))
    }

}