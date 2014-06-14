package org.kolinek.gengame.lighting

import org.kolinek.gengame.game.SceneGraphProvider
import org.kolinek.gengame.threading.ErrorHelpers
import org.kolinek.gengame.reporting.ErrorLoggingComponent
import com.jme3.light.DirectionalLight
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import com.jme3.light.AmbientLight

trait LightingProvider extends ErrorHelpers {
    self: SceneGraphProvider with ErrorLoggingComponent =>

    sceneGraphRoot.foreach { root =>
        val sun = new DirectionalLight
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-.5f, -.5f, -.5f).normalizeLocal());
        val ambient = new AmbientLight
        ambient.setColor(ColorRGBA.Blue)
        root.addLight(sun)
        root.addLight(ambient)
    }
}