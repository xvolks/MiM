package org.xvolks.dofus.injection.impl

import java.io.File
import java.io.IOException
import java.util.HashMap
import java.util.TreeMap
import java.util.prefs.BackingStoreException

import org.apache.log4j.Logger
import org.ini4j.Ini
import org.ini4j.IniPreferences
import org.ini4j.InvalidFileFormatException
import org.xvolks.dofus.events.GUIListener
import org.xvolks.dofus.events.TrafficListener
import org.xvolks.dofus.injection.Plugin
import org.xvolks.dofus.injection.PluginManager

import dofus.aks.ProtocolProcessor
import dofus.datacenter.abstractCreatures.PlayableCharacter
import org.xvolks.dofus.events.DummyTrafficListener
import org.xvolks.dofus.events.TrafficEvent

object PluginManagerImpl : PluginManager {

    private val pluginsByName = TreeMap<String, Plugin>()
    private val plugins = HashMap<Plugin, TrafficListener?>()
    private val logger = Logger.getLogger(PluginManagerImpl::class.java)
    private val processor = ProtocolProcessor(PluginManagerProtocolListenerImpl(this))
    override var isFight: Boolean = false
        set(fight) {
            field = fight
            logger.warn("Fight state changed to $fight")
        }
    var mySelf: PlayableCharacter? = null

    override val pluginNames: Set<String>
        get() = pluginsByName.keys


    override val trafficListener: TrafficListener
        get() {
            for (plugin in plugins.keys) {
                plugins[plugin] = plugin.trafficListener
            }
            return object:TrafficListener {
                override fun dataReceived(event: TrafficEvent) {
                    try {
                        processor.process(event.data)
                    } catch (e: IOException) {
                        logger.error(e, e)
                    }
                }
            }
        }


    override val name: String
        get() = throw IllegalStateException("This method cannot be called on PluginManager instance")

    @Throws(InvalidFileFormatException::class, IOException::class, BackingStoreException::class)
    override fun loadPlugins(listener: GUIListener) {
        listener.information("Chargement des plugins")
        logger.debug("Plugin loading started")
        val ini = Ini(File("xvolks.plugins.ini"))
        val iniPrefs = IniPreferences(ini)
        for (name in iniPrefs.childrenNames()) {
            logger.debug("Plugin declared : $name")
            val prefs = iniPrefs.node(name)
            val clazz = prefs.get("class", null)
            if (clazz == null) {
                logger.warn("Plugin$name is misconfigured : missing class entry")
                continue
            } else {
                try {
                    val c = Class.forName(clazz) as Class<Plugin>
                    if (Plugin::class.java.isAssignableFrom(c)) {
                        try {
                            val plugin = c.newInstance()
                            plugins[plugin] = DummyTrafficListener()
                            pluginsByName[plugin.name] = plugin
                            logger.info("Plugin " + name + " loaded with name " + plugin.name)
                        } catch (e: Exception) {
                            logger.error("Plugin $name cannot be instanciated", e)
                        }

                    } else {
                        logger.warn("Plugin$name does not honnor Plugin interface")
                        continue
                    }
                } catch (e: ClassNotFoundException) {
                    logger.warn("Plugin$name was not found : missing class $clazz")
                    continue
                }

            }

        }
    }


    override fun getPlugin(name: String): Plugin {
        return pluginsByName[name]!!
    }


    override fun addGUIListener(listener: GUIListener) {
        for (plugin in plugins.keys) {
            plugin.addGUIListener(listener)
        }
    }


    override fun proxyReady() {
        for (plugin in plugins.keys) {
            plugin.proxyReady()
        }
    }


    override fun proxyStarted(gameServer: Boolean) {
        for (plugin in plugins.keys) {
            plugin.proxyStarted(gameServer)
        }
    }


    override fun proxyStarting(manager: PluginManager) {
        for (plugin in plugins.keys) {
            plugin.proxyStarting(this)
        }
    }


    override fun proxyStopped() {
        for (plugin in plugins.keys) {
            plugin.proxyStopped()
        }
    }


    override fun toDofusCommunicationString(): String {
        return ""
    }


}
