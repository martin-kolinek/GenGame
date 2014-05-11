package org.kolinek.gengame.game

import rx.lang.scala.Observable
import rx.lang.scala.Subject

trait UpdateStep {
    def update(tpf: Float)
}

trait UpdateComponent {
    def updates: Observable[Float]
}

trait DefaultUpdateComponent extends UpdateStep with UpdateComponent {
    private lazy val updateSubj = Subject[Float]

    lazy val updates = updateSubj

    def update(tpf: Float) = {
        updateSubj.onNext(tpf)
    }
}