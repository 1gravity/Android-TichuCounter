package com.onegravity.tichucount.db

import android.content.Context
import com.onegravity.tichucount.R
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class MatchRepository @Inject constructor(
    private val context: Context,
    private val db: TichuDatabase
) {

    fun newMatch(): Single<Match> =
        Single.create { emitter ->
            val match = Match(0, context.getString(R.string.name_team_1), context.getString(R.string.name_team_2), 0, 0)
            db.match().insert(match)
            emitter.onSuccess(match)
        }

    fun matches(): Flowable<List<MatchWithGames>> =
        db.match().getMatchesWithGames()

}