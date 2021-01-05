package com.onegravity.tichucount.db

import androidx.room.*
import io.reactivex.rxjava3.core.Flowable

@Dao
interface MatchDao {

    @Transaction
    @Query("SELECT * FROM 'match'")
    fun getMatchesWithGames(): Flowable<List<MatchWithGames>>

    @Query("DELETE FROM 'match'")
    fun deleteMatches()

    @Query("DELETE FROM 'match' where uid = :matchUid")
    fun deleteMatch(matchUid: Int)

    @Query("DELETE FROM 'game' where uid = :matchUid")
    fun deleteGames(matchUid: Int)

    @Query("DELETE FROM 'game'")
    fun deleteGames()

    @Insert
    fun insert(match: Match): Long

}
