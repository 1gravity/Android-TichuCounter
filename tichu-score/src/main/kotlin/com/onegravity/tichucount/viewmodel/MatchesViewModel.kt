package com.onegravity.tichucount.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onegravity.tichucount.db.MatchRepository
import com.onegravity.tichucount.db.MatchWithGames
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MatchesViewModelEvent

object DeleteMatches : MatchesViewModelEvent()
object NewMatch : MatchesViewModelEvent()
data class OpenMatch(val matchUid: Int) : MatchesViewModelEvent()

class MatchesViewModel @Inject constructor(
    private val repository: MatchRepository
): ViewModel() {

    private val events = MutableSharedFlow<MatchesViewModelEvent>(replay = 0)

    fun events(): Flow<MatchesViewModelEvent> = events

    suspend fun matches(): Flow<List<MatchWithGames>> = repository.getMatches()

    suspend fun createMatch(team1: String, team2: String) =
        repository.createMatch(team1, team2)
            .also { events.emit(OpenMatch(it)) }

    suspend fun deleteMatches() {
        repository.deleteMatches()
    }

    fun matchSelected(matchUid: Int) {
        viewModelScope.launch {
            events.emit(OpenMatch(matchUid))
        }
    }

}