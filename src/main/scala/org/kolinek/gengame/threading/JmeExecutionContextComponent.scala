package org.kolinek.gengame.threading

import java.util.concurrent.Callable
import scala.concurrent.ExecutionContext
import org.kolinek.gengame.game.Game
import org.kolinek.gengame.util.subscribeFuture
import rx.lang.scala.Observable
import org.kolinek.gengame.game.UnsafeAppProvider
import javax.swing.BoundedRangeModel
import org.kolinek.gengame.game.UnsafeAppProvider

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
    self: UnsafeAppProvider =>
    lazy val gameExecutionContext = new JmeExecutionContext(unsafeApp)
}

trait AppProvider {
    def app: BoundFuture[Game]
}

trait DefaultAppProvider extends AppProvider {
    self: UnsafeAppProvider with JmeExecutionComponent =>

    def app = BoundFuture(gameExecutionContext)(unsafeApp)
}

trait GameExecutionHelper {
    self: GameExecutionContextComponent =>

    implicit class SubscribeGameExecOps[T](obs: Observable[T]) {
        def subscribeOnUpdateLoop(act: T => Unit) = obs.subscribeFuture(gameExecutionContext)(act)
    }
}