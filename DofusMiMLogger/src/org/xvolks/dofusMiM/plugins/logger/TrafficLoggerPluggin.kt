package org.xvolks.dofusMiM.plugins.logger

import java.awt.CheckboxMenuItem
import java.awt.MenuItem
import java.awt.event.ItemEvent
import java.awt.event.ItemListener
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

import org.apache.log4j.Logger
import org.xvolks.dofus.events.GUIListener
import org.xvolks.dofus.events.TrafficEvent
import org.xvolks.dofus.events.TrafficListener
import org.xvolks.dofus.events.TrafficEvent.Source
import org.xvolks.dofus.gui.AbstractPlugin
import org.xvolks.dofus.injection.Plugin
import org.xvolks.dofus.injection.PluginManager
import org.xvolks.dofus.util.JPopupWindow
import org.xvolks.dofus.util.Toolkit

class TrafficLoggerPluggin : AbstractPlugin() {

    internal var logger = Logger.getLogger(TrafficLoggerPluggin::class.java)
    private lateinit var trafficLogger: TrafficLogger
    private var writer: BufferedWriter? = null
    private var menuItem: CheckboxMenuItem? = null
    private var active = false

    override val trafficListener: TrafficListener
        get() = trafficLogger

    override fun proxyStarted(gameServer: Boolean) {

    }

    override fun proxyStarting(pluginManager: PluginManager) {
        super.proxyStarting(pluginManager)

        val fileName = File("traffic.trace.txt")
        renameIfExists(fileName)
        try {
            writer = BufferedWriter(FileWriter(fileName))
        } catch (e: IOException) {
            logger.error(e, e)
        }

        trafficLogger = TrafficLogger(writer!!, active)
    }

    private fun renameIfExists(fileName: File) {
        if (fileName.exists()) {
            val old = File(fileName.absolutePath + ".old")
            if (old.exists()) {
                renameIfExists(old)
            } else {
                fileName.renameTo(old)
            }
        }

    }

    override fun proxyStopped() {
        try {
            writer!!.flush()
            writer!!.close()
        } catch (e: IOException) {
            logger.error(e, e)
        }

    }

    override fun proxyReady() {
        for (listener in guiListeners) {
            listener.addTrayMenuItem(getMenuItem())
        }
    }

    private fun getMenuItem(): MenuItem {
        if (menuItem == null) {
            menuItem = CheckboxMenuItem("Log packets Actif")
            menuItem!!.addItemListener {
                active = !active
                menuItem!!.state = active
                JPopupWindow.showMessage("Log " + if (active) "activé" else "désactivé")
                trafficLogger!!.isActive = active
                synchronized(writer!!) {
                    try {
                        writer!!.flush()
                    } catch (e1: IOException) {
                        logger.error("Error while flushing writer by menu")
                    }

                }
            }
        }
        return menuItem!!
    }


    override fun toDofusCommunicationString(): String? {
        //no injection here
        return null
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = TrafficLoggerPluggin()
            `in`.proxyStarting(null!!)
            val l = `in`.trafficListener as TrafficLogger
            l.isActive = true
            l.dataReceived(TrafficEvent(Source.FROM_CLIENT, 0L, "test"))
            Toolkit.sleepSeconds(4, 5)
            `in`.proxyStopped()
        }
    }
}
