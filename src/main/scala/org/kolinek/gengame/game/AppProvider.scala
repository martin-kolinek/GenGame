package org.kolinek.gengame.game

import org.kolinek.gengame.threading.{ GameExecutionContextComponent }
import com.jme3.asset.AssetManager
import com.jme3.input.InputManager
import com.jme3.scene.Node
import com.jme3.renderer.Camera
import rx.lang.scala.Observable

trait UnsafeAppProvider {
    def unsafeApp: Game
}

trait AppProvider {
    def app: Observable[Game]
}

trait DefaultAppProvider extends AppProvider {
    self: UnsafeAppProvider with GameExecutionContextComponent =>

    def app = Observable.items(unsafeApp).subscribeOn(gameScheduler).observeOn(gameScheduler)
}

trait SceneGraphProvider {
    def sceneGraphRoot: Observable[Node]
}

trait DefaultSceneGraphProvider extends SceneGraphProvider {
    self: AppProvider =>

    def sceneGraphRoot = app.map(_.getRootNode)
}

trait AssetManagerProvider {
    def assetManager: Observable[AssetManager]
}

trait DefaultAssetManager extends AssetManagerProvider {
    self: AppProvider =>

    def assetManager = app.map(_.getAssetManager)
}

trait InputManagerProvider {
    def inputManager: Observable[InputManager]
}

trait DefaultInputManagerProvider extends InputManagerProvider {
    self: AppProvider =>

    def inputManager = app.map(_.getInputManager)
}

trait JmeCameraComponent {
    def jmeCamera: Observable[Camera]
}

trait DefaultJmeCameraComponent extends JmeCameraComponent {
    self: AppProvider =>

    def jmeCamera = app.map(_.getCamera())
}