package org.xvolks.dofus.events

import org.apache.log4j.Logger


interface TrafficListener {

    /**
     * This method will receive all data from server and client. It gives a chance to the plugin to answer to a particular message
     * by adding TrafficEvents to the `event` object. This response is not guaranteed to be sent if 2 or more plugins
     * add TrafficEvents to the same `event` for the same direction (server or client).
     * @param event
     */
    fun dataReceived(event: TrafficEvent)

}

class DummyTrafficListener: TrafficListener {
    private val logger = Logger.getLogger(javaClass)
    private val caller = RuntimeException("debug information")

    override fun dataReceived(event: TrafficEvent) {
        logger.warn("usage of a dummy traffic listener", caller)
    }
}