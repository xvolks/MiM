package org.xvolks.dofus.injection.impl.flood

import java.awt.CheckboxMenuItem
import java.awt.Component
import java.awt.MenuItem
import java.io.BufferedReader
import java.io.File
import java.io.FileFilter
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.LinkedList

import javax.swing.Icon

import org.apache.log4j.Logger
import org.xvolks.dofus.events.GUIListener
import org.xvolks.dofus.events.TrafficEvent
import org.xvolks.dofus.events.TrafficListener
import org.xvolks.dofus.events.TrafficEvent.Source
import org.xvolks.dofus.injection.Plugin
import org.xvolks.dofus.injection.PluginManager
import org.xvolks.dofus.util.JPopupWindow
import org.xvolks.dofus.util.Toolkit
import org.xvolks.dofus.util.apache.commons.lang.StringEscapeUtils

import dofus.aks.ProtocolProcessor
import org.xvolks.dofus.injection.impl.PluginManagerImpl


class FortuneFlood : AbstractFlood(), Plugin, TrafficListener {
    private val fortune = LinkedList<String>()
    var menuItem: CheckboxMenuItem? = null
    private val processor = FortuneProtocolProcessor(this)
    private val protocolProcessor = ProtocolProcessor(processor)

    override val trafficListener: TrafficListener
        get() = this

    override val message: String
        get() {
            if (fortune.size == 0) {
                return "Base fortune vide"
            }
            val citation = fortune[Math.round(Math.random() * (fortune.size - 1)).toInt()]
            // Try an other message
            if (citation.length > 255) return message
            for (listener in guiListeners) {
                listener.information(citation, "Flood")
            }
            return StringEscapeUtils.escapeHtml(citation) ?: "Aïe!"
        }

    override fun proxyReady() {
        for (listener in guiListeners) {
            listener.addTrayMenuItem(getMenuItem())
        }
    }

    internal fun getMenuItem(): CheckboxMenuItem {
        if (menuItem == null) {
            menuItem = CheckboxMenuItem("Food Actif", isFloodAllowed)
            menuItem!!.addItemListener {
                isFloodAllowed = !isFloodAllowed
                menuItem!!.state = isFloodAllowed
                JPopupWindow.showMessage("Flood " + if (isFloodAllowed) "activé" else "désactivé")
            }
        }
        return menuItem!!
    }

    override fun proxyStarting(manager: PluginManager) {
        super.proxyStarting(manager)
        val fortuneDir = File("fortune")
        Toolkit.listDir(fortuneDir, FileFilter { pathname ->
            if (!pathname.isDirectory) {
                readFile(pathname)
            }
            true
        })
    }


    private fun readFile(f: File) {
        var br: BufferedReader? = null
        var `in`: InputStream? = null
        try {
            `in` = FileInputStream(f)
            br = BufferedReader(InputStreamReader(`in`, "ISO8859-15"))

            var citation = ""
            while (true) {
                val line = br.readLine() ?: break
                if (line.trim { it <= ' ' } == "%") {
                    //Fin de citation
                    val substring = citation.substring(0, citation.length - 1)
                    fortune.add(substring)
                    citation = ""
                } else {
                    citation += line
                    citation += " "
                }
            }
        } catch (e: IOException) {
            logger.error(e, e)
        } finally {
            try {
                `in`?.close()
            } catch (e: IOException) {
                logger.error(e, e)
            }

            try {
                br?.close()
            } catch (e: IOException) {
                logger.error(e, e)
            }

        }
    }


    override fun dataReceived(event: TrafficEvent) {
        if (event.source === Source.FROM_SERVER) {
            //Déactive le flood en combat
            try {
                protocolProcessor.process(event.data)
            } catch (e: IOException) {
                logger.error(e, e)
            }

        }
    }

    companion object {

        private val logger = Logger.getLogger(FortuneFlood::class.java)


        @JvmStatic
        fun main(args: Array<String>) {
            val ff = FortuneFlood()
            ff.proxyStarting(PluginManagerImpl)
            ff.addGUIListener(object : GUIListener {
                override fun warning(message: String, title: String) {}
                override fun warning(message: String) {}
                override fun information(message: String, title: String) {}
                override fun information(message: String) {}
                override fun error(message: String, title: String, t: Throwable?) {}
                override fun error(message: String, title: String) {}
                override fun error(message: String) {}
                override fun addTrayMenuItem(menuItem: MenuItem) {}
                override fun addTabToMainFrame(component: Component, name: String, icon: Icon) {}
            })
            for (i in 0..9999) {
                System.err.println(ff.message)
            }
        }
    }
}
