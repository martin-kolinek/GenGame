package org.kolinek.gengame.main

import org.kolinek.gengame.game.SimpleAppState
import org.kolinek.gengame.game.UpdateStep
import org.kolinek.gengame.util.Closeable

class MainAppState extends SimpleAppState {
    class Component
        extends Closeable
        with UpdateStep

    def component = new Component
}