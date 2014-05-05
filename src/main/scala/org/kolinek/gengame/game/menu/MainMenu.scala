package org.kolinek.gengame.game.menu

import org.kolinek.gengame.game.nifty.SimpleScreenController
import org.kolinek.gengame.game.SimpleAppState
import org.kolinek.gengame.game.GameControlComponent

trait MainMenuComponent {
    self: MenuComponent with GameControlComponent =>

    class MainMenu extends SimpleScreenController {

        def startGame(): Unit = {
            menu.gotoStartMenu()
        }

        def quitGame(): Unit = {
            gameControl.quitGame()
        }

        def screenId = "start"
    }

}