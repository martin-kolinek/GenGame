package org.kolinek.gengame.game.nifty

import org.kolinek.gengame.util.Closeable
import com.jme3.niftygui.NiftyJmeDisplay
import org.kolinek.gengame.game.AppProvider
import org.kolinek.gengame.util.OnCloseProvider
import org.bushe.swing.event.EventBus
import scala.collection.mutable.HashMap
import org.kolinek.gengame.game.Game

private object NiftyRepository {
    private val niftyMap = new HashMap[Game, NiftyJmeDisplay]

    def niftyFor(game: Game) = synchronized {
        val ret = niftyMap.get(game)
        ret match {
            case Some(disp) => disp
            case None => {
                val disp = new NiftyJmeDisplay(game.getAssetManager(), game.getInputManager(), game.getAudioRenderer(), game.getGuiViewPort())
                niftyMap(game) = disp
                disp
            }
        }
    }
}

trait GameNiftyProvider extends NiftyProvider {
    self: AppProvider with OnCloseProvider =>

    lazy val niftyDisplay = (for (a <- app) yield {
        val disp = NiftyRepository.niftyFor(a)
        a.getGuiViewPort.addProcessor(disp)
        disp
    }).cache

    lazy val nifty = niftyDisplay.map(_.getNifty)

    onClose.foreach { _ =>
        for {
            a <- app
            d <- niftyDisplay
        } {
            a.getGuiViewPort.removeProcessor(d)
        }
    }
}