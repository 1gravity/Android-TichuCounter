package com.onegravity.tichucount.viewmodel

import com.onegravity.tichucount.db.Match
import com.onegravity.tichucount.db.MatchRepository
import com.onegravity.tichucount.db.MatchWithGames
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MatchViewModel @Inject constructor(private val repository: MatchRepository) {

    fun matches(): Flowable<List<MatchWithGames>> = repository.matches()
        .subscribeOn(Schedulers.io())

    fun lastMatch(): Observable<MatchWithGames> = repository.lastMatch()
        .subscribeOn(Schedulers.io())

    fun createMatch(team1: String, team2: String): Single<Match> = repository.createMatch(team1, team2)
        .subscribeOn(Schedulers.io())

    fun deleteMatches(): Completable = repository.deleteMatches()
        .subscribeOn(Schedulers.io())

}