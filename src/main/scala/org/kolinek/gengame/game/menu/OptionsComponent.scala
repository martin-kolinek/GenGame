package org.kolinek.gengame.game.menu

import org.kolinek.gengame.game.nifty.SimpleScreenController
import org.kolinek.gengame.config.GraphicsConfigProvider
import org.kolinek.gengame.threading.GameExecutionHelper
import org.kolinek.gengame.threading.GameExecutionContextComponent
import de.lessvoid.nifty.screen.Screen
import de.lessvoid.nifty.controls.TextField
import com.typesafe.scalalogging.slf4j.LazyLogging
import de.lessvoid.nifty.controls.Button
import de.lessvoid.nifty.NiftyEventSubscriber
import de.lessvoid.nifty.controls.ButtonClickedEvent
import de.lessvoid.nifty.controls.TextFieldChangedEvent
import org.kolinek.gengame.util.parseInt
import rx.Observable
import rx.lang.scala.JavaConversions._
import org.kolinek.gengame.config.GraphicsConfig
import org.kolinek.gengame.util.sequence
import org.kolinek.gengame.util.optionApplicative
import org.kolinek.gengame.util.tupleFlatten
import org.kolinek.gengame.util.SomeOps
import org.kolinek.gengame.config.GraphicsConfig
import rx.subjects.BehaviorSubject
import org.kolinek.gengame.util.withLatest
import org.kolinek.gengame.util.collectPartFunc
import org.kolinek.gengame.config.ConfigProvider
import net.ceedubs.ficus.FicusConfig._
import org.kolinek.gengame.config.ConfigUpdaterComponent
import org.kolinek.gengame.config.Lenses
import org.kolinek.gengame.threading.ErrorHelpers
import org.kolinek.gengame.reporting.ErrorLoggingComponent

trait OptionsComponent extends GameExecutionHelper with ErrorHelpers {
    self: MenuComponent with GraphicsConfigProvider with GameExecutionContextComponent with ConfigUpdaterComponent with ConfigProvider with ErrorLoggingComponent =>

    class OptionsController extends SimpleScreenController with LazyLogging {

        lazy val widthTf = new NiftyTextField("WidthTextField")
        lazy val heightTf = new NiftyTextField("HeightTextField")

        lazy val applyButton = new NiftyButton("ApplyButton")
        lazy val backButton = new NiftyButton("BackButton")

        def controls = List(widthTf, heightTf, applyButton, backButton)

        override def setup(): Unit = {

            graphicsConfig.subscribeOnUpdateLoop { conf =>
                widthTf.setText(conf.width.toString)
                heightTf.setText(conf.height.toString)
            }

            val optionGraphicsConfig = (widthTf.text.map(parseInt)
                combineLatest heightTf.text.map(parseInt)
                combineLatest Observable.just(false.some))
                .map(tupleFlatten)
                .map(sequence(_).map(GraphicsConfig.tupled))

            optionGraphicsConfig.map(_.isDefined).subscribeOnUpdateLoop(applyButton.setEnabled)

            val graphConf = optionGraphicsConfig.collectPartFunc {
                case Some(c) => c
            }

            applyButton.clicks.withLatest(graphConf).foreach {
                case (_, conf) =>
                    configUpdater.updateConfig(Lenses.graphicsConfigLens.set(_)(conf))
            }

            backButton.clicks.foreach { _ =>
                menu.foreach(_.gotoMainMenu())
            }
        }
        def screenId = "options"
    }
}