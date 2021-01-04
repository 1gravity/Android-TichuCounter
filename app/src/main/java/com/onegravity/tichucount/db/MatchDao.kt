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

    @Query("DELETE FROM 'game'")
    fun deleteGames()

    @Insert
    fun insert(vararg match: Match)

    @Delete
    fun delete(match: Match)

}
