package org.kolinek.gengame.game.nifty

import de.lessvoid.nifty.Nifty
import rx.lang.scala.Observable

trait NiftyProvider {
    def nifty: Observable[Nifty]
}