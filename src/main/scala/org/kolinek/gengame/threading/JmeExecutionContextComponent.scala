package org.kolinek.gengame.threading

import java.util.concurrent.Callable
import scala.concurrent.ExecutionContext
import org.kolinek.gengame.game.AppProvider
import org.kolinek.gengame.game.Game
import org.kolinek.gengame.util.subscribeFuture
import rx.lang.scala.Observable

trait GameExecutionContextComponent {
    def gameExecutionContext: ExecutionContext
}

class JmeExecutionContext(app: Game) extends ExecutionContext {
    def execute(runnable: Runnable) = {
        app.enqueue(new Callable[Unit] {
            def call() = runnable.run()
        })
    }

    def reportFailure(cause: Throwable) = cause.printStackTrace()
}

trait JmeExecutionComponent extends GameExecutionContextComponent {
    self: AppProvider =>
    lazy val gameExecutionContext = new JmeExecutionContext(app)
}

trait GameExecutionHelper {
    self: GameExecutionContextComponent =>

    implicit class SubscribeGameExecOps[T](obs: Observable[T]) {
        def subscribeOnUpdateLoop(act: T => Unit) = obs.subscribeFuture(gameExecutionContext)(act)
    }
}