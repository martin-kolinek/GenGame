package org.kolinek.gengame.game.nifty

import scala.reflect.ClassTag

trait SimpleNiftyControlComponent {
    self: SimpleScreenController =>

    trait SimpleNiftyControl {
        def id: String

        def events: Seq[EventInfo]
    }
}
