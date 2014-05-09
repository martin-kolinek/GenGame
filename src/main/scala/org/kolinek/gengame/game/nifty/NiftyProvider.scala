package org.kolinek.gengame.game.nifty

import de.lessvoid.nifty.Nifty
import org.kolinek.gengame.threading.BoundFuture

trait NiftyProvider {
    def nifty: BoundFuture[Nifty]
}