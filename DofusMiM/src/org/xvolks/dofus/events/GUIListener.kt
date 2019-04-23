package org.xvolks.dofus.events

import java.awt.Component
import java.awt.MenuItem

import javax.swing.Icon

/**
 * Interface for logging events to the GUI
 * @author xvolks
 */
interface GUIListener {
    fun information(message: String)
    fun information(message: String, title: String)
    fun warning(message: String)
    fun warning(message: String, title: String)
    fun error(message: String)
    fun error(message: String, title: String)
    fun error(message: String, title: String, t: Throwable?)
    /**
     * Adds a menu specific to the plugin into the proxy tray menu
     * @param menuItem
     */
    fun addTrayMenuItem(menuItem: MenuItem)

    /**
     * Adds a tab panel to the main window
     * @param component
     * @param name
     * @param icon
     */
    fun addTabToMainFrame(component: Component, name: String, icon: Icon)
}
