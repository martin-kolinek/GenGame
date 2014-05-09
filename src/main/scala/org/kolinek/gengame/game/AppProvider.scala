package org.kolinek.gengame.game

import com.jme3.app.Application
import com.jme3.app.SimpleApplication
import org.kolinek.gengame.threading.BoundFuture

trait UnsafeAppProvider {
    def unsafeApp: Game
}