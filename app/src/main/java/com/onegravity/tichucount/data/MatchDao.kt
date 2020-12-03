package com.onegravity.tichucount.data

import androidx.room.*

@Dao
interface MatchDao {

    @Transaction
    @Query("SELECT * FROM 'match'")
    fun getMatchesWithGames(): List<MatchWithGames>

    @Query("SELECT * FROM 'match'")
    fun getAll(): List<Match>

    @Query("SELECT * FROM 'match' WHERE uid IN (:matchIds)")
    fun loadAllByIds(matchIds: IntArray): List<Match>

    @Insert
    fun insertAll(vararg game: Match)

    @Delete
    fun delete(game: Match)

}
