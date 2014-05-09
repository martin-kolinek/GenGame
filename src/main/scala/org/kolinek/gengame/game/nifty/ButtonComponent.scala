package org.kolinek.gengame.game.nifty

import de.lessvoid.nifty.controls.Button
import de.lessvoid.nifty.controls.ButtonClickedEvent

trait ButtonComponent {
    self: SimpleScreenController =>

    class NiftyButton(id: String) {
        private val underlying = screen.findNiftyControl(id, classOf[Button])

        lazy val clicks = observable[ButtonClickedEvent](underlying).map(x => {})

        def setEnabled(enabled: Boolean) = {
            if (enabled)
                underlying.enable()
            else
                underlying.disable()
        }
    }
}