package org.kolinek.gengame.game.menu

import org.kolinek.gengame.game.nifty.NiftyProvider
import org.kolinek.gengame.game.GameControlComponent
import de.lessvoid.nifty.screen.ScreenController
import org.kolinek.gengame.config.ConfigProvider
import org.kolinek.gengame.config.GraphicsConfigProvider
import org.kolinek.gengame.threading.GameExecutionContextComponent
import org.kolinek.gengame.config.ConfigSaver
import org.kolinek.gengame.config.ConfigSaverComponent
import de.lessvoid.nifty.Nifty
import org.kolinek.gengame.threading.BoundFuture

trait Menu {
    def gotoMainMenu()
    def gotoStartMenu()
    def gotoOptions()
}

trait MenuComponent {
    def menu: BoundFuture[Menu]
}

trait GameMenuComponent extends MenuComponent with MainMenuComponent with StartMenuComponent with OptionsComponent {
    self: NiftyProvider with GameControlComponent with GraphicsConfigProvider with GameExecutionContextComponent with ConfigSaverComponent with ConfigProvider =>

    lazy val mainMenu = new MainMenu
    lazy val startMenu = new StartMenu
    lazy val options = new OptionsController

    class GameMenu(nifty: Nifty) extends Menu {
        def gotoMainMenu() = {
            nifty.gotoScreen(mainMenu.screenId)
        }
        def gotoStartMenu() = {
            nifty.gotoScreen(startMenu.screenId)
        }
        def gotoOptions() = {
            nifty.gotoScreen(options.screenId)
        }
    }

    def menu = nifty.map(new GameMenu(_))

    nifty.foreach(_.fromXml("gui/menu/gui.xml", "start", mainMenu, startMenu, options))
}