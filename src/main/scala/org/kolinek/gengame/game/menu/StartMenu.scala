package org.kolinek.gengame.game.menu

import org.kolinek.gengame.game.nifty.SimpleScreenController
import org.kolinek.gengame.game.GameControlComponent

trait StartMenuComponent {
    self: MenuComponent with GameControlComponent =>

    class StartMenu extends SimpleScreenController {
        def startGame() = {
            gameControl.startGame()
        }

        def back() = {
            menu.gotoMainMenu()
        }

        def screenId = "start_game"
    }
}