package org.xvolks.dofusMiM.plugins.logger

import java.io.BufferedWriter
import java.io.IOException
import java.util.Timer
import java.util.TimerTask

import org.apache.log4j.Logger
import org.xvolks.dofus.events.TrafficEvent
import org.xvolks.dofus.events.TrafficListener

class TrafficLogger(internal val writer: BufferedWriter, var isActive: Boolean) : TrafficListener {
    internal var logger = Logger.getLogger(TrafficLogger::class.java)
    internal var cpt = 0

    init {
        val t = Timer("TrafficLogger Flusher", true)
        t.schedule(object : TimerTask() {
            override fun run() {
                synchronized(this@TrafficLogger.writer) {
                    try {
                        this@TrafficLogger.writer.flush()
                    } catch (e: IOException) {
                        logger.error("Timer flush failed !", e)
                    }

                }
            }
        }, 2000)
    }


    override fun dataReceived(event: TrafficEvent) {
        if (isActive) {
            var toLog = ""
            when (event.source) {
                TrafficEvent.Source.FROM_CLIENT -> toLog = ">"
                TrafficEvent.Source.FROM_SERVER -> toLog = "<"
            }
            val format = String.format("%d %s %s", event.timeStamp, toLog, event.data)
            synchronized(writer) {
                try {
                    logger.info(format)
                    writer.write(format)
                    writer.newLine()
                    if (++cpt % 32 == 0) {
                        cpt = 0
                        writer.flush()
                    }
                } catch (e: IOException) {
                    logger.error("Can't write $format to log file", e)
                }

            }
        }
    }

}
