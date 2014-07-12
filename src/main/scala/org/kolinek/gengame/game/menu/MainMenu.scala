package org.kolinek.gengame.game.menu

import org.kolinek.gengame.game.nifty.SimpleScreenController
import org.kolinek.gengame.game.SimpleAppState
import org.kolinek.gengame.game.GameControlComponent
import de.lessvoid.nifty.NiftyEventSubscriber
import de.lessvoid.nifty.controls.ButtonClickedEvent

trait MainMenuComponent {
    self: MenuComponent with GameControlComponent =>

    class MainMenu extends SimpleScreenController {

        val startButton = new NiftyButton("StartButton")
        val optionsButton = new NiftyButton("OptionsButton")
        val quitButton = new NiftyButton("QuitButton")

        def controls = List(startButton, optionsButton, quitButton)

        def setup() {
            startButton.clicks.foreach { _ =>
                menu.foreach(_.gotoStartMenu())
            }
            optionsButton.clicks.foreach { _ =>
                menu.foreach(_.gotoOptions())
            }
            quitButton.clicks.foreach { _ =>
                gameControl.foreach(_.quitGame())
            }
        }

        def screenId = "start"
    }

}