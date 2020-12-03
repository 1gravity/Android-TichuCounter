package com.onegravity.tichucount.data

import android.content.Context
import com.onegravity.tichucount.R
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class MatchRepository @Inject constructor(
    private val context: Context,
    private val db: TichuDatabase
) {

    fun newMatch() = Completable.create { emitter ->
        val match = Match(0, context.getString(R.string.name_team_1), context.getString(R.string.name_team_2), 10, 10)
        db.match().insertAll(match)
        emitter.onComplete()
    }

    fun matches(): Observable<MatchWithGames> = Observable.create<MatchWithGames> { emitter ->
        db.match().getMatchesWithGames().forEach {
            emitter.onNext(it)
        }
        emitter.onComplete()
    }

}