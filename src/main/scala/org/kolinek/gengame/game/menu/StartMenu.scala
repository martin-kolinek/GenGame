package org.kolinek.gengame.game.menu

import org.kolinek.gengame.game.nifty.SimpleScreenController
import org.kolinek.gengame.game.GameControlComponent
import de.lessvoid.nifty.NiftyEventSubscriber
import de.lessvoid.nifty.controls.ButtonClickedEvent
import org.kolinek.gengame.threading.ErrorHelpers
import org.kolinek.gengame.reporting.ErrorLoggingComponent

trait StartMenuComponent extends ErrorHelpers {
    self: MenuComponent with GameControlComponent with ErrorLoggingComponent =>

    class StartMenu extends SimpleScreenController {
        lazy val startButton = new NiftyButton("OKButton")
        lazy val backButton = new NiftyButton("BackButton")

        def setup() {
            startButton.clicks.foreach { _ =>
                gameControl.foreach(_.startGame())
            }
            backButton.clicks.foreach { _ =>
                menu.foreach(_.gotoMainMenu())
            }
        }

        def controls = Nil

        def screenId = "start_game"
    }
}