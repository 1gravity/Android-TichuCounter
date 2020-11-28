package com.onegravity.tichucount

import com.onegravity.tichucount.entry.viewmodel.Entry
import com.onegravity.tichucount.entry.viewmodel.EntryState
import com.onegravity.tichucount.entry.viewmodel.Game
import org.junit.Assert
import org.junit.Test

class GameUnitTest {

    @Test
    fun tests() {
        val game = Game(
            Entry(EntryState.NOT_PLAYED, EntryState.NOT_PLAYED, false, 0, "Team 1"),
            Entry(EntryState.WON, EntryState.NOT_PLAYED, true, 0, "Team 2")
        )

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
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 300)
        }

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

        game.team1.tichu = EntryState.WON

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

        game.team2.tichu = EntryState.WON

        game.team1.run {
            Assert.assertEquals(tichu, EntryState.NOT_PLAYED)
            Assert.assertEquals(points(), 0)
        }

        game.team2.run {
            Assert.assertEquals(tichu, EntryState.WON)
            Assert.assertEquals(points(), 200)
        }
    }

}