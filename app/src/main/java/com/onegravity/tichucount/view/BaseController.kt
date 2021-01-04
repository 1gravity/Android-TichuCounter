package com.onegravity.tichucount.view

import android.content.Context
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.onegravity.tichucount.APP_SCOPE
import com.onegravity.tichucount.util.Logger
import hu.akarnokd.rxjava3.util.CompositeSubscription
import io.reactivex.rxjava3.disposables.CompositeDisposable
import toothpick.Scope
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject

@Suppress("LeakingThis")
abstract class BaseController : Controller() {

    protected val scope: Scope = KTP.openRootScope().openSubScope(APP_SCOPE)
    protected val disposables by lazy { CompositeDisposable() }
    protected val subscriptions by lazy { CompositeSubscription() }

    protected val logger: Logger by inject()
    protected val appContext: Context by inject()

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
    override fun onDestroy() {
        disposables.clear()
        subscriptions.cancel()
    }

    protected fun getString(@StringRes id: Int) = appContext.getString(id)

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

    protected fun createRouterTx(controller: Controller) =
        RouterTransaction.with(controller)
            .tag(controller.javaClass.simpleName)
            .pushChangeHandler(FadeChangeHandler())
            .popChangeHandler(FadeChangeHandler())

}
