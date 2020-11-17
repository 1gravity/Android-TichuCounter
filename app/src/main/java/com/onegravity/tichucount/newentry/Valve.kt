package com.onegravity.tichucount.newentry

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class Valve(private val initState: State = State.CLOSED) {

    enum class State {
        OPEN,
        CLOSED
    }

    private val valve = PublishSubject.create<State>()
        .apply { onNext(initState) }

    fun isOpen(): Observable<Boolean> = valve.map { it == State.OPEN }

    fun open() = valve.onNext(State.OPEN)

    fun close()= valve.onNext(State.CLOSED)

}
