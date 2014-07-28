package org.kolinek.gengame.util

class ThreadLocalVal[T](init: => T) {
    private val threadLocal = new ThreadLocal[T]

    def get = {
        val ret = threadLocal.get
        if (ret == null) {
            val initialized = init
            threadLocal.set(initialized)
            initialized
        } else ret
    }
}