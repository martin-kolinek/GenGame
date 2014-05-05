package org.kolinek.gengame.game

import com.jme3.app.Application
import com.jme3.app.SimpleApplication

trait AppProvider {
    def app: Game
}
