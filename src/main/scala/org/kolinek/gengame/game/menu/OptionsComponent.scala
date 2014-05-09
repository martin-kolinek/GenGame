package org.kolinek.gengame.game.menu

import org.kolinek.gengame.game.nifty.SimpleScreenController
import org.kolinek.gengame.config.GraphicsConfigProvider
import org.kolinek.gengame.threading.GameExecutionHelper
import org.kolinek.gengame.threading.GameExecutionContextComponent
import de.lessvoid.nifty.screen.Screen
import de.lessvoid.nifty.controls.TextField
import com.typesafe.scalalogging.slf4j.LazyLogging
import org.kolinek.gengame.config.ConfigSaver
import de.lessvoid.nifty.controls.Button
import org.kolinek.gengame.config.ConfigSaverComponent
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

trait OptionsComponent extends GameExecutionHelper {
    self: MenuComponent with GraphicsConfigProvider with GameExecutionContextComponent with ConfigSaverComponent =>

    class OptionsController extends SimpleScreenController with LazyLogging {

        lazy val widthTf = new NiftyTextField("WidthTextField")
        lazy val heightTf = new NiftyTextField("HeightTextField")

        lazy val saveButton = new NiftyButton("SaveButton")
        lazy val backButton = new NiftyButton("BackButton")

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

            optionGraphicsConfig.map(_.isDefined).subscribeOnUpdateLoop { defined =>
                println(defined)
                saveButton.setEnabled(defined)
            }

            /*saveButton.clicks.subscribe { _ =>
                configSaver.saveConfig(config)
                menu.gotoMainMenu()
            }*/

            backButton.clicks.subscribe { _ =>
                menu.gotoMainMenu()
            }
        }

        def screenId = "options"
    }
}