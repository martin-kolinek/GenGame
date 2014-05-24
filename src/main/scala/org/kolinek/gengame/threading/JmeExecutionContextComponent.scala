package org.kolinek.gengame.threading

import java.util.concurrent.Callable
import scala.concurrent.ExecutionContext
import org.kolinek.gengame.game.Game
import rx.lang.scala.Observable
import org.kolinek.gengame.game.UnsafeAppProvider
import javax.swing.BoundedRangeModel
import org.kolinek.gengame.game.UnsafeAppProvider
import org.kolinek.gengame.reporting.ErrorLoggingComponent
import scala.concurrent.Future
import rx.lang.scala.Scheduler
import rx.lang.scala.Scheduler
import rx.Scheduler.Worker
import rx.subscriptions.BooleanSubscription
import java.util.concurrent.Executors
import rx.functions.Action0
import java.util.concurrent.TimeUnit
import java.util.concurrent.ThreadFactory
import rx.subscriptions.Subscriptions

trait GameExecutionContextComponent {
    def gameScheduler: Scheduler
}

class JmeScheduler(app: Game) extends Scheduler {

    private val schedExec = Executors.newSingleThreadScheduledExecutor(new ThreadFactory {
        def newThread(r: Runnable) = {
            val t = new Thread(r)
            t.setDaemon(true)
            t
        }
    })

    val asJavaScheduler = new rx.Scheduler {
        def createWorker() = new Worker {

            val subs = new BooleanSubscription
            def unsubscribe() = {
                subs.unsubscribe()
            }
            def isUnsubscribed() = subs.isUnsubscribed()
            def schedule(act: Action0) = {
                if (app.isOnUpdateLoop) {
                    act.call()
                    Subscriptions.empty()
                } else {
                    schedule(act, 0L, TimeUnit.MILLISECONDS)
                }
            }
            def schedule(act: Action0, time: Long, unit: TimeUnit) = {
                val subscr = new BooleanSubscription
                schedExec.schedule(new Runnable {
                    def run() {
                        if (!subscr.isUnsubscribed()) {
                            app.enqueue(new Callable[Unit] {
                                def call() {
                                    act.call()
                                }
                            })
                        }
                    }
                }, time, unit)
                subscr
            }
        }
    }
}

trait JmeExecutionComponent extends GameExecutionContextComponent {
    self: UnsafeAppProvider =>
    lazy val gameScheduler = new JmeScheduler(unsafeApp)
}
