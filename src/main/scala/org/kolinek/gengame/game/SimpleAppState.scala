package org.kolinek.gengame.game

import com.jme3.app.state.AbstractAppState
import com.jme3.app.SimpleApplication
import com.jme3.app.Application
import com.jme3.app.state.AppStateManager
import org.kolinek.gengame.util.Closeable

trait SimpleAppState extends AbstractAppState {
    self =>

    private var appVar: Application = null
    def app = appVar

    private var compVar: Closeable with UpdateStep = null

    trait GameAppProvider extends AppProvider {
        def app = self.app.asInstanceOf[Game]
    }

    override final def initialize(stateManager: AppStateManager, app: Application) = {
        appVar = app
        compVar = component
    }

    protected def component: Closeable with UpdateStep

    override final def cleanup() = {
        compVar.close()
    }

    override final def update(tpf: Float) = {
        compVar.update(tpf)
    }
}