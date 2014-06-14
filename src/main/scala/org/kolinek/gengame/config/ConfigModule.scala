package org.kolinek.gengame.config

import org.kolinek.gengame.reporting.ErrorLoggingComponent

trait ConfigModule
        extends DefaultConfigProvider
        with DefaultConfigSaver
        with DefaultControlsConfigProvider {
    self: ErrorLoggingComponent =>
}