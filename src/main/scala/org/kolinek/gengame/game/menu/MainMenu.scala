package org.kolinek.gengame.game.menu

import org.kolinek.gengame.game.nifty.SimpleScreenController
import org.kolinek.gengame.game.SimpleAppState
import org.kolinek.gengame.game.GameControlComponent
import de.lessvoid.nifty.NiftyEventSubscriber
import de.lessvoid.nifty.controls.ButtonClickedEvent

trait MainMenuComponent {
    self: MenuComponent with GameControlComponent =>

    class MainMenu extends SimpleScreenController {

        @NiftyEventSubscriber(id = "StartButton")
        def startGame(id: String, ev: ButtonClickedEvent): Unit = {
            menu.gotoStartMenu()
        }

        @NiftyEventSubscriber(id = "OptionsButton")
        def options(id: String, ev: ButtonClickedEvent): Unit = {
            menu.gotoOptions()
        }

        @NiftyEventSubscriber(id = "QuitButton")
        def quitGame(id: String, ev: ButtonClickedEvent): Unit = {
            gameControl.quitGame()
        }

        def screenId = "start"
    }

}