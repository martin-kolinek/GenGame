package org.kolinek.gengame.config

import com.typesafe.config.Config
import java.nio.file.Paths
import com.typesafe.config.ConfigFactory
import rx.lang.scala.Observable
import rx.lang.scala.subjects.BehaviorSubject
import org.apache.commons.io.FileUtils
import com.typesafe.config.ConfigValueFactory
import scala.collection.JavaConversions._
import rx.lang.scala.Subscription
import rx.lang.scala.Subject
import rx.lang.scala.JavaConversions._
import org.kolinek.gengame.util.withLatest
import org.kolinek.gengame.threading.ErrorHelpers
import org.kolinek.gengame.reporting.ErrorLoggingComponent

trait ConfigProvider {
    def config: Observable[Config]
}

trait ConfigUpdater {
    def updateConfig(trans: Config => Config): Unit
}

trait ConfigUpdaterComponent {
    def configUpdater: ConfigUpdater
}

trait DefaultConfigProvider extends ConfigProvider with ConfigUpdaterComponent with ErrorHelpers {
    self: ConfigSaver with ErrorLoggingComponent =>

    private lazy val subj = BehaviorSubject(configFromFile)

    lazy val config = subj

    lazy val configUpdater = new ConfigUpdater {
        val updateSubj = Subject[Config => Config]()

        def updateConfig(trans: Config => Config) {
            updateSubj.onNext(trans)
        }

        updateSubj.withLatest(config).map {
            case (trans, cfg) => trans(cfg)
        }.foreach({ x =>
            subj.onNext(x)
        })
    }

}



