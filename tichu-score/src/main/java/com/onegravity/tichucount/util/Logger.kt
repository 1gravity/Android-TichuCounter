package com.onegravity.tichucount.util

const val LOGGER_TAG = "TICHU"

interface Logger {

    fun v(tag: String, s: String)
    fun d(tag: String, s: String)
    fun i(tag: String, s: String)
    fun w(tag: String, s: String)
    fun w(tag: String, s: String, t: Throwable)
    fun e(tag: String, s: String)
    fun e(tag: String, s: String, t: Throwable)

}
