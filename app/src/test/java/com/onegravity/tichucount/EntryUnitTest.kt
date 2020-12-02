package com.onegravity.tichucount

import com.onegravity.tichucount.entry.viewmodel.Entry
import com.onegravity.tichucount.entry.viewmodel.EntryState
import com.onegravity.tichucount.entry.viewmodel.EntryType
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import toothpick.ktp.KTP

class EntryUnitTest {

    @Before
    fun prepare() {
        if (! KTP.isScopeOpen(APP_SCOPE)) {
            KTP.openScope(APP_SCOPE).installTestModules(AppTestModule).inject(this)
        }
    }

    @Test
    fun testBasic() {
        Entry(EntryState.NOT_PLAYED, EntryState.NOT_PLAYED, false, 0).run {
            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 0)
        }

        Entry(EntryState.WON, EntryState.LOST, false, 50).run {
            Assert.assertEquals(tichu, EntryState.WON)
            Assert.assertEquals(bigTichu, EntryState.LOST)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 50)
            Assert.assertEquals(points(), -50)
        }

        Entry(EntryState.NOT_PLAYED, EntryState.NOT_PLAYED, true, 50).run {
            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 200)
        }
    }

    @Test
    fun testTiuchu() {
        // check all Tichu states
        Entry(EntryState.NOT_PLAYED, EntryState.NOT_PLAYED, false, 0).run {
            tichu = EntryState.WON

            Assert.assertEquals(tichu, EntryState.WON)
            Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 100)

            tichu = EntryState.NOT_PLAYED

            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   0)

            tichu = EntryState.LOST

            Assert.assertEquals(tichu, EntryState.LOST)
            Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   -100)
        }

        // won Tichu -> 100 points
        Entry(EntryState.WON, EntryState.NOT_PLAYED, false, 0).run {
            Assert.assertEquals(tichu, EntryState.WON)
            Assert.assertEquals(points(),   100)
        }

        // lost Tichu -> -100 points
        Entry(EntryState.LOST, EntryState.NOT_PLAYED, false, 0).run {
            Assert.assertEquals(tichu, EntryState.LOST)
            Assert.assertEquals(points(),   -100)
        }

        // check the event stream for the Tichu field
        Entry(EntryState.NOT_PLAYED, EntryState.NOT_PLAYED, false, 0).run {
            changes().test().run {
                assertEmpty()
                assertValueCount(0)

                tichu = EntryState.WON
                assertValues(EntryType.TICHU)
                assertValueCount(1)

                tichu = EntryState.LOST
                assertValues(EntryType.TICHU, EntryType.TICHU)
                assertValueCount(2)

                tichu = EntryState.LOST
                assertValues(EntryType.TICHU, EntryType.TICHU)
                assertValueCount(2)

                tichu = EntryState.WON
                assertValues(EntryType.TICHU, EntryType.TICHU, EntryType.TICHU)
                assertValueCount(3)
            }
        }
    }

    @Test
    fun testBigTiuchu() {
        // test all BigTichu states
        Entry(EntryState.NOT_PLAYED, EntryState.NOT_PLAYED, false, 0).run {
            bigTichu = EntryState.WON

            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, EntryState.WON)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 200)

            bigTichu = EntryState.NOT_PLAYED

            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   0)

            bigTichu = EntryState.LOST

            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, EntryState.LOST)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   -200)
        }

        // won BigTichu -> 200 points
        Entry(EntryState.NOT_PLAYED, EntryState.WON, false, 0).run {
            Assert.assertEquals(bigTichu, EntryState.WON)
            Assert.assertEquals(points(),   200)
        }

        // lost BigTichu -> -200 points
        Entry(EntryState.NOT_PLAYED, EntryState.LOST, false, 0).run {
            Assert.assertEquals(bigTichu, EntryState.LOST)
            Assert.assertEquals(points(),   -200)
        }

        // check the event stream for the BigTichu field
        Entry(EntryState.NOT_PLAYED, EntryState.NOT_PLAYED, false, 0).run {
            changes().test().run {
                assertEmpty()
                assertValueCount(0)

                bigTichu = EntryState.WON
                assertValues(EntryType.BIG_TICHU)
                assertValueCount(1)

                bigTichu = EntryState.LOST
                assertValues(EntryType.BIG_TICHU, EntryType.BIG_TICHU)
                assertValueCount(2)

                bigTichu = EntryState.LOST
                assertValues(EntryType.BIG_TICHU, EntryType.BIG_TICHU)
                assertValueCount(2)

                bigTichu = EntryState.WON
                assertValues(EntryType.BIG_TICHU, EntryType.BIG_TICHU, EntryType.BIG_TICHU)
                assertValueCount(3)
            }
        }
    }

    @Test
    fun testDoubleWin() {
        Entry(EntryState.WON, EntryState.WON, true, 0).run {
            // maximum points (tichu, big tichu, double win)
            Assert.assertEquals(tichu, EntryState.WON)
            Assert.assertEquals(bigTichu, EntryState.WON)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 500)

            // tichu and double win
            bigTichu = EntryState.NOT_PLAYED
            Assert.assertEquals(tichu, EntryState.WON)
            Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   300)

            // lost big tichu -> can't be double win
            bigTichu = EntryState.LOST
            Assert.assertEquals(tichu, EntryState.WON)
            Assert.assertEquals(bigTichu, EntryState.LOST)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   -100)

            // back to winning big tichu -> must be double win
            bigTichu = EntryState.WON
            Assert.assertEquals(tichu, EntryState.WON)
            Assert.assertEquals(bigTichu, EntryState.WON)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   500)

            // big tichu and double win
            tichu = EntryState.NOT_PLAYED
            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, EntryState.WON)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   400)

            // lost tichu -> can't be double win
            tichu = EntryState.LOST
            Assert.assertEquals(tichu, EntryState.LOST)
            Assert.assertEquals(bigTichu, EntryState.WON)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   100)

            // back to winning tichu -> again max points
            tichu = EntryState.WON
            Assert.assertEquals(tichu, EntryState.WON)
            Assert.assertEquals(bigTichu, EntryState.WON)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   500)
        }

        // check double win event stream for big tichu state changes
        Entry(EntryState.WON, EntryState.NOT_PLAYED, false, 0).run {
            changes().test().run {
                assertEmpty()
                assertValueCount(0)

                bigTichu = EntryState.WON
                assertValues(EntryType.DOUBLE_WIN, EntryType.BIG_TICHU)
                assertValueCount(2)

                bigTichu = EntryState.NOT_PLAYED
                assertValues(EntryType.DOUBLE_WIN, EntryType.BIG_TICHU, EntryType.BIG_TICHU)
                assertValueCount(3)

                bigTichu = EntryState.LOST
                assertValues(EntryType.DOUBLE_WIN, EntryType.BIG_TICHU, EntryType.BIG_TICHU, EntryType.DOUBLE_WIN, EntryType.BIG_TICHU)
                assertValueCount(5)

                bigTichu = EntryState.WON
                assertValues(EntryType.DOUBLE_WIN, EntryType.BIG_TICHU, EntryType.BIG_TICHU, EntryType.DOUBLE_WIN, EntryType.BIG_TICHU, EntryType.DOUBLE_WIN, EntryType.BIG_TICHU)
                assertValueCount(7)
            }
        }

        // check double win event stream for tichu state changes
        Entry(EntryState.NOT_PLAYED, EntryState.WON, false, 0).run {
            changes().test().run {
                assertEmpty()
                assertValueCount(0)

                tichu = EntryState.WON
                assertValues(EntryType.DOUBLE_WIN, EntryType.TICHU)
                assertValueCount(2)

                tichu = EntryState.NOT_PLAYED
                assertValues(EntryType.DOUBLE_WIN, EntryType.TICHU, EntryType.TICHU)
                assertValueCount(3)

                tichu = EntryState.LOST
                assertValues(EntryType.DOUBLE_WIN, EntryType.TICHU, EntryType.TICHU, EntryType.DOUBLE_WIN, EntryType.TICHU)
                assertValueCount(5)

                tichu = EntryState.WON
                assertValues(EntryType.DOUBLE_WIN, EntryType.TICHU, EntryType.TICHU, EntryType.DOUBLE_WIN, EntryType.TICHU, EntryType.DOUBLE_WIN, EntryType.TICHU)
                assertValueCount(7)
            }
        }

        // if we have a tichu and a big tichu -> double win cannot be altered!
        Entry(EntryState.WON, EntryState.WON, true, 0).run {
            Assert.assertEquals(tichu, EntryState.WON)
            Assert.assertEquals(bigTichu, EntryState.WON)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(points(),   500)

            doubleWin = false
            Assert.assertEquals(tichu, EntryState.WON)
            Assert.assertEquals(bigTichu, EntryState.WON)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(points(),   500)
        }
    }

    @Test
    fun testPlayedPoints() {
        Entry(EntryState.NOT_PLAYED, EntryState.NOT_PLAYED, false, 10).run {
            Assert.assertEquals(playedPoints, 10)

            playedPoints = 20
            Assert.assertEquals(playedPoints, 20)

            playedPoints = -25
            Assert.assertEquals(playedPoints, -25)

            tichu = EntryState.WON
            Assert.assertEquals(playedPoints, -25)

            tichu = EntryState.LOST
            Assert.assertEquals(playedPoints, -25)

            playedPoints = 100
            bigTichu = EntryState.WON
            Assert.assertEquals(playedPoints, 100)

            bigTichu = EntryState.LOST
            Assert.assertEquals(playedPoints, 100)

            doubleWin = true
            Assert.assertEquals(playedPoints, 0)
        }

        Entry(EntryState.NOT_PLAYED, EntryState.WON, true, -25).run {
            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, EntryState.WON)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 400)
        }

        Entry(EntryState.WON, EntryState.WON, false, 100).run {
            Assert.assertEquals(tichu, EntryState.WON)
            Assert.assertEquals(bigTichu, EntryState.WON)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 500)
        }

        Entry(EntryState.NOT_PLAYED, EntryState.NOT_PLAYED, true, 100).run {
            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 200)
        }

        Entry(EntryState.NOT_PLAYED, EntryState.NOT_PLAYED, false, 40).run {
            changes().test().run {
                assertEmpty()
                assertValueCount(0)

                playedPoints = 30
                assertValues(EntryType.PLAYED_POINTS)
                assertValueCount(1)

                playedPoints = 20
                assertValues(EntryType.PLAYED_POINTS, EntryType.PLAYED_POINTS)
                assertValueCount(2)

                playedPoints = 20
                assertValues(EntryType.PLAYED_POINTS, EntryType.PLAYED_POINTS)
                assertValueCount(2)

                playedPoints = 100
                assertValues(EntryType.PLAYED_POINTS, EntryType.PLAYED_POINTS, EntryType.PLAYED_POINTS)
                assertValueCount(3)
            }
        }
    }

    @Test
    fun testTotalPoints() {
        Entry(EntryState.NOT_PLAYED, EntryState.NOT_PLAYED, false, 10).run {
            Assert.assertEquals(points(), 10)

            playedPoints = 20
            Assert.assertEquals(points(), 20)

            playedPoints = -25
            Assert.assertEquals(points(), -25)

            tichu = EntryState.WON
            Assert.assertEquals(points(), 75)

            tichu = EntryState.NOT_PLAYED
            bigTichu = EntryState.WON
            Assert.assertEquals(points(), 175)

            doubleWin = true
            Assert.assertEquals(points(), 400 )

            playedPoints = 50
            Assert.assertEquals(points(), 250)
        }

        Entry(EntryState.NOT_PLAYED, EntryState.WON, true, -25).run {
            Assert.assertEquals(points(), 400 )
        }

        Entry(EntryState.WON, EntryState.WON, false, 100).run {
            Assert.assertEquals(points(), 500 )
        }

        Entry(EntryState.NOT_PLAYED, EntryState.NOT_PLAYED, true, 100).run {
            Assert.assertEquals(points(), 200 )
        }
    }

}