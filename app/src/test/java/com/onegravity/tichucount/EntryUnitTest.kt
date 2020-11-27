package com.onegravity.tichucount

import com.onegravity.tichucount.entry.viewmodel.Entry
import com.onegravity.tichucount.entry.viewmodel.EntryState
import org.junit.Assert
import org.junit.Test

class EntryUnitTest {

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
    }

    @Test
    fun testTiuchu() {
        Entry(EntryState.NOT_PLAYED, EntryState.NOT_PLAYED, false, 0).run {
            tichu = EntryState.WON

            Assert.assertEquals(tichu, EntryState.WON)
            Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 100)

            tichu = EntryState.LOST

            Assert.assertEquals(tichu, EntryState.LOST)
            Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   -100)
        }

        Entry(EntryState.WON, EntryState.NOT_PLAYED, false, 0).run {
            Assert.assertEquals(tichu, EntryState.WON)
            Assert.assertEquals(points(),   100)
        }

        Entry(EntryState.LOST, EntryState.NOT_PLAYED, false, 0).run {
            Assert.assertEquals(tichu, EntryState.LOST)
            Assert.assertEquals(points(),   -100)
        }

        Entry(EntryState.NOT_PLAYED, EntryState.NOT_PLAYED, false, 0).run {
            changes().test().run {
                assertEmpty()
                assertValueCount(0)

                tichu = EntryState.WON
                assertValues(true)
                assertValueCount(1)

                tichu = EntryState.LOST
                assertValues(true, true)
                assertValueCount(2)

                tichu = EntryState.LOST
                assertValues(true, true)
                assertValueCount(2)

                tichu = EntryState.WON
                assertValues(true, true, true)
                assertValueCount(3)
            }
        }
    }

    @Test
    fun testBigTiuchu() {
        Entry(EntryState.NOT_PLAYED, EntryState.NOT_PLAYED, false, 0).run {
            bigTichu = EntryState.WON

            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, EntryState.WON)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 200)

            bigTichu = EntryState.LOST

            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, EntryState.LOST)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(),   -200)
        }

        Entry(EntryState.NOT_PLAYED, EntryState.WON, false, 0).run {
            Assert.assertEquals(bigTichu, EntryState.WON)
            Assert.assertEquals(points(),   200)
        }

        Entry(EntryState.NOT_PLAYED, EntryState.LOST, false, 0).run {
            Assert.assertEquals(bigTichu, EntryState.LOST)
            Assert.assertEquals(points(),   -200)
        }

        Entry(EntryState.WON, EntryState.WON, false, 100).run {
            Assert.assertEquals(tichu, EntryState.WON)
            Assert.assertEquals(bigTichu, EntryState.WON)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 500)
        }

        Entry(EntryState.NOT_PLAYED, EntryState.NOT_PLAYED, false, 0).run {
            changes().test().run {
                assertEmpty()
                assertValueCount(0)

                bigTichu = EntryState.WON
                assertValues(true)
                assertValueCount(1)

                bigTichu = EntryState.LOST
                assertValues(true, true)
                assertValueCount(2)

                bigTichu = EntryState.LOST
                assertValues(true, true)
                assertValueCount(2)

                bigTichu = EntryState.WON
                assertValues(true, true, true)
                assertValueCount(3)
            }
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
                assertValues(true)
                assertValueCount(1)

                playedPoints = 20
                assertValues(true, true)
                assertValueCount(2)

                playedPoints = 20
                assertValues(true, true)
                assertValueCount(2)

                playedPoints = 100
                assertValues(true, true, true)
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