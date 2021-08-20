package com.onegravity.tichucount.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onegravity.tichucount.db.MatchRepository
import com.onegravity.tichucount.db.MatchWithGames
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MatchesViewModelEvent

object NOP : MatchesViewModelEvent()
object DeleteMatches : MatchesViewModelEvent()
object NewMatch : MatchesViewModelEvent()
data class OpenMatch(val matchUid: Int) : MatchesViewModelEvent()

class MatchesViewModel @Inject constructor(
    private val repository: MatchRepository
): ViewModel() {

    private val events = MutableStateFlow<MatchesViewModelEvent>(NOP)

    fun events(): Flow<MatchesViewModelEvent> = events

    fun matches(): Flow<List<MatchWithGames>> = repository.getMatches()

    fun createMatch(team1: String, team2: String): Single<Int> =
        repository.createMatch(team1, team2)
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                viewModelScope.launch {
                    events.emit(OpenMatch(it))
                }
            }

    fun deleteMatches(): Completable = repository.deleteMatches()
        .subscribeOn(Schedulers.io())

    fun matchSelected(matchUid: Int) {
        viewModelScope.launch {
            events.emit(OpenMatch(matchUid))
        }
    }

}