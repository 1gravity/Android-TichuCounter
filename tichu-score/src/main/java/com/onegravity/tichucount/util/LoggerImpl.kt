package com.onegravity.tichucount.util

import android.util.Log

object LoggerImpl : Logger {

    override fun v(tag: String, s: String) {
        Log.v(tag, s)
    }

    override fun d(tag: String, s: String) {
        Log.d(tag, s)
    }

    override fun i(tag: String, s: String) {
        Log.i(tag, s)
    }

    override fun w(tag: String, s: String) {
        Log.w(tag, s)
    }

    override fun w(tag: String, s: String, t: Throwable) {
        Log.w(tag, s, t)
    }

    override fun e(tag: String, s: String) {
        Log.e(tag, s)
    }

    override fun e(tag: String, s: String, t: Throwable) {
        Log.e(tag, s, t)
    }

}
