package com.onegravity.tichucount

import com.onegravity.tichucount.util.CountdownValve
import org.junit.Test

class CountdownValveUnitTest {

    @Test
    fun tests() {
        CountdownValve().isOpenEvents().test().run { assertValue(false) }

        CountdownValve().open().isOpenEvents().test().run { assertValue(true) }
        CountdownValve().close().isOpenEvents().test().run { assertValue(false) }

        CountdownValve().close().open().close().isOpenEvents().test().run { assertValue(false) }
        CountdownValve().open().open().open().isOpenEvents().test().run { assertValue(true) }

        CountdownValve().close().open().open().close().isOpenEvents().test().run { assertValue(false) }
        CountdownValve().open().open().open().close().close().isOpenEvents().test().run { assertValue(true) }
        CountdownValve().open().open().open().close().close().close().isOpenEvents().test().run { assertValue(false) }

        CountdownValve().run {
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

        CountdownValve(0).close().isOpenEvents().test().run { assertValue(false) }
        CountdownValve(3).close().isOpenEvents().test().run { assertValue(true) }
        CountdownValve(3).close().close().isOpenEvents().test().run { assertValue(true) }
        CountdownValve(3).close().close().close().isOpenEvents().test().run { assertValue(false) }

        CountdownValve(-2).open().isOpenEvents().test().run { assertValue(false) }
        CountdownValve(-2).open().open().isOpenEvents().test().run { assertValue(false) }
        CountdownValve(-2).open().open().open().isOpenEvents().test().run { assertValue(true) }
    }

}