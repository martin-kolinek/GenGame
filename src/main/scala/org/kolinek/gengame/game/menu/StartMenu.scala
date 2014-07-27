package org.kolinek.gengame.game.menu

import org.kolinek.gengame.game.nifty.SimpleScreenController
import org.kolinek.gengame.game.GameControlComponent
import de.lessvoid.nifty.NiftyEventSubscriber
import de.lessvoid.nifty.controls.ButtonClickedEvent

trait StartMenuComponent {
    self: MenuComponent with GameControlComponent =>

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

        def controls = Seq(startButton, backButton)

        def screenId = "start_game"
    }
}