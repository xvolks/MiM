package org.xvolks.dofus.gui

import java.util.ArrayList

import org.xvolks.dofus.events.GUIListener
import org.xvolks.dofus.injection.Plugin
import org.xvolks.dofus.injection.PluginManager

abstract class AbstractPlugin : Plugin {

    private val listeners = ArrayList<GUIListener>()
    lateinit var pluginManager: PluginManager
        private set

    protected val guiListeners: List<GUIListener>
        get() = listeners

    override val name: String
        get() = javaClass.getSimpleName()

    override fun addGUIListener(listener: GUIListener) {
        listeners.add(listener)
    }

    override fun proxyStarting(manager: PluginManager) {
        this.pluginManager = manager
    }

}
