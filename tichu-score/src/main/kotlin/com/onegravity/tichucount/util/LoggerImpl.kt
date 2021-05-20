package com.onegravity.tichucount.util

import android.util.Log

class LoggerImpl(private val enabled: Boolean) : Logger {

    override fun v(tag: String, s: String) {
        if (enabled) Log.v(tag, s)
    }

    override fun d(tag: String, s: String) {
        if (enabled)  Log.d(tag, s)
    }

    override fun i(tag: String, s: String) {
        if (enabled) Log.i(tag, s)
    }

    override fun w(tag: String, s: String) {
        if (enabled) Log.w(tag, s)
    }

    override fun w(tag: String, s: String, t: Throwable) {
        if (enabled) Log.w(tag, s, t)
    }

    override fun e(tag: String, s: String) {
        if (enabled) Log.e(tag, s)
    }

    override fun e(tag: String, s: String, t: Throwable) {
        if (enabled) Log.e(tag, s, t)
    }

}
