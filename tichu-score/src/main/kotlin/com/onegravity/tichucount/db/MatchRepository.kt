package com.onegravity.tichucount.db

import com.onegravity.tichucount.util.LOGGER_TAG
import com.onegravity.tichucount.util.Logger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MatchRepository @Inject constructor(
    private val db: TichuDatabase,
    private val logger: Logger
) {

    suspend fun getMatches() = flow {
        db.match()
            .getMatchesWithGames()
            .catch { logger.e(LOGGER_TAG, "error while retrieving matches", it) }
            .collect { emit(it) }
    }

    suspend fun getMatch(matchUid: Int) = coroutineScope {
        getMatches()
            .first()
            .first { it.match.uid == matchUid }
    }

    suspend fun createMatch(team1: String, team2: String) = coroutineScope {
        val match = Match(0, team1, team2, 1000,    0, 0)
        val uid = db.match().upsert(match)
        uid.toInt()
    }

    suspend fun deleteMatches() = coroutineScope {
        db.match().deleteMatches()
    }

    suspend fun deleteMatch(matchUid: Int) {
        db.match().deleteMatch(matchUid)
    }

    suspend fun deleteGame(matchUid: Int, gameUid: Int) {
        db.match().deleteGame(matchUid, gameUid)
    }

    suspend fun saveGame(game: Game) = coroutineScope {
        db.match().upsert(game)
    }

}