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
            Assert.assertEquals(bigTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 0)
        }

        Score(ScoreState.WON, ScoreState.LOST, false, 50, "team").run {
            Assert.assertEquals(tichu, ScoreState.WON)
            Assert.assertEquals(bigTichu, ScoreState.LOST)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 50)
            Assert.assertEquals(points(), -50)
        }

        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, true, 50, "team").run {
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, ScoreState.NOT_PLAYED)
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
            Assert.assertEquals(bigTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 100)

            tichu = ScoreState.NOT_PLAYED

            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   0)

            tichu = ScoreState.LOST

            Assert.assertEquals(tichu, ScoreState.LOST)
            Assert.assertEquals(bigTichu, ScoreState.NOT_PLAYED)
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
    fun testBigTiuchu() {
        // test all BigTichu states
        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, "team").run {
            bigTichu = ScoreState.WON

            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, ScoreState.WON)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 200)

            bigTichu = ScoreState.NOT_PLAYED

            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   0)

            bigTichu = ScoreState.LOST

            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, ScoreState.LOST)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   -200)
        }

        // won BigTichu -> 200 points
        Score(ScoreState.NOT_PLAYED, ScoreState.WON, false, 0, "team").run {
            Assert.assertEquals(bigTichu, ScoreState.WON)
            Assert.assertEquals(points(),   200)
        }

        // lost BigTichu -> -200 points
        Score(ScoreState.NOT_PLAYED, ScoreState.LOST, false, 0, "team").run {
            Assert.assertEquals(bigTichu, ScoreState.LOST)
            Assert.assertEquals(points(),   -200)
        }

        // check the event stream for the BigTichu field
        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, "team").run {
            changes().test().run {
                assertEmpty()
                assertValueCount(0)

                bigTichu = ScoreState.WON
                assertValues(ScoreType.BIG_TICHU)
                assertValueCount(1)

                bigTichu = ScoreState.LOST
                assertValues(ScoreType.BIG_TICHU, ScoreType.BIG_TICHU)
                assertValueCount(2)

                bigTichu = ScoreState.LOST
                assertValues(ScoreType.BIG_TICHU, ScoreType.BIG_TICHU)
                assertValueCount(2)

                bigTichu = ScoreState.WON
                assertValues(ScoreType.BIG_TICHU, ScoreType.BIG_TICHU, ScoreType.BIG_TICHU)
                assertValueCount(3)
            }
        }
    }

    @Test
    fun testDoubleWin() {
        Score(ScoreState.WON, ScoreState.WON, true, 100, "team").run {
            // maximum points (big tichu, double win)
            Assert.assertEquals(tichu, ScoreState.WON)
            Assert.assertEquals(bigTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 300)
        }

        Score(ScoreState.NOT_PLAYED, ScoreState.WON, true, 0, "team").run {
            // maximum points (big tichu, double win)
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, ScoreState.WON)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 400)

            // tichu and double win
            bigTichu = ScoreState.NOT_PLAYED
            tichu = ScoreState.WON
            Assert.assertEquals(tichu, ScoreState.WON)
            Assert.assertEquals(bigTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   300)

            // tichu, big tichu and double win is NOT possible
            bigTichu = ScoreState.WON
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, ScoreState.WON)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   400)

            // tichu, big tichu and double win is NOT possible
            tichu = ScoreState.WON
            Assert.assertEquals(tichu, ScoreState.WON)
            Assert.assertEquals(bigTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   300)

            // lost tichu AND big tichu -> can't be double win
            tichu = ScoreState.LOST
            bigTichu = ScoreState.LOST
            Assert.assertEquals(tichu, ScoreState.LOST)
            Assert.assertEquals(bigTichu, ScoreState.LOST)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   -300)

            // lost tichu AND big tichu + double win -> one loss needs to go away
            doubleWin = true
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, ScoreState.LOST)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   0)

            // back to winning big tichu
            bigTichu = ScoreState.WON
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, ScoreState.WON)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 400)

            // lost tichu
            tichu = ScoreState.LOST
            Assert.assertEquals(tichu, ScoreState.LOST)
            Assert.assertEquals(bigTichu, ScoreState.WON)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   300)

            // lost big tichu -> can't be double win
            bigTichu = ScoreState.LOST
            Assert.assertEquals(tichu, ScoreState.LOST)
            Assert.assertEquals(bigTichu, ScoreState.LOST)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   -300)
        }

        // check double win event stream for big tichu state changes
        Score(ScoreState.WON, ScoreState.NOT_PLAYED, false, 0, "team").run {
            changes().test().run {
                assertEmpty()
                assertValueCount(0)

                bigTichu = ScoreState.WON
                assertValues(ScoreType.TICHU, ScoreType.BIG_TICHU)
                assertValueCount(2)

                bigTichu = ScoreState.NOT_PLAYED
                assertValues(ScoreType.TICHU, ScoreType.BIG_TICHU, ScoreType.BIG_TICHU)
                assertValueCount(3)

                bigTichu = ScoreState.LOST
                assertValues(ScoreType.TICHU, ScoreType.BIG_TICHU, ScoreType.BIG_TICHU, ScoreType.BIG_TICHU)
                assertValueCount(4)

                bigTichu = ScoreState.WON
                assertValues(ScoreType.TICHU, ScoreType.BIG_TICHU, ScoreType.BIG_TICHU, ScoreType.BIG_TICHU, ScoreType.BIG_TICHU)
                assertValueCount(5)
            }
        }

        // check double win event stream for tichu state changes
        Score(ScoreState.NOT_PLAYED, ScoreState.WON, false, 0, "team").run {
            changes().test().run {
                assertEmpty()
                assertValueCount(0)

                tichu = ScoreState.WON
                assertValues(ScoreType.BIG_TICHU, ScoreType.TICHU)
                assertValueCount(2)

                tichu = ScoreState.NOT_PLAYED
                assertValues(ScoreType.BIG_TICHU, ScoreType.TICHU, ScoreType.TICHU)
                assertValueCount(3)

                tichu = ScoreState.LOST
                assertValues(ScoreType.BIG_TICHU, ScoreType.TICHU, ScoreType.TICHU, ScoreType.TICHU)
                assertValueCount(4)

                tichu = ScoreState.WON
                assertValues(ScoreType.BIG_TICHU, ScoreType.TICHU, ScoreType.TICHU, ScoreType.TICHU, ScoreType.TICHU)
                assertValueCount(5)
            }
        }

        // check double win event stream for tichu and big tichu  state changes
        Score(ScoreState.NOT_PLAYED, ScoreState.WON, true, 0, "team").run {
            changes().test().run {
                assertEmpty()
                assertValueCount(0)

                tichu = ScoreState.WON
                assertValues(ScoreType.BIG_TICHU, ScoreType.TICHU)
                assertValueCount(2)

                tichu = ScoreState.LOST
                assertValues(ScoreType.BIG_TICHU, ScoreType.TICHU, ScoreType.TICHU)
                assertValueCount(3)

                bigTichu = ScoreState.LOST
                assertValues(ScoreType.BIG_TICHU, ScoreType.TICHU, ScoreType.TICHU, ScoreType.DOUBLE_WIN, ScoreType.BIG_TICHU)
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
            bigTichu = ScoreState.WON
            Assert.assertEquals(playedPoints, 100)

            bigTichu = ScoreState.LOST
            Assert.assertEquals(playedPoints, 100)

            doubleWin = true
            Assert.assertEquals(playedPoints, 0)
        }

        Score(ScoreState.NOT_PLAYED, ScoreState.WON, true, -25, "team").run {
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, ScoreState.WON)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 400)
        }

        Score(ScoreState.WON, ScoreState.WON, false, 100, "team").run {
            Assert.assertEquals(tichu, ScoreState.WON)
            Assert.assertEquals(bigTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 100)
            Assert.assertEquals(points(), 200)
        }

        Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, true, 100, "team").run {
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, ScoreState.NOT_PLAYED)
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
            bigTichu = ScoreState.WON
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
            Assert.assertEquals(bigTichu, ScoreState.NOT_PLAYED)
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