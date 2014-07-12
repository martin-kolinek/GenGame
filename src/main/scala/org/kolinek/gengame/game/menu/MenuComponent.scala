package org.kolinek.gengame.game.menu

import org.kolinek.gengame.game.nifty.NiftyProvider
import org.kolinek.gengame.game.GameControlComponent
import de.lessvoid.nifty.screen.ScreenController
import org.kolinek.gengame.config.ConfigProvider
import org.kolinek.gengame.config.GraphicsConfigProvider
import org.kolinek.gengame.threading.GameExecutionContextComponent
import de.lessvoid.nifty.Nifty
import org.kolinek.gengame.config.ConfigUpdaterComponent
import rx.lang.scala.Observable

trait Menu {
    def gotoMainMenu()
    def gotoStartMenu()
    def gotoOptions()
}

trait MenuComponent {
    def menu: Observable[Menu]
}

trait GameMenuComponent extends MenuComponent with MainMenuComponent with StartMenuComponent with OptionsComponent {
    self: NiftyProvider with GameControlComponent with GraphicsConfigProvider with GameExecutionContextComponent with ConfigUpdaterComponent with ConfigProvider =>

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