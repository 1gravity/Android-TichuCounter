package com.onegravity.tichucount.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onegravity.tichucount.db.MatchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MatchViewModelEvent

object NewGame : MatchViewModelEvent()
data class OpenGame(val gameUid: Int) : MatchViewModelEvent()
data class DeleteGame(val gameUid: Int) : MatchViewModelEvent()

class MatchViewModel @Inject constructor(
    private val repository: MatchRepository
): ViewModel() {

    private val events = MutableSharedFlow<MatchViewModelEvent>(replay = 0)

    fun events(): Flow<MatchViewModelEvent> = events

    suspend fun match(matchUid: Int) = repository.getMatches()
        .map { matches -> matches.first { it.match.uid == matchUid } }

    suspend fun deleteMatch(matchUid: Int) {
        repository.deleteMatch(matchUid)
    }

    fun gameSelected(gameUid: Int) {
        viewModelScope.launch {
            events.emit(OpenGame(gameUid))
        }
    }

}
