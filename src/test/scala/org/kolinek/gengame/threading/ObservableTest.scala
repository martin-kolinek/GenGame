/*package org.kolinek.gengame.threading

import org.scalatest.FunSuite
import rx.lang.scala.Subject
import rx.lang.scala.Observable
import rx.schedulers.Schedulers
import rx.lang.scala.JavaConversions._
import rx.Scheduler
import rx.Scheduler.Worker
import rx.functions.Action0
import java.util.concurrent.TimeUnit
import rx.schedulers.EventLoopsScheduler
import java.util.concurrent.Executors
import rx.subscriptions.BooleanSubscription
import java.util.concurrent.ScheduledExecutorService
import rx.Subscription
import rx.subscriptions.Subscriptions

class ObservableTest extends FunSuite {
    test("observeOn creates new observable") {
        val subj = Subject[Int]
        val obs: Observable[Int] = subj
        val obs2 = subj.map(_ + 1)
        val obs3 = obs2.observeOn(Schedulers.newThread)
        assert(obs2 ne obs3)
        val thr = Thread.currentThread.getId
        var thr2 = -1L
        var thr3 = -1L
        obs2.subscribe { _ =>
            thr2 = Thread.currentThread.getId
        }
        obs3.subscribe { _ =>
            thr3 = Thread.currentThread.getId
        }
        subj.onNext(1)
        Thread.sleep(100)
        assert(thr2 >= 0)
        assert(thr3 >= 0)
        assert(thr2 === thr)
        assert(thr3 !== thr)
    }

    class TestScheduler extends Scheduler {

        var threadId = -1L

        val exec = Executors.newSingleThreadScheduledExecutor()

        exec.submit(new Runnable {
            def run() {
                threadId = Thread.currentThread().getId()
            }
        })

        class TestWorker extends Worker {
            val subscript = new BooleanSubscription

            def schedule(act: Action0) = {
                schedule(act, 0, TimeUnit.MILLISECONDS)
            }
            def isUnsubscribed = subscript.isUnsubscribed()
            def unsubscribe() = subscript.unsubscribe()
            def schedule(act: Action0, amount: Long, timeUnit: TimeUnit) = {
                class CancellableRunnable(act: Action0) extends Runnable {
                    @volatile private var cancelVar = false
                    def cancel() {
                        cancelVar = true
                    }

                    def run() {
                        if (!cancelVar)
                            act.call()
                    }
                }

                val canc = new CancellableRunnable(act)

                exec.schedule(canc, amount, timeUnit)

                Subscriptions.create(new Action0 {
                    def call() = {
                        canc.cancel()
                    }
                })
            }
        }

        def createWorker = new TestWorker
    }

    test("flatMap threading") {
        println(s"thread ${Thread.currentThread().getId()}")

        val sch1 = new TestScheduler
        val sch2 = new TestScheduler
        val sch3 = new TestScheduler
        Thread.sleep(100)
        println(s"sch1: ${sch1.threadId}")
        println(s"sch2: ${sch2.threadId}")
        println(s"sch3: ${sch3.threadId}")

        val obs3 = Observable[Int] { s =>
            println(s"obs3 subscribe ${Thread.currentThread().getId()}")
            s.onNext(1)
            s.onCompleted()
        }

        val obs3s = obs3.subscribeOn(sch1)

        /*obs3s.map { x =>
            println(s"obs3map: ${Thread.currentThread().getId()}")
            x + 1
        }.subscribe { x =>
            println(s"obs3: $x, thread ${Thread.currentThread().getId()}")
        }*/

        val obs4 = Observable.items(2)

        val obs4s = obs4.observeOn(sch2)

        val comb = for {
            i <- obs3s
            i2 <- obs4s
        } yield (i, i2)

        comb.observeOn(sch1).map { x =>
            println(s"combMap: $x ${Thread.currentThread().getId()}")
            x
        }.subscribe { x =>
            println(s"comb: $x ${Thread.currentThread().getId()}")
        }

        /*val fm = for {
            i <- obs
            i2 <- obs2
        } yield {
            println(i -> i2)
            Thread.currentThread().getId()
        }*/
        /*val fm = obs.map { a =>
            println(s"a $a thread ${Thread.currentThread().getId()}")
            val x = obs2.map(b => {
                println(s"b $b")
                Thread.currentThread().getId()
            })

            println(s"a $a mapped")
            x
        }.flatten*/

        /*val fm = obs.flatMap { a =>
            val x = obs2.map(b => {
                println(a -> b)
                Thread.currentThread().getId()
            })
            x
        }*/

        /*fm.observeOn(Schedulers.computation()).subscribe { id =>
            println(id.toString)
        }

        obs.subscribe { _ =>
            println(s"obs thread ${Thread.currentThread().getId()}")
        }
        obs2.subscribe { _ =>
            println(s"obs2 thread ${Thread.currentThread().getId()}")
        }
        /*obs2.map(x => {
            println("test")
        }).subscribe(x => {})*/
        Thread.sleep(100)
        subj.onNext(1)
        Thread.sleep(100)
        //Thread.sleep(10000)
        subj.onNext(2)
        subj.onNext(3)
        subj2.onNext(4)
        subj2.onNext(5)
        subj.onNext(6)
        //Thread.sleep(100)

        subj2.onNext(7)
        Thread.sleep(1000)
*/
    }
}*/