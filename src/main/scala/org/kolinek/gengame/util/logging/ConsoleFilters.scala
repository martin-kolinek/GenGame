package org.kolinek.gengame.util.logging

import ch.qos.logback.core.filter.AbstractMatcherFilter
import ch.qos.logback.core.spi.FilterReply
import ch.qos.logback.classic.spi.LoggingEvent
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.filter.Filter

class LevelFilter(levels: Level*) extends Filter[ILoggingEvent] {
    override def decide(ev: ILoggingEvent) = {
        val logEv = ev.asInstanceOf[LoggingEvent]
        if (levels.contains(logEv.getLevel))
            FilterReply.ACCEPT
        else
            FilterReply.DENY
    }
}

class BelowWarningFilter extends LevelFilter(Level.TRACE, Level.DEBUG, Level.INFO)

class AboveInfoFilter extends LevelFilter(Level.WARN, Level.ERROR)