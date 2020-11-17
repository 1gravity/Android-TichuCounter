package com.onegravity.tichucount.newentry

import hu.akarnokd.rxjava3.operators.ObservableTransformers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.Subject

abstract class Component(private val update: Subject<Boolean>) {

    private val valve = Valve()

    private val disposable = CompositeDisposable()

    fun start() {
        update
            .compose(ObservableTransformers.valve(valve.isOpen()))
            .doOnSubscribe { disposable.add(it) }
            .subscribe { update() }

        changes()
            .doOnSubscribe { disposable.add(it) }
            .subscribe {
                valve.close()
                update.onNext(true)
                valve.open()
            }
    }

    fun stop() {
        disposable.clear()
    }

    protected abstract fun changes(): Observable<Unit>

    protected abstract fun update()

}
