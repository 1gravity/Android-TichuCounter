package com.onegravity.tichucount

import com.onegravity.tichucount.model.Score
import com.onegravity.tichucount.model.ScoreState
import com.onegravity.tichucount.model.ScoreType
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import toothpick.ktp.KTP

class ScoreUnitTest {

    @Before
    fun prepare() {
        if (! KTP.isScopeOpen(APP_SCOPE)) {
            KTP.openScope(APP_SCOPE).installTestModules(AppTestModule).inject(this)
        }
    }

    @Test
    fun testBasic() {
        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, "team").run {
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 0)
        }

        Score(ScoreState.WON, ScoreState.LOST, false, 50, "team").run {
            Assert.assertEquals(tichu, ScoreState.WON)
            Assert.assertEquals(grandTichu, ScoreState.LOST)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 50)
            Assert.assertEquals(points(), -50)
        }

        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, true, 50, "team").run {
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 200)
        }
    }

    @Test
    fun testTiuchu() {
        // check all Tichu states
        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, "team").run {
            tichu = ScoreState.WON

            Assert.assertEquals(tichu, ScoreState.WON)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 100)

            tichu = ScoreState.NOT_PLAYED

            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   0)

            tichu = ScoreState.LOST

            Assert.assertEquals(tichu, ScoreState.LOST)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   -100)
        }

        // won Tichu -> 100 points
        Score(ScoreState.WON, ScoreState.NOT_PLAYED, false, 0, "team").run {
            Assert.assertEquals(tichu, ScoreState.WON)
            Assert.assertEquals(points(),   100)
        }

        // lost Tichu -> -100 points
        Score(ScoreState.LOST, ScoreState.NOT_PLAYED, false, 0, "team").run {
            Assert.assertEquals(tichu, ScoreState.LOST)
            Assert.assertEquals(points(),   -100)
        }

        // check the event stream for the Tichu field
        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, "team").run {
            changes().test().run {
                assertEmpty()
                assertValueCount(0)

                tichu = ScoreState.WON
                assertValues(ScoreType.TICHU)
                assertValueCount(1)

                tichu = ScoreState.LOST
                assertValues(ScoreType.TICHU, ScoreType.TICHU)
                assertValueCount(2)

                tichu = ScoreState.LOST
                assertValues(ScoreType.TICHU, ScoreType.TICHU)
                assertValueCount(2)

                tichu = ScoreState.WON
                assertValues(ScoreType.TICHU, ScoreType.TICHU, ScoreType.TICHU)
                assertValueCount(3)
            }
        }
    }

    @Test
    fun testGrandTichu() {
        // test all GrandTichu states
        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, "team").run {
            grandTichu = ScoreState.WON

            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.WON)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 200)

            grandTichu = ScoreState.NOT_PLAYED

            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   0)

            grandTichu = ScoreState.LOST

            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.LOST)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   -200)
        }

        // won GrandTichu -> 200 points
        Score(ScoreState.NOT_PLAYED, ScoreState.WON, false, 0, "team").run {
            Assert.assertEquals(grandTichu, ScoreState.WON)
            Assert.assertEquals(points(),   200)
        }

        // lost GrandTichu -> -200 points
        Score(ScoreState.NOT_PLAYED, ScoreState.LOST, false, 0, "team").run {
            Assert.assertEquals(grandTichu, ScoreState.LOST)
            Assert.assertEquals(points(),   -200)
        }

        // check the event stream for the GrandTichu field
        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, "team").run {
            changes().test().run {
                assertEmpty()
                assertValueCount(0)

                grandTichu = ScoreState.WON
                assertValues(ScoreType.GRAND_TICHU)
                assertValueCount(1)

                grandTichu = ScoreState.LOST
                assertValues(ScoreType.GRAND_TICHU, ScoreType.GRAND_TICHU)
                assertValueCount(2)

                grandTichu = ScoreState.LOST
                assertValues(ScoreType.GRAND_TICHU, ScoreType.GRAND_TICHU)
                assertValueCount(2)

                grandTichu = ScoreState.WON
                assertValues(ScoreType.GRAND_TICHU, ScoreType.GRAND_TICHU, ScoreType.GRAND_TICHU)
                assertValueCount(3)
            }
        }
    }

    @Test
    fun testDoubleWin() {
        Score(ScoreState.WON, ScoreState.WON, true, 100, "team").run {
            // maximum points (grand tichu, double win)
            Assert.assertEquals(tichu, ScoreState.WON)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 300)
        }

        Score(ScoreState.NOT_PLAYED, ScoreState.WON, true, 0, "team").run {
            // maximum points (grand tichu, double win)
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.WON)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 400)

            // tichu and double win
            grandTichu = ScoreState.NOT_PLAYED
            tichu = ScoreState.WON
            Assert.assertEquals(tichu, ScoreState.WON)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   300)

            // tichu, grand tichu and double win is NOT possible
            grandTichu = ScoreState.WON
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.WON)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   400)

            // tichu, grand tichu and double win is NOT possible
            tichu = ScoreState.WON
            Assert.assertEquals(tichu, ScoreState.WON)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   300)

            // lost tichu AND grand tichu -> can't be double win
            tichu = ScoreState.LOST
            grandTichu = ScoreState.LOST
            Assert.assertEquals(tichu, ScoreState.LOST)
            Assert.assertEquals(grandTichu, ScoreState.LOST)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   -300)

            // lost tichu AND grand tichu + double win -> one loss needs to go away
            doubleWin = true
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.LOST)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   0)

            // back to winning grand tichu
            grandTichu = ScoreState.WON
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.WON)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 400)

            // lost tichu
            tichu = ScoreState.LOST
            Assert.assertEquals(tichu, ScoreState.LOST)
            Assert.assertEquals(grandTichu, ScoreState.WON)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   300)

            // lost grand tichu -> can't be double win
            grandTichu = ScoreState.LOST
            Assert.assertEquals(tichu, ScoreState.LOST)
            Assert.assertEquals(grandTichu, ScoreState.LOST)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   -300)
        }

        // check double win event stream for grand tichu state changes
        Score(ScoreState.WON, ScoreState.NOT_PLAYED, false, 0, "team").run {
            changes().test().run {
                assertEmpty()
                assertValueCount(0)

                grandTichu = ScoreState.WON
                assertValues(ScoreType.TICHU, ScoreType.GRAND_TICHU)
                assertValueCount(2)

                grandTichu = ScoreState.NOT_PLAYED
                assertValues(ScoreType.TICHU, ScoreType.GRAND_TICHU, ScoreType.GRAND_TICHU)
                assertValueCount(3)

                grandTichu = ScoreState.LOST
                assertValues(ScoreType.TICHU, ScoreType.GRAND_TICHU, ScoreType.GRAND_TICHU, ScoreType.GRAND_TICHU)
                assertValueCount(4)

                grandTichu = ScoreState.WON
                assertValues(ScoreType.TICHU, ScoreType.GRAND_TICHU, ScoreType.GRAND_TICHU, ScoreType.GRAND_TICHU, ScoreType.GRAND_TICHU)
                assertValueCount(5)
            }
        }

        // check double win event stream for tichu state changes
        Score(ScoreState.NOT_PLAYED, ScoreState.WON, false, 0, "team").run {
            changes().test().run {
                assertEmpty()
                assertValueCount(0)

                tichu = ScoreState.WON
                assertValues(ScoreType.GRAND_TICHU, ScoreType.TICHU)
                assertValueCount(2)

                tichu = ScoreState.NOT_PLAYED
                assertValues(ScoreType.GRAND_TICHU, ScoreType.TICHU, ScoreType.TICHU)
                assertValueCount(3)

                tichu = ScoreState.LOST
                assertValues(ScoreType.GRAND_TICHU, ScoreType.TICHU, ScoreType.TICHU, ScoreType.TICHU)
                assertValueCount(4)

                tichu = ScoreState.WON
                assertValues(ScoreType.GRAND_TICHU, ScoreType.TICHU, ScoreType.TICHU, ScoreType.TICHU, ScoreType.TICHU)
                assertValueCount(5)
            }
        }

        // check double win event stream for tichu and grand tichu state changes
        Score(ScoreState.NOT_PLAYED, ScoreState.WON, true, 0, "team").run {
            changes().test().run {
                assertEmpty()
                assertValueCount(0)

                tichu = ScoreState.WON
                assertValues(ScoreType.GRAND_TICHU, ScoreType.TICHU)
                assertValueCount(2)

                tichu = ScoreState.LOST
                assertValues(ScoreType.GRAND_TICHU, ScoreType.TICHU, ScoreType.TICHU)
                assertValueCount(3)

                grandTichu = ScoreState.LOST
                assertValues(ScoreType.GRAND_TICHU, ScoreType.TICHU, ScoreType.TICHU, ScoreType.DOUBLE_WIN, ScoreType.GRAND_TICHU)
                assertValueCount(5)
            }
        }
    }

    @Test
    fun testPlayedPoints() {
        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 10, "team").run {
            Assert.assertEquals(playedPoints, 10)

            playedPoints = 20
            Assert.assertEquals(playedPoints, 20)

            playedPoints = -25
            Assert.assertEquals(playedPoints, -25)

            tichu = ScoreState.WON
            Assert.assertEquals(playedPoints, -25)

            tichu = ScoreState.LOST
            Assert.assertEquals(playedPoints, -25)

            playedPoints = 100
            grandTichu = ScoreState.WON
            Assert.assertEquals(playedPoints, 100)

            grandTichu = ScoreState.LOST
            Assert.assertEquals(playedPoints, 100)

            doubleWin = true
            Assert.assertEquals(playedPoints, 0)
        }

        Score(ScoreState.NOT_PLAYED, ScoreState.WON, true, -25, "team").run {
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.WON)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 400)
        }

        Score(ScoreState.WON, ScoreState.WON, false, 100, "team").run {
            Assert.assertEquals(tichu, ScoreState.WON)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 100)
            Assert.assertEquals(points(), 200)
        }

        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, true, 100, "team").run {
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 200)
        }

        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 40, "team").run {
            changes().test().run {
                assertEmpty()
                assertValueCount(0)

                playedPoints = 30
                assertValues(ScoreType.PLAYED_POINTS)
                assertValueCount(1)

                playedPoints = 20
                assertValues(ScoreType.PLAYED_POINTS, ScoreType.PLAYED_POINTS)
                assertValueCount(2)

                playedPoints = 20
                assertValues(ScoreType.PLAYED_POINTS, ScoreType.PLAYED_POINTS)
                assertValueCount(2)

                playedPoints = 100
                assertValues(ScoreType.PLAYED_POINTS, ScoreType.PLAYED_POINTS, ScoreType.PLAYED_POINTS)
                assertValueCount(3)
            }
        }
    }

    @Test
    fun testTotalPoints() {
        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 10, "team").run {
            Assert.assertEquals(points(), 10)

            playedPoints = 20
            Assert.assertEquals(points(), 20)

            playedPoints = -25
            Assert.assertEquals(points(), -25)

            tichu = ScoreState.WON
            Assert.assertEquals(points(), 75)

            tichu = ScoreState.NOT_PLAYED
            grandTichu = ScoreState.WON
            Assert.assertEquals(points(), 175)

            doubleWin = true
            Assert.assertEquals(points(), 400 )

            playedPoints = 50
            Assert.assertEquals(points(), 250)
        }

        Score(ScoreState.WON, ScoreState.WON, true, -25, "team").run {
            Assert.assertEquals(points(), 300)

            playedPoints = 50
            Assert.assertEquals(tichu, ScoreState.WON)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 50)
            Assert.assertEquals(points(), 150)
        }

        Score(ScoreState.WON, ScoreState.WON, false, 100, "team").run {
            Assert.assertEquals(points(), 200 )
        }

        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, true, 100, "team").run {
            Assert.assertEquals(points(), 200 )
        }
    }

}