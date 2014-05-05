package org.kolinek.gengame.game.menu

import org.kolinek.gengame.game.nifty.NiftyProvider
import org.kolinek.gengame.game.GameControlComponent
import de.lessvoid.nifty.screen.ScreenController

trait Menu {
    def gotoMainMenu()
    def gotoStartMenu()
}

trait MenuComponent {
    def menu: Menu
}

trait GameMenuComponent extends MenuComponent with MainMenuComponent with StartMenuComponent{
    self: NiftyProvider with GameControlComponent =>

    lazy val mainMenu = new MainMenu
    lazy val startMenu = new StartMenu

    object GameMenu extends Menu {
        def gotoMainMenu() = {
            nifty.gotoScreen(mainMenu.screenId)
        }
        def gotoStartMenu() = {
        	nifty.gotoScreen(startMenu.screenId)
        }
    }

    def menu = GameMenu

    nifty.fromXml("gui/menu/gui.xml", "start", mainMenu, startMenu)
}