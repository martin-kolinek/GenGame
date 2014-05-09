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
import org.kolinek.gengame.util.withLatest
import org.kolinek.gengame.util.collectPartFunc
import org.kolinek.gengame.util.subs
import org.kolinek.gengame.config.ConfigProvider
import net.ceedubs.ficus.FicusConfig._

trait OptionsComponent extends GameExecutionHelper {
    self: MenuComponent with GraphicsConfigProvider with GameExecutionContextComponent with ConfigSaverComponent with ConfigProvider =>

    class OptionsController extends SimpleScreenController with LazyLogging {

        lazy val widthTf = new NiftyTextField("WidthTextField")
        lazy val heightTf = new NiftyTextField("HeightTextField")

        lazy val saveButton = new NiftyButton("SaveButton")
        lazy val backButton = new NiftyButton("BackButton")

        def controls = List(widthTf, heightTf, saveButton, backButton)

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

            optionGraphicsConfig.map(_.isDefined).subscribeOnUpdateLoop(saveButton.setEnabled)

            val graphConf = optionGraphicsConfig.collectPartFunc {
                case Some(c) => c
            }

            val updatedConf = config.combineLatest(graphConf).map {
                case (cfg, g) => g.updateConfigSettings(cfg)
            }

            saveButton.clicks.withLatest(updatedConf).subs {
                case (_, conf) =>
                    configSaver.saveConfig(conf)
                    menu.foreach(_.gotoMainMenu())

            }

            backButton.clicks.subscribe { _ =>
                menu.foreach(_.gotoMainMenu())
            }
        }
        def screenId = "options"
    }
}