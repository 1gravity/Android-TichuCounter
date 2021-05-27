package com.onegravity.tichucount.view

import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.SimpleSwapChangeHandler
import hu.akarnokd.rxjava3.util.CompositeSubscription
import io.reactivex.rxjava3.disposables.CompositeDisposable

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseController : Controller() {

    protected val disposables by lazy { CompositeDisposable() }
    protected val subscriptions by lazy { CompositeSubscription() }

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

    @CallSuper
    override fun onDetach(view: View) {
        super.onDetach(view)
        disposables.clear()
        subscriptions.cancel()
    }

    protected fun setToolbar(toolbar: Toolbar) {
        // yes very hacky but don't care at the moment
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    protected fun shoWDialog(dialog: DialogFragment) {
        // again very hacky but don't care at the moment
        (activity as? FragmentActivity)?.let {
            dialog.show(it.supportFragmentManager, dialog::class.java.name)
        }
    }

    protected fun push(controller: Controller) {
        RouterTransaction.with(controller)
            .tag(controller.javaClass.simpleName)
            .pushChangeHandler(SimpleSwapChangeHandler())
            .popChangeHandler(FadeChangeHandler())
            .also { router.pushController(it) }
    }

}
