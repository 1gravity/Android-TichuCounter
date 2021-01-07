package com.onegravity.tichucount.db

import androidx.room.*
import io.reactivex.rxjava3.core.Flowable

@Dao
interface MatchDao {

    @Transaction
    @Query("SELECT * FROM 'match'")
    fun getMatchesWithGames(): Flowable<List<MatchWithGames>>

    /**
     * Delete all matches + games
     */
    @Transaction
    fun deleteMatches() {
        deleteGamesInternal()
        deleteMatchesInternal()
    }
    @Query("DELETE FROM 'match'")
    fun deleteMatchesInternal()
    @Query("DELETE FROM 'game'")
    fun deleteGamesInternal()

    /**
     * Delete one match + games
     */
    @Transaction
    fun deleteMatch(matchUid: Int) {
        deleteGamesInternal(matchUid)
        deleteMatchInternal(matchUid)
        updateTotals(matchUid)
    }
    @Query("DELETE FROM 'match' where uid = :matchUid")
    fun deleteMatchInternal(matchUid: Int)
    @Query("DELETE FROM 'game' where matchUid = :matchUid")
    fun deleteGamesInternal(matchUid: Int)

    /**
     * Delete one game
     */
    @Transaction
    fun deleteGame(matchUid: Int, gameUid: Int) {
        deleteGameInternal(gameUid)
        updateTotals(matchUid)
    }
    @Query("DELETE FROM 'game' where uid = :gameUid")
    fun deleteGameInternal(gameUid: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(match: Match): Long

    @Transaction
    fun upsert(game: Game) = upsertInternal(game).apply { updateTotals(game.matchUid) }
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertInternal(game: Game): Long

    private fun updateTotals(matchUid: Int) {
        updateTotal1(matchUid)
        updateTotal2(matchUid)
    }
    @Query("UPDATE 'match' SET score_team_1 = (SELECT COALESCE(SUM(team_1_total_points), 0) FROM 'game' WHERE matchUid = :matchUid) WHERE uid = :matchUid")
    fun updateTotal1(matchUid: Int): Int
    @Query("UPDATE 'match' SET score_team_2 = (SELECT COALESCE(SUM(team_2_total_points), 0) FROM 'game' WHERE matchUid = :matchUid) WHERE uid = :matchUid")
    fun updateTotal2(matchUid: Int): Int

}
