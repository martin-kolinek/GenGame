package org.kolinek.gengame.game

import com.jme3.app.Application
import org.kolinek.gengame.game.menu.GuiAppState
import com.jme3.app.FlyCamAppState
import com.jme3.app.DebugKeysAppState
import com.jme3.scene.shape.Box
import com.jme3.scene.Geometry
import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.app.SimpleApplication
import com.jme3.app.StatsAppState
import org.kolinek.gengame.threading.BoundFuture
import org.kolinek.gengame.threading.AppProvider

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
        app.getStateManager.attachAll(new FlyCamAppState, new StatsAppState)
        val b = new Box(1, 1, 1)
        val geom = new Geometry("Box", b)
        val mat = new Material(app.getAssetManager, "Common/MatDefs/Misc/Unshaded.j3md")
        mat.setColor("Color", ColorRGBA.Blue)
        geom.setMaterial(mat)
        app.getRootNode.attachChild(geom)
    }
}

trait AppGameControlComponent extends GameControlComponent {
    self: AppProvider =>
    def gameControl = app.map(new AppGameControl(_))
}