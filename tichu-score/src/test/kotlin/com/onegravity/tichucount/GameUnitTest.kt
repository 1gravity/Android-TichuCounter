package com.onegravity.tichucount

import com.onegravity.tichucount.model.Score
import com.onegravity.tichucount.model.ScoreState
import com.onegravity.tichucount.model.Game
import com.onegravity.tichucount.util.LoggerTestImpl
import org.junit.Assert
import org.junit.Test

class GameUnitTest {

    @Test
    fun basicTests() {
        val game = Game(0, "Team 1", "Team 2",
            Score(ScoreState.WON, ScoreState.NOT_PLAYED, false, 0, "Team 1", LoggerTestImpl),
            Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, "Team 2", LoggerTestImpl),
            LoggerTestImpl
        )

        game.score1.run {
            Assert.assertEquals(tichu, ScoreState.WON)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 100)
        }

        game.score2.run {
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 100)
            Assert.assertEquals(points(), 100)
        }

        // team 1 won Tichu, now team 2 won it
        game.score2.tichu = ScoreState.WON

        game.score1.run {
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 0)
        }

        game.score2.run {
            Assert.assertEquals(tichu, ScoreState.WON)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 100)
            Assert.assertEquals(points(), 200)
        }

        // team 1 won GrandTichu
        game.score2.tichu = ScoreState.NOT_PLAYED
        game.score1.grandTichu = ScoreState.WON

        game.score1.run {
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.WON)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 200)
        }

        game.score2.run {
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 100)
            Assert.assertEquals(points(), 100)
        }

        // team 1 won GrandTichu, now team 2 won it
        game.score2.grandTichu = ScoreState.WON

        game.score1.run {
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 0)
        }

        game.score2.run {
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.WON)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 100)
            Assert.assertEquals(points(), 300)
        }

        // team 1 with double win
        game.score2.grandTichu = ScoreState.NOT_PLAYED
        game.score1.doubleWin = true

        game.score1.run {
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 200)
        }

        game.score2.run {
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 0)
        }

        // team 1 with double win, now team 2 with double win
        game.score2.grandTichu = ScoreState.NOT_PLAYED
        game.score2.doubleWin = true

        game.score1.run {
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, false)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 0)
        }

        game.score2.run {
            Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
            Assert.assertEquals(doubleWin, true)
            Assert.assertEquals(playedPoints, 0)
            Assert.assertEquals(points(), 200)
        }
    }

    @Test
    fun complexTests() {
        Game(0, "Team 1", "Team 2",
            Score(ScoreState.WON, ScoreState.WON, true, 0, "Team 1", LoggerTestImpl),
            Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, "Team 2", LoggerTestImpl),
            LoggerTestImpl
        ).run {
            score1.run {
                Assert.assertEquals(tichu, ScoreState.WON)
                Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(doubleWin, true)
                Assert.assertEquals(playedPoints, 0)
                Assert.assertEquals(points(), 300)
            }

            score2.run {
                Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(doubleWin, false)
                Assert.assertEquals(playedPoints, 0)
                Assert.assertEquals(points(), 0)
            }

            score2.doubleWin = true

            score1.run {
                Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(doubleWin, false)
                Assert.assertEquals(playedPoints, 0)
                Assert.assertEquals(points(), 0)
            }

            score2.run {
                Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(doubleWin, true)
                Assert.assertEquals(playedPoints, 0)
                Assert.assertEquals(points(), 200)
            }
        }

        Game(0, "Team 1", "Team 2",
            Score(ScoreState.WON, ScoreState.WON, true, 0, "Team 1", LoggerTestImpl),
            Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, "Team 2", LoggerTestImpl),
            LoggerTestImpl
        ).run {
            score2.tichu = ScoreState.WON

            score1.run {
                Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(doubleWin, false)
                Assert.assertEquals(playedPoints, 100)
                Assert.assertEquals(points(), 100)
            }

            score2.run {
                Assert.assertEquals(tichu, ScoreState.WON)
                Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(doubleWin, false)
                Assert.assertEquals(playedPoints, 0)
                Assert.assertEquals(points(), 100)
            }
        }

        Game(0, "Team 1", "Team 2",
            Score(ScoreState.WON, ScoreState.WON, true, 0, "Team 1", LoggerTestImpl),
            Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, "Team 2", LoggerTestImpl),
            LoggerTestImpl
        ).run {
            score2.grandTichu = ScoreState.WON

            score1.run {
                Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(doubleWin, false)
                Assert.assertEquals(playedPoints, 100)
                Assert.assertEquals(points(), 100)
            }

            score2.run {
                Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(grandTichu, ScoreState.WON)
                Assert.assertEquals(doubleWin, false)
                Assert.assertEquals(playedPoints, 0)
                Assert.assertEquals(points(), 200)
            }
        }
    }

    @Test
    fun pointTests() {
        Game(0, "Team 1", "Team 2",
            Score(ScoreState.WON, ScoreState.WON, true, 0, "Team 1", LoggerTestImpl),
            Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, "Team 2", LoggerTestImpl),
            LoggerTestImpl
        ).run {
            score1.playedPoints = 45

            score1.run {
                Assert.assertEquals(tichu, ScoreState.WON)
                Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(doubleWin, false)
                Assert.assertEquals(playedPoints, 45)
                Assert.assertEquals(points(), 145)
            }

            score2.run {
                Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(doubleWin, false)
                Assert.assertEquals(playedPoints, 55)
                Assert.assertEquals(points(), 55)
            }

            score2.doubleWin = true

            score1.run {
                Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(doubleWin, false)
                Assert.assertEquals(playedPoints, 0)
                Assert.assertEquals(points(), 0)
            }

            score2.run {
                Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(doubleWin, true)
                Assert.assertEquals(playedPoints, 0)
                Assert.assertEquals(points(), 200)
            }
        }

        Game(0, "Team 1", "Team 2",
            Score(ScoreState.WON, ScoreState.WON, true, 0, "Team 1", LoggerTestImpl),
            Score(ScoreState.NOT_PLAYED, ScoreState.NOT_PLAYED, false, 0, "Team 2", LoggerTestImpl),
            LoggerTestImpl
        ).run {
            score2.playedPoints = 40

            score1.run {
                Assert.assertEquals(tichu, ScoreState.WON)
                Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(doubleWin, false)
                Assert.assertEquals(playedPoints, 60)
                Assert.assertEquals(points(), 160)
            }

            score2.run {
                Assert.assertEquals(tichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(grandTichu, ScoreState.NOT_PLAYED)
                Assert.assertEquals(doubleWin, false)
                Assert.assertEquals(playedPoints, 40)
                Assert.assertEquals(points(), 40)
            }
        }
    }

}