package com.onegravity.tichucount

import app.cash.turbine.FlowTurbine
import app.cash.turbine.test
import com.onegravity.tichucount.model.Score
import com.onegravity.tichucount.model.ScoreState
import com.onegravity.tichucount.model.ScoreType
import com.onegravity.tichucount.util.LoggerTestImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
class ScoreUnitTest {

    private suspend fun <T> FlowTurbine<T>.assertValue(expected: T) = coroutineScope {
        assertEquals(expected, awaitItem())
    }

    private suspend fun <T> FlowTurbine<T>.assertValues(vararg expected: T) = coroutineScope {
        for (value in expected) assertValue(value)
    }

    @Test
    fun testBasic() {
        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, "team", LoggerTestImpl).run {
            assertEquals(tichu, ScoreState.NOT_PLAYED)
            assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            assertEquals(doubleWin, false)
            assertEquals(playedPoints, 0)
            assertEquals(points(), 0)
        }

        Score(ScoreState.WON, ScoreState.LOST, false, 50, "team", LoggerTestImpl).run {
            assertEquals(tichu, ScoreState.WON)
            assertEquals(grandTichu, ScoreState.LOST)
            assertEquals(doubleWin, false)
            assertEquals(playedPoints, 50)
            assertEquals(points(), -50)
        }

        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, true, 50, "team", LoggerTestImpl).run {
            assertEquals(tichu, ScoreState.NOT_PLAYED)
            assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            assertEquals(doubleWin, true)
            assertEquals(playedPoints, 0)
            assertEquals(points(), 200)
        }
    }

    @Test
    fun testTiuchu() = runBlockingTest {
        // check all Tichu states
        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, "team", LoggerTestImpl).run {
            tichu = ScoreState.WON

            assertEquals(tichu, ScoreState.WON)
            assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            assertEquals(doubleWin, false)
            assertEquals(playedPoints, 0)
            assertEquals(points(), 100)

            tichu = ScoreState.NOT_PLAYED

            assertEquals(tichu, ScoreState.NOT_PLAYED)
            assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            assertEquals(doubleWin, false)
            assertEquals(playedPoints, 0)
            assertEquals(points(),   0)

            tichu = ScoreState.LOST

            assertEquals(tichu, ScoreState.LOST)
            assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            assertEquals(doubleWin, false)
            assertEquals(playedPoints, 0)
            assertEquals(points(),   -100)
        }

        // won Tichu -> 100 points
        Score(ScoreState.WON, ScoreState.NOT_PLAYED, false, 0, "team", LoggerTestImpl).run {
            assertEquals(tichu, ScoreState.WON)
            assertEquals(points(),   100)
        }

        // lost Tichu -> -100 points
        Score(ScoreState.LOST, ScoreState.NOT_PLAYED, false, 0, "team", LoggerTestImpl).run {
            assertEquals(tichu, ScoreState.LOST)
            assertEquals(points(),   -100)
        }

        // check the event stream for the Tichu field
        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, "team", LoggerTestImpl).run {
            val scoreKeeper = ScoreKeeper(this, this@runBlockingTest)
            scoreKeeper.changes().test {
                scoreKeeper.assertCount(0)

                tichu = ScoreState.WON
                assertValue(ScoreType.TICHU)
                scoreKeeper.assertCount(1)

                tichu = ScoreState.LOST
                scoreKeeper.assertValues(ScoreType.TICHU, ScoreType.TICHU)
                scoreKeeper.assertCount(2)

                tichu = ScoreState.LOST
                scoreKeeper.assertValues(ScoreType.TICHU, ScoreType.TICHU)
                scoreKeeper.assertCount(2)

                tichu = ScoreState.WON
                scoreKeeper.assertValues(ScoreType.TICHU, ScoreType.TICHU, ScoreType.TICHU)
                scoreKeeper.assertCount(3)

                cancelAndIgnoreRemainingEvents()
            }
        }
        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, "team", LoggerTestImpl).run {
            val scoreKeeper = ScoreKeeper(this, this@runBlockingTest)
            scoreKeeper.changes().test {
                tichu = ScoreState.WON
                tichu = ScoreState.LOST
                tichu = ScoreState.LOST
                tichu = ScoreState.WON

                assertValues(ScoreType.TICHU, ScoreType.TICHU, ScoreType.TICHU)

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun testGrandTichu() = runBlockingTest {
        // test all GrandTichu states
        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, "team", LoggerTestImpl).run {
            grandTichu = ScoreState.WON

            assertEquals(tichu, ScoreState.NOT_PLAYED)
            assertEquals(grandTichu, ScoreState.WON)
            assertEquals(doubleWin, false)
            assertEquals(playedPoints, 0)
            assertEquals(points(), 200)

            grandTichu = ScoreState.NOT_PLAYED

            assertEquals(tichu, ScoreState.NOT_PLAYED)
            assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            assertEquals(doubleWin, false)
            assertEquals(playedPoints, 0)
            assertEquals(points(),   0)

            grandTichu = ScoreState.LOST

            assertEquals(tichu, ScoreState.NOT_PLAYED)
            assertEquals(grandTichu, ScoreState.LOST)
            assertEquals(doubleWin, false)
            assertEquals(playedPoints, 0)
            assertEquals(points(),   -200)
        }

        // won GrandTichu -> 200 points
        Score(ScoreState.NOT_PLAYED, ScoreState.WON, false, 0, "team", LoggerTestImpl).run {
            assertEquals(grandTichu, ScoreState.WON)
            assertEquals(points(),   200)
        }

        // lost GrandTichu -> -200 points
        Score(ScoreState.NOT_PLAYED, ScoreState.LOST, false, 0, "team", LoggerTestImpl).run {
            assertEquals(grandTichu, ScoreState.LOST)
            assertEquals(points(),   -200)
        }

        // check the event stream for the GrandTichu field
        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, "team", LoggerTestImpl).run {
            val scoreKeeper = ScoreKeeper(this, this@runBlockingTest)
            scoreKeeper.changes().test {
                scoreKeeper.assertCount(0)

                grandTichu = ScoreState.WON
                assertValue(ScoreType.GRAND_TICHU)
                scoreKeeper.assertCount(1)

                grandTichu = ScoreState.LOST
                assertValue(ScoreType.GRAND_TICHU)
                scoreKeeper.assertCount(2)

                grandTichu = ScoreState.LOST
                scoreKeeper.assertCount(2)

                grandTichu = ScoreState.WON
                assertValue(ScoreType.GRAND_TICHU)
                scoreKeeper.assertCount(3)

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun testDoubleWin() = runBlockingTest {
        Score(ScoreState.WON, ScoreState.WON, true, 100, "team", LoggerTestImpl).run {
            // maximum points (grand tichu, double win)
            assertEquals(tichu, ScoreState.WON)
            assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            assertEquals(doubleWin, true)
            assertEquals(playedPoints, 0)
            assertEquals(points(), 300)
        }

        Score(ScoreState.NOT_PLAYED, ScoreState.WON, true, 0, "team", LoggerTestImpl).run {
            // maximum points (grand tichu, double win)
            assertEquals(tichu, ScoreState.NOT_PLAYED)
            assertEquals(grandTichu, ScoreState.WON)
            assertEquals(doubleWin, true)
            assertEquals(playedPoints, 0)
            assertEquals(points(), 400)

            // tichu and double win
            grandTichu = ScoreState.NOT_PLAYED
            tichu = ScoreState.WON
            assertEquals(tichu, ScoreState.WON)
            assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            assertEquals(doubleWin, true)
            assertEquals(playedPoints, 0)
            assertEquals(points(),   300)

            // tichu, grand tichu and double win is NOT possible
            grandTichu = ScoreState.WON
            assertEquals(tichu, ScoreState.NOT_PLAYED)
            assertEquals(grandTichu, ScoreState.WON)
            assertEquals(doubleWin, true)
            assertEquals(playedPoints, 0)
            assertEquals(points(),   400)

            // tichu, grand tichu and double win is NOT possible
            tichu = ScoreState.WON
            assertEquals(tichu, ScoreState.WON)
            assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            assertEquals(doubleWin, true)
            assertEquals(playedPoints, 0)
            assertEquals(points(),   300)

            // lost tichu AND grand tichu -> can't be double win
            tichu = ScoreState.LOST
            grandTichu = ScoreState.LOST
            assertEquals(tichu, ScoreState.LOST)
            assertEquals(grandTichu, ScoreState.LOST)
            assertEquals(doubleWin, false)
            assertEquals(playedPoints, 0)
            assertEquals(points(),   -300)

            // lost tichu AND grand tichu + double win -> one loss needs to go away
            doubleWin = true
            assertEquals(tichu, ScoreState.NOT_PLAYED)
            assertEquals(grandTichu, ScoreState.LOST)
            assertEquals(doubleWin, true)
            assertEquals(playedPoints, 0)
            assertEquals(points(),   0)

            // back to winning grand tichu
            grandTichu = ScoreState.WON
            assertEquals(tichu, ScoreState.NOT_PLAYED)
            assertEquals(grandTichu, ScoreState.WON)
            assertEquals(doubleWin, true)
            assertEquals(playedPoints, 0)
            assertEquals(points(), 400)

            // lost tichu
            tichu = ScoreState.LOST
            assertEquals(tichu, ScoreState.LOST)
            assertEquals(grandTichu, ScoreState.WON)
            assertEquals(doubleWin, true)
            assertEquals(playedPoints, 0)
            assertEquals(points(),   300)

            // lost grand tichu -> can't be double win
            grandTichu = ScoreState.LOST
            assertEquals(tichu, ScoreState.LOST)
            assertEquals(grandTichu, ScoreState.LOST)
            assertEquals(doubleWin, false)
            assertEquals(playedPoints, 0)
            assertEquals(points(),   -300)
        }

        // check double win event stream for grand tichu state changes
        Score(ScoreState.WON, ScoreState.NOT_PLAYED, false, 0, "team", LoggerTestImpl).run {
            val scoreKeeper = ScoreKeeper(this, this@runBlockingTest)
            scoreKeeper.changes().test {
                scoreKeeper.assertCount(0)

                grandTichu = ScoreState.WON
                assertValues(ScoreType.TICHU, ScoreType.GRAND_TICHU)
                scoreKeeper.assertCount(2)

                grandTichu = ScoreState.NOT_PLAYED
                assertValue(ScoreType.GRAND_TICHU)
                scoreKeeper.assertCount(3)

                grandTichu = ScoreState.LOST
                assertValue(ScoreType.GRAND_TICHU)
                scoreKeeper.assertCount(4)

                grandTichu = ScoreState.WON
                assertValue(ScoreType.GRAND_TICHU)
                scoreKeeper.assertCount(5)

                cancelAndIgnoreRemainingEvents()
            }
        }


        // check double win event stream for tichu and grand tichu state changes
        Score(ScoreState.NOT_PLAYED, ScoreState.WON, true, 0, "team", LoggerTestImpl).run {
            val scoreKeeper = ScoreKeeper(this, this@runBlockingTest)
            scoreKeeper.changes().test {
                scoreKeeper.assertCount(0)

                tichu = ScoreState.WON
                assertValues(ScoreType.GRAND_TICHU, ScoreType.TICHU)
                scoreKeeper.assertCount(2)

                tichu = ScoreState.LOST
                assertValue(ScoreType.TICHU)
                scoreKeeper.assertCount(3)

                grandTichu = ScoreState.LOST
                assertValues(ScoreType.DOUBLE_WIN, ScoreType.GRAND_TICHU)
                scoreKeeper.assertCount(5)

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun testPlayedPoints() = runBlockingTest {
        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 10, "team", LoggerTestImpl).run {
            assertEquals(playedPoints, 10)

            playedPoints = 20
            assertEquals(playedPoints, 20)

            playedPoints = -25
            assertEquals(playedPoints, -25)

            tichu = ScoreState.WON
            assertEquals(playedPoints, -25)

            tichu = ScoreState.LOST
            assertEquals(playedPoints, -25)

            playedPoints = 100
            grandTichu = ScoreState.WON
            assertEquals(playedPoints, 100)

            grandTichu = ScoreState.LOST
            assertEquals(playedPoints, 100)

            doubleWin = true
            assertEquals(playedPoints, 0)
        }

        Score(ScoreState.NOT_PLAYED, ScoreState.WON, true, -25, "team", LoggerTestImpl).run {
            assertEquals(tichu, ScoreState.NOT_PLAYED)
            assertEquals(grandTichu, ScoreState.WON)
            assertEquals(doubleWin, true)
            assertEquals(playedPoints, 0)
            assertEquals(points(), 400)
        }

        Score(ScoreState.WON, ScoreState.WON, false, 100, "team", LoggerTestImpl).run {
            assertEquals(tichu, ScoreState.WON)
            assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            assertEquals(doubleWin, false)
            assertEquals(playedPoints, 100)
            assertEquals(points(), 200)
        }

        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, true, 100, "team", LoggerTestImpl).run {
            assertEquals(tichu, ScoreState.NOT_PLAYED)
            assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            assertEquals(doubleWin, true)
            assertEquals(playedPoints, 0)
            assertEquals(points(), 200)
        }

        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 40, "team", LoggerTestImpl).run {
            val scoreKeeper = ScoreKeeper(this, this@runBlockingTest)
            scoreKeeper.changes().test {
                scoreKeeper.assertCount(0)

                playedPoints = 30
                assertValue(ScoreType.PLAYED_POINTS)
                scoreKeeper.assertCount(1)

                playedPoints = 20
                assertValue(ScoreType.PLAYED_POINTS)
                scoreKeeper.assertCount(2)

                playedPoints = 20
                scoreKeeper.assertCount(2)

                playedPoints = 100
                assertValue(ScoreType.PLAYED_POINTS)
                scoreKeeper.assertCount(3)

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun testTotalPoints() = runBlockingTest {
        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 10, "team", LoggerTestImpl).run {
            assertEquals(points(), 10)

            playedPoints = 20
            assertEquals(points(), 20)

            playedPoints = -25
            assertEquals(points(), -25)

            tichu = ScoreState.WON
            assertEquals(points(), 75)

            tichu = ScoreState.NOT_PLAYED
            grandTichu = ScoreState.WON
            assertEquals(points(), 175)

            doubleWin = true
            assertEquals(points(), 400 )

            playedPoints = 50
            assertEquals(points(), 250)
        }

        Score(ScoreState.WON, ScoreState.WON, true, -25, "team", LoggerTestImpl).run {
            assertEquals(points(), 300)

            playedPoints = 50
            assertEquals(tichu, ScoreState.WON)
            assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            assertEquals(doubleWin, false)
            assertEquals(playedPoints, 50)
            assertEquals(points(), 150)
        }

        Score(ScoreState.WON, ScoreState.WON, false, 100, "team", LoggerTestImpl).run {
            assertEquals(points(), 200 )
        }

        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, true, 100, "team", LoggerTestImpl).run {
            assertEquals(points(), 200 )
        }
    }

}