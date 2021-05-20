package com.onegravity.tichucount.util

object LoggerTestImpl : Logger {

    override fun v(tag: String, s: String) {
        println("V $tag: $s")
    }

    override fun d(tag: String, s: String) {
        println("D $tag: $s")
    }

    override fun i(tag: String, s: String) {
        println("I $tag: $s")
    }

    override fun w(tag: String, s: String) {
        println("W $tag: $s")
    }

    override fun w(tag: String, s: String, t: Throwable) {
        println("W $tag: $s")
        t.printStackTrace()
    }

    override fun e(tag: String, s: String) {
        println("E $tag: $s")
    }

    override fun e(tag: String, s: String, t: Throwable) {
        println("E $tag: $s")
        t.printStackTrace()
    }

}
