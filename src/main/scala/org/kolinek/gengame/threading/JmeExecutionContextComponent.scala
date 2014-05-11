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

trait GameExecutionContextComponent {
    def gameExecutionContext: ExecutionContext
}

class JmeExecutionContext(app: Game) extends ExecutionContext {
    def execute(runnable: Runnable) = {
        if (app.isOnUpdateLoop)
            runnable.run()
        else {
            app.enqueue(new Callable[Unit] {
                def call() = runnable.run()
            })
        }
    }

    def reportFailure(cause: Throwable) = cause.printStackTrace()
}

trait JmeExecutionComponent extends GameExecutionContextComponent {
    self: UnsafeAppProvider =>
    lazy val gameExecutionContext = new JmeExecutionContext(unsafeApp)
}

trait GameExecutionHelper extends ErrorHelpers {
    self: GameExecutionContextComponent with ErrorLoggingComponent =>

    implicit class SubscribeGameExecOps[T](obs: Observable[T]) {
        def subscribeOnUpdateLoop(act: T => Unit) = obs.foreach { x =>
            val f = Future(act(x))(gameExecutionContext)
            f.onFailure {
                case thr => errorLogger.logError(thr)
            }(gameExecutionContext)
        }
    }
}