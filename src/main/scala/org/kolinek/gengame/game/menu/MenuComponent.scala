package org.kolinek.gengame.game.menu

import org.kolinek.gengame.game.nifty.NiftyProvider
import org.kolinek.gengame.game.GameControlComponent
import de.lessvoid.nifty.screen.ScreenController
import org.kolinek.gengame.config.ConfigProvider
import org.kolinek.gengame.config.GraphicsConfigProvider
import org.kolinek.gengame.threading.GameExecutionContextComponent
import org.kolinek.gengame.config.ConfigSaver
import org.kolinek.gengame.config.ConfigSaverComponent

trait Menu {
    def gotoMainMenu()
    def gotoStartMenu()
    def gotoOptions()
}

trait MenuComponent {
    def menu: Menu
}

trait GameMenuComponent extends MenuComponent with MainMenuComponent with StartMenuComponent with OptionsComponent {
    self: NiftyProvider with GameControlComponent with GraphicsConfigProvider with GameExecutionContextComponent with ConfigSaverComponent =>

    lazy val mainMenu = new MainMenu
    lazy val startMenu = new StartMenu
    lazy val options = new OptionsController

    object GameMenu extends Menu {
        def gotoMainMenu() = {
            nifty.map(_.gotoScreen(mainMenu.screenId))
        }
        def gotoStartMenu() = {
            nifty.map(_.gotoScreen(startMenu.screenId))
        }
        def gotoOptions() = {
            nifty.map(_.gotoScreen(options.screenId))
        }
    }

    def menu = GameMenu

    nifty.foreach(_.fromXml("gui/menu/gui.xml", "start", mainMenu, startMenu, options))
}