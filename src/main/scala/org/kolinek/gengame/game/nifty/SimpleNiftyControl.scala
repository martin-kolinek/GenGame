package org.kolinek.gengame.game.nifty

trait SimpleNiftyControlComponent {
    self: SimpleScreenController =>

    trait SimpleNiftyControl {
        def id: String

        def events: Seq[EventInfo]
    }
}
