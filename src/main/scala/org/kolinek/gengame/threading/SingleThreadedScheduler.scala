package org.kolinek.gengame.threading

import rx.lang.scala.Scheduler
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import rx.subscriptions.BooleanSubscription
import rx.functions.Action0
import rx.subscriptions.Subscriptions
import java.util.concurrent.TimeUnit

class SingleThreadedScheduler extends Scheduler {
    private val schedExec = Executors.newSingleThreadScheduledExecutor(new ThreadFactory {
        def newThread(r: Runnable) = {
            val t = new Thread(r)
            t.setDaemon(true)
            t
        }
    })

    val asJavaScheduler = new rx.Scheduler {
        def createWorker() = new rx.Scheduler.Worker {

            val subs = new BooleanSubscription
            def unsubscribe() = {
                subs.unsubscribe()
            }
            def isUnsubscribed() = subs.isUnsubscribed()
            def schedule(act: Action0) = {
                schedule(act, 0L, TimeUnit.MILLISECONDS)
            }
            def schedule(act: Action0, time: Long, unit: TimeUnit) = {
                val subscr = new BooleanSubscription
                schedExec.schedule(new Runnable {
                    def run() {
                        if (!subscr.isUnsubscribed()) {
                            act.call()
                        }
                    }
                }, time, unit)
                subscr
            }
        }
    }
}