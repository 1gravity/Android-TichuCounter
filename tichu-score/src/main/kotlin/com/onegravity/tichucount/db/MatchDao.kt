package com.onegravity.tichucount.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {

    @Transaction
    @Query("SELECT * FROM 'match'")
    fun getMatchesWithGames(): Flow<List<MatchWithGames>>

    /**
     * Delete all matches + games
     */
    @Transaction
    suspend fun deleteMatches() {
        deleteGamesInternal()
        deleteMatchesInternal()
    }
    @Query("DELETE FROM 'match'")
    suspend fun deleteMatchesInternal()
    @Query("DELETE FROM 'game'")
    suspend fun deleteGamesInternal()

    /**
     * Delete one match + games
     */
    @Transaction
    suspend fun deleteMatch(matchUid: Int) {
        deleteGamesInternal(matchUid)
        deleteMatchInternal(matchUid)
        updateTotals(matchUid)
    }
    @Query("DELETE FROM 'match' where uid = :matchUid")
    suspend fun deleteMatchInternal(matchUid: Int)
    @Query("DELETE FROM 'game' where matchUid = :matchUid")
    suspend fun deleteGamesInternal(matchUid: Int)

    /**
     * Delete one game
     */
    @Transaction
    suspend fun deleteGame(matchUid: Int, gameUid: Int) {
        deleteGameInternal(gameUid)
        updateTotals(matchUid)
    }
    @Query("DELETE FROM 'game' where uid = :gameUid")
    suspend fun deleteGameInternal(gameUid: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(match: Match): Long

    @Transaction
    suspend fun upsert(game: Game) = upsertInternal(game).apply { updateTotals(game.matchUid) }
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertInternal(game: Game): Long

    private suspend fun updateTotals(matchUid: Int) {
        updateTotal1(matchUid)
        updateTotal2(matchUid)
    }
    @Query("UPDATE 'match' SET score_team_1 = (SELECT COALESCE(SUM(team_1_total_points), 0) FROM 'game' WHERE matchUid = :matchUid) WHERE uid = :matchUid")
    suspend fun updateTotal1(matchUid: Int): Int
    @Query("UPDATE 'match' SET score_team_2 = (SELECT COALESCE(SUM(team_2_total_points), 0) FROM 'game' WHERE matchUid = :matchUid) WHERE uid = :matchUid")
    suspend fun updateTotal2(matchUid: Int): Int

}
