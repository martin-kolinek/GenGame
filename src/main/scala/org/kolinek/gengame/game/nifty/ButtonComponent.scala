package org.kolinek.gengame.game.nifty

import de.lessvoid.nifty.controls.Button
import de.lessvoid.nifty.controls.ButtonClickedEvent
import rx.lang.scala.Subject

trait ButtonComponent {
    self: SimpleNiftyControlComponent with SimpleScreenController =>

    class NiftyButton(val id: String) extends SimpleNiftyControl {
        private val underlying = screen.findNiftyControl(id, classOf[Button])

        private val clickSubj = Subject[ButtonClickedEvent]()
        val clicks = clickSubj.obs.map(_ => {})

        def events = List(clickSubj.ei)

        def setEnabled(enabled: Boolean) = {
            if (enabled)
                underlying.enable()
            else
                underlying.disable()
        }
    }
}