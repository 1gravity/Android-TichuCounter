package com.onegravity.tichucount.util

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class Valve(private var currentState: ValveState = ValveState.CLOSED) {

    private val valve = BehaviorSubject.create<ValveState>()
        .apply { onNext(currentState) }

    fun isOpen(): Observable<Boolean> = valve.map { it == ValveState.OPENED }

    @Synchronized
    fun open() = this.also {
        if (currentState == ValveState.CLOSED) {
            currentState = ValveState.OPENED
            valve.onNext(currentState)
        }
    }

    @Synchronized
    fun close() = this.also {
        if (currentState == ValveState.OPENED) {
            currentState = ValveState.CLOSED
            valve.onNext(currentState)
        }
    }

}
