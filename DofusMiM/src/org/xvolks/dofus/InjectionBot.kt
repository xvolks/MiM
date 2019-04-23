package org.xvolks.dofus

import org.apache.log4j.Logger
import org.xvolks.dofus.util.Toolkit

class InjectionBot(internal val communication: Communication) : Thread() {
    internal var logger = Logger.getLogger(InjectionBot::class.java!!)

    init {
        start()
    }

    override fun run() {
        while (communication.isRunning) {
            Toolkit.sleepSeconds(20, 50)
            try {
                for (pluginName in communication.pluginManager.pluginNames) {
                    communication.inject(communication.pluginManager.getPlugin(pluginName))
                }
            } catch (e: Exception) {
                logger.error(e, e)
            }

        }
    }

}
