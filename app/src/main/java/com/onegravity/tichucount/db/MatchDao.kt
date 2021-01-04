package com.onegravity.tichucount.db

import androidx.room.*
import io.reactivex.rxjava3.core.Flowable

@Dao
interface MatchDao {

    @Transaction
    @Query("SELECT * FROM 'match'")
    fun getMatchesWithGames(): Flowable<List<MatchWithGames>>

    @Query("SELECT * FROM 'match' WHERE uid IN (:matchIds)")
    fun loadAllByIds(matchIds: IntArray): List<Match>

    @Insert
    fun insert(vararg match: Match)

    @Delete
    fun delete(match: Match)

}
