package org.kolinek.gengame.game.nifty

import de.lessvoid.nifty.controls.Label

trait LabelComponent {
    self: SimpleScreenController =>

    class NiftyLabel(val id: String) extends SimpleNiftyControl {
        private val underlying = screen.findNiftyControl(id, classOf[Label])
        def events = Nil

        def setText(str: String) {
            underlying.setText(str)
        }
    }
}