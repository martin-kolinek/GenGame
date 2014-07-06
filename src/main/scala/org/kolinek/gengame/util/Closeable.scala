package org.kolinek.gengame.util

import rx.lang.scala.Observable
import rx.lang.scala.subjects.ReplaySubject

trait Closeable {
    def close()
}

trait OnCloseProvider {
    def onClose: Observable[Unit]
}

trait DefaultOnCloseProvider extends Closeable with OnCloseProvider {
    def close() {
        onClose.onNext(Unit)
        onClose.onCompleted()
    }

    lazy val onClose = ReplaySubject[Unit]()
}