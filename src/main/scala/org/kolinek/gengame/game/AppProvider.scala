package org.kolinek.gengame.game

import com.jme3.app.Application
import com.jme3.app.SimpleApplication
import org.kolinek.gengame.threading.BoundFuture
import org.kolinek.gengame.threading.GameExecutionContextComponent
import com.jme3.scene.Node
import com.jme3.asset.AssetManager
import com.jme3.input.InputManager

trait UnsafeAppProvider {
    def unsafeApp: Game
}

trait AppProvider {
    def app: BoundFuture[Game]
}

trait DefaultAppProvider extends AppProvider {
    self: UnsafeAppProvider with GameExecutionContextComponent =>

    def app = BoundFuture(gameExecutionContext)(unsafeApp)
}

trait SceneGraphProvider {
    def sceneGraphaphRoot: BoundFuture[Node]
}

trait DefaultSceneGraphProvider {
    self: AppProvider =>

    def sceneGraphRoot = app.map(_.getRootNode)
}

trait AssetManagerProvider {
    def assetManager: BoundFuture[AssetManager]
}

trait DefaultAssetManager {
    self: AppProvider =>

    def assetManager = app.map(_.getAssetManager)
}

trait InputManagerProvider {
    def inputManager: BoundFuture[InputManager]
}

trait DefaultInputManagerProvider {
    self: AppProvider =>

    def inputManager = app.map(_.getInputManager)
}