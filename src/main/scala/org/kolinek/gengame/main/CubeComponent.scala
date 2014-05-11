package org.kolinek.gengame.main

import org.kolinek.gengame.threading.ErrorHelpers
import org.kolinek.gengame.reporting.ErrorLoggingComponent
import com.jme3.scene.shape.Box
import com.jme3.scene.Geometry
import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import org.kolinek.gengame.game.AppProvider
import org.kolinek.gengame.game.SceneGraphProvider
import org.kolinek.gengame.game.AssetManagerProvider

trait CubeComponent extends ErrorHelpers {
    self: SceneGraphProvider with ErrorLoggingComponent with AssetManagerProvider =>

    for {
        root <- sceneGraphRoot
        assets <- assetManager
    } {
        val b = new Box(1, 1, 1)
        val geom = new Geometry("Box", b)
        geom.setLocalTranslation(0, 0, -10)
        val mat = new Material(assets, "Common/MatDefs/Misc/Unshaded.j3md")
        mat.setColor("Color", ColorRGBA.Blue)
        geom.setMaterial(mat)
        root.attachChild(geom)
    }
}