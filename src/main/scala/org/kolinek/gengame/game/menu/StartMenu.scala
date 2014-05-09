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
            gameControl.foreach(_.startGame())
        }

        @NiftyEventSubscriber(id = "BackButton")
        def back(id: String, ev: ButtonClickedEvent) = {
            menu.foreach(_.gotoMainMenu())
        }

        def controls = Nil

        def screenId = "start_game"
    }
}