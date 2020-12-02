package com.onegravity.tichucount

import com.onegravity.tichucount.entry.viewmodel.Entry
import com.onegravity.tichucount.entry.viewmodel.EntryState
import com.onegravity.tichucount.entry.viewmodel.Game
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import toothpick.ktp.KTP

class GameUnitTest {

    @Before
    fun prepare() {
        if (! KTP.isScopeOpen(APP_SCOPE)) {
            KTP.openScope(APP_SCOPE).installTestModules(AppTestModule).inject(this)
        }
    }

    @Test
    fun basicTests() {
        val game = Game(
            Entry(EntryState.WON, EntryState.NOT_PLAYED, false, 0, "Team 1"),
            Entry(EntryState.NOT_PLAYED, EntryState.NOT_PLAYED, false, 0, "Team 2")
        )

        game.team1.run {
            Assert.assertEquals(tichu, EntryState.WON)
            Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 100)
        }

        game.team2.run {
            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 100)
            Assert.assertEquals(points(), 100)
        }

        // team 1 won Tichu, now team 2 won it
        game.team2.tichu = EntryState.WON

        game.team1.run {
            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 0)
        }

        game.team2.run {
            Assert.assertEquals(tichu, EntryState.WON)
            Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 100)
            Assert.assertEquals(points(), 200)
        }

        // team 1 won BigTichu
        game.team2.tichu = EntryState.NOT_PLAYED
        game.team1.bigTichu = EntryState.WON

        game.team1.run {
            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, EntryState.WON)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 200)
        }

        game.team2.run {
            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 100)
            Assert.assertEquals(points(), 100)
        }

        // team 1 won BigTichu, now team 2 won it
        game.team2.bigTichu = EntryState.WON

        game.team1.run {
            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 0)
        }

        game.team2.run {
            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, EntryState.WON)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 100)
            Assert.assertEquals(points(), 300)
        }

        // team 1 with double win
        game.team2.bigTichu = EntryState.NOT_PLAYED
        game.team1.doubleWin = true

        game.team1.run {
            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 200)
        }

        game.team2.run {
            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 0)
        }

        // team 1 with double win, now team 2 with double win
        game.team2.bigTichu = EntryState.NOT_PLAYED
        game.team2.doubleWin = true

        game.team1.run {
            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 0)
        }

        game.team2.run {
            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 200)
        }
    }

    @Test
    fun complexTests() {
        Game(
            Entry(EntryState.WON, EntryState.WON, true, 0, "Team 1"),
            Entry(EntryState.NOT_PLAYED, EntryState.NOT_PLAYED, false, 0, "Team 2")
        ).run {
            team1.run {
                Assert.assertEquals(tichu, EntryState.WON)
                Assert.assertEquals(bigTichu, EntryState.WON)
                Assert.assertEquals(doubleWin, true)
                Assert.assertEquals(playedPoints, 0)
                Assert.assertEquals(points(), 500)
            }

            team2.run {
                Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
                Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
                Assert.assertEquals(doubleWin, false)
                Assert.assertEquals(playedPoints, 0)
                Assert.assertEquals(points(), 0)
            }

            team2.doubleWin = true

            team1.run {
                Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
                Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
                Assert.assertEquals(doubleWin, false)
                Assert.assertEquals(playedPoints, 0)
                Assert.assertEquals(points(), 0)
            }

            team2.run {
                Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
                Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
                Assert.assertEquals(doubleWin, true)
                Assert.assertEquals(playedPoints, 0)
                Assert.assertEquals(points(), 200)
            }
        }

        Game(
            Entry(EntryState.WON, EntryState.WON, true, 0, "Team 1"),
            Entry(EntryState.NOT_PLAYED, EntryState.NOT_PLAYED, false, 0, "Team 2")
        ).run {
            team2.tichu = EntryState.WON

            team1.run {
                Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
                Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
                Assert.assertEquals(doubleWin, false)
                Assert.assertEquals(playedPoints, 100)
                Assert.assertEquals(points(), 100)
            }

            team2.run {
                Assert.assertEquals(tichu, EntryState.WON)
                Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
                Assert.assertEquals(doubleWin, false)
                Assert.assertEquals(playedPoints, 0)
                Assert.assertEquals(points(), 100)
            }
        }

        Game(
            Entry(EntryState.WON, EntryState.WON, true, 0, "Team 1"),
            Entry(EntryState.NOT_PLAYED, EntryState.NOT_PLAYED, false, 0, "Team 2")
        ).run {
            team2.bigTichu = EntryState.WON

            team1.run {
                Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
                Assert.assertEquals(bigTichu, EntryState.NOT_PLAYED)
                Assert.assertEquals(doubleWin, false)
                Assert.assertEquals(playedPoints, 100)
                Assert.assertEquals(points(), 100)
            }

            team2.run {
                Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
                Assert.assertEquals(bigTichu, EntryState.WON)
                Assert.assertEquals(doubleWin, false)
                Assert.assertEquals(playedPoints, 0)
                Assert.assertEquals(points(), 200)
            }
        }
    }

}