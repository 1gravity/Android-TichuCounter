package com.onegravity.tichucount

import android.view.View
import androidx.annotation.CallSuper
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler

abstract class BaseController : Controller() {

    override fun onChangeStarted(
        changeHandler: ControllerChangeHandler,
        changeType: ControllerChangeType
    ) {
        super.onChangeStarted(changeHandler, changeType)

        when (changeType) {
            ControllerChangeType.PUSH_ENTER, ControllerChangeType.POP_ENTER -> onEnterStarted(view)
            ControllerChangeType.PUSH_EXIT, ControllerChangeType.POP_EXIT -> onExitStarted(view)
        }
    }

    override fun onChangeEnded(
        changeHandler: ControllerChangeHandler,
        changeType: ControllerChangeType
    ) {
        super.onChangeEnded(changeHandler, changeType)

        when (changeType) {
            ControllerChangeType.PUSH_ENTER, ControllerChangeType.POP_ENTER -> onEnterEnded(view)
            ControllerChangeType.PUSH_EXIT, ControllerChangeType.POP_EXIT -> onExitEnded(view)
        }
    }

    /**
     * Convenience methods
     */
    @CallSuper
    protected open fun onEnterStarted(view: View?) {}

    @CallSuper
    protected open fun onExitStarted(view: View?) {}

    @CallSuper
    protected open fun onEnterEnded(view: View?) {}

    @CallSuper
    protected open fun onExitEnded(view: View?) {}

    protected fun createRouterTx(controller: Controller) =
        RouterTransaction.with(controller)
            .tag(controller.javaClass.simpleName)
            .pushChangeHandler(FadeChangeHandler())
            .popChangeHandler(FadeChangeHandler())

}
