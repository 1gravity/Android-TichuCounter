package com.onegravity.tichucount.util

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.atomic.AtomicInteger

class CountValve(initialCount: Int = 0) {

    private val count = AtomicInteger(initialCount)

    private var currentState = ValveState.CLOSED

    private val valve = BehaviorSubject.create<ValveState>()
        .apply {
            count.set(initialCount)
            currentState = when (initialCount > 0) {
                true -> ValveState.OPENED
                else -> ValveState.CLOSED
            }
            onNext(currentState)
        }

    fun isOpenEvents(): Observable<Boolean> = valve.map { it == ValveState.OPENED }

//    fun isOpen() = currentState == ValveState.OPENED

    @Synchronized
    fun open() = this.also {
        if (count.incrementAndGet() == 1) {
            currentState = ValveState.OPENED
            valve.onNext(currentState)
        }
    }

    @Synchronized
    fun close() = this.also {
        if (count.decrementAndGet() == 0) {
            currentState = ValveState.CLOSED
            valve.onNext(currentState)
        }
    }

}
