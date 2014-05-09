package org.kolinek.gengame.game.menu

import org.kolinek.gengame.game.nifty.SimpleScreenController
import org.kolinek.gengame.game.GameControlComponent
import de.lessvoid.nifty.NiftyEventSubscriber
import de.lessvoid.nifty.controls.ButtonClickedEvent

trait StartMenuComponent {
    self: MenuComponent with GameControlComponent =>

    class StartMenu extends SimpleScreenController {
        @NiftyEventSubscriber(id = "OKButton")
        def startGame(id: String, ev: ButtonClickedEvent) = {
            gameControl.startGame()
        }

        @NiftyEventSubscriber(id = "BackButton")
        def back(id: String, ev: ButtonClickedEvent) = {
            menu.gotoMainMenu()
        }

        def screenId = "start_game"
    }
}