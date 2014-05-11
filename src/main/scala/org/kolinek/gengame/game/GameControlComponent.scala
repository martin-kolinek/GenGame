package org.kolinek.gengame.game

import org.kolinek.gengame.game.menu.GuiAppState
import org.kolinek.gengame.threading.BoundFuture
import com.jme3.app.{ FlyCamAppState, StatsAppState }
import org.kolinek.gengame.main.MainAppState

trait GameControl {
    def quitGame(): Unit
    def startGame(): Unit
}

trait GameControlComponent {
    def gameControl: BoundFuture[GameControl]
}

class AppGameControl(app: Game) extends GameControl {
    def quitGame() = app.stop()
    def startGame() = {
        val state = app.getStateManager.getState(classOf[GuiAppState])
        app.getStateManager.detach(state)
        app.getStateManager.attachAll(new MainAppState, new StatsAppState)
    }
}

trait AppGameControlComponent extends GameControlComponent {
    self: AppProvider =>
    def gameControl = app.map(new AppGameControl(_))
}