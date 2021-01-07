package com.onegravity.tichucount

import com.onegravity.tichucount.util.CountValve
import org.junit.Test

class CountdownValveUnitTest {

    @Test
    fun tests() {
        CountValve().isOpenEvents().test().run { assertValue(false) }

        CountValve().open().isOpenEvents().test().run { assertValue(true) }
        CountValve().close().isOpenEvents().test().run { assertValue(false) }

        CountValve().close().open().close().isOpenEvents().test().run { assertValue(false) }
        CountValve().open().open().open().isOpenEvents().test().run { assertValue(true) }

        CountValve().close().open().open().close().isOpenEvents().test().run { assertValue(false) }
        CountValve().open().open().open().close().close().isOpenEvents().test().run { assertValue(true) }
        CountValve().open().open().open().close().close().close().isOpenEvents().test().run { assertValue(false) }

        CountValve().run {
            for (i in 1..100) {
                open()
            }
            isOpenEvents().test().assertValue(true)
            for (i in 1..98) {
                close()
            }
            isOpenEvents().test().assertValue(true)
            close()
            isOpenEvents().test().assertValue(true)
            close()
            isOpenEvents().test().assertValue(false)
            close()
            close()
            close()
            isOpenEvents().test().assertValue(false)
            open()
            isOpenEvents().test().assertValue(false)
            open()
            isOpenEvents().test().assertValue(false)
            open()
            isOpenEvents().test().assertValue(false)
            open()
            isOpenEvents().test().assertValue(true)
        }

        CountValve(0).close().isOpenEvents().test().run { assertValue(false) }
        CountValve(3).close().isOpenEvents().test().run { assertValue(true) }
        CountValve(3).close().close().isOpenEvents().test().run { assertValue(true) }
        CountValve(3).close().close().close().isOpenEvents().test().run { assertValue(false) }

        CountValve(-2).open().isOpenEvents().test().run { assertValue(false) }
        CountValve(-2).open().open().isOpenEvents().test().run { assertValue(false) }
        CountValve(-2).open().open().open().isOpenEvents().test().run { assertValue(true) }
    }

}