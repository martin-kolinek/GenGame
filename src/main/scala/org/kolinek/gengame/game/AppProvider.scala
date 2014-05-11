package org.kolinek.gengame.game

import org.kolinek.gengame.threading.{ BoundFuture, GameExecutionContextComponent }
import com.jme3.asset.AssetManager
import com.jme3.input.InputManager
import com.jme3.scene.Node
import com.jme3.renderer.Camera

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
    def sceneGraphRoot: BoundFuture[Node]
}

trait DefaultSceneGraphProvider extends SceneGraphProvider {
    self: AppProvider =>

    def sceneGraphRoot = app.map(_.getRootNode)
}

trait AssetManagerProvider {
    def assetManager: BoundFuture[AssetManager]
}

trait DefaultAssetManager extends AssetManagerProvider {
    self: AppProvider =>

    def assetManager = app.map(_.getAssetManager)
}

trait InputManagerProvider {
    def inputManager: BoundFuture[InputManager]
}

trait DefaultInputManagerProvider extends InputManagerProvider {
    self: AppProvider =>

    def inputManager = app.map(_.getInputManager)
}

trait JmeCameraComponent {
    def jmeCamera: BoundFuture[Camera]
}

trait DefaultJmeCameraComponent extends JmeCameraComponent {
    self: AppProvider =>

    def jmeCamera = app.map(_.getCamera())
}