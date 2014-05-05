package org.kolinek.gengame.game.nifty

import de.lessvoid.nifty.screen.ScreenController
import de.lessvoid.nifty.Nifty
import de.lessvoid.nifty.screen.Screen

trait SimpleScreenController extends ScreenController {
    private var niftyVar: Nifty = null
    def nifty = niftyVar

    def bind(nifty: Nifty, screen: Screen) = {
        niftyVar = nifty
    }
    def onStartScreen() = {}
    def onEndScreen() = {}
}