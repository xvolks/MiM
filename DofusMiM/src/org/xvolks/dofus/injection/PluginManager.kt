package org.xvolks.dofus.injection

import java.io.IOException
import java.util.prefs.BackingStoreException

import org.ini4j.InvalidFileFormatException
import org.xvolks.dofus.events.GUIListener

interface PluginManager : Plugin {

    /**
     *
     * @return all loaded plugins names
     */
    val pluginNames: Set<String>

    var isFight: Boolean

    /**
     * Loads all plugins
     *
     * @param listener
     * @throws InvalidFileFormatException
     * @throws IOException
     * @throws BackingStoreException
     */
    @Throws(InvalidFileFormatException::class, IOException::class, BackingStoreException::class)
    fun loadPlugins(listener: GUIListener)

    /**
     *
     * @param name
     * @return the plugin instance of the named plugin
     */
    fun getPlugin(name: String): Plugin
}
