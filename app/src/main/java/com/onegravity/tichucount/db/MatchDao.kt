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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(match: Match): Long

    @Query("DELETE FROM 'game'")
    fun deleteGames()

    @Query("DELETE FROM 'game' where matchUid = :matchUid")
    fun deleteGames(matchUid: Int)

    @Query("DELETE FROM 'game' where uid = :gameUid")
    fun deleteGame(gameUid: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(game: Game): Long

}
