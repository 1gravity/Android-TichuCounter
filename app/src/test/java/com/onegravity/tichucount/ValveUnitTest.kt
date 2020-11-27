package com.onegravity.tichucount

import com.onegravity.tichucount.util.Valve
import com.onegravity.tichucount.util.ValveState
import org.junit.Test

class ValveUnitTest {

    @Test
    fun tests() {
        Valve().isOpen().test().run { assertValue(false) }

        Valve(ValveState.CLOSED).isOpen().test().run { assertValue(false) }
        Valve(ValveState.OPENED).isOpen().test().run { assertValue(true) }

        Valve().open().isOpen().test().run { assertValue(true) }
        Valve().close().isOpen().test().run { assertValue(false) }

        Valve(ValveState.CLOSED).open().isOpen().test().run { assertValue(true) }
        Valve(ValveState.OPENED).close().isOpen().test().run { assertValue(false) }

        Valve().close().open().close().isOpen().test().run { assertValue(false) }
        Valve().open().open().open().isOpen().test().run { assertValue(true) }
    }

}