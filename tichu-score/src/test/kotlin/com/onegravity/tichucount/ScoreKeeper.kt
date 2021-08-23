package com.onegravity.tichucount

import com.onegravity.tichucount.model.Score
import com.onegravity.tichucount.model.ScoreListener
import com.onegravity.tichucount.model.ScoreType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Assert.assertEquals

data class ScoreKeeper(val score: Score, val scope: CoroutineScope) : ScoreListener {
    init {
        score.addListener(this)
    }

    private val scoreFlow = MutableSharedFlow<ScoreType>(replay = 0)

    private val allScores = arrayListOf<ScoreType>()

    fun changes(): Flow<ScoreType> = scoreFlow

    fun assertValues(vararg expected: ScoreType) {
        assertEquals(expected.size, allScores.size)
        expected.forEachIndexed { index, value ->
            assertEquals(value, allScores[index])
        }
    }

    fun assertCount(expected: Int) {
        assertEquals(expected, allScores.size)
    }

    @DelicateCoroutinesApi
    override fun onChanged(type: ScoreType) {
        allScores.add(type)
        scope.launch {
            scoreFlow.emit(type)
        }
    }

}
