package org.xvolks.dofus.gui

import java.awt.Component
import java.awt.MenuItem

import javax.swing.Icon
import javax.swing.JFrame
import javax.swing.JTabbedPane

import org.xvolks.dofus.Main
import org.xvolks.dofus.events.GUIListener

class DockableFrame(xvolksBot: String) : JFrame(xvolksBot), GUIListener {
    private var tabbedPane: JTabbedPane? = null

    init {
        initialize()
    }

    private fun initialize() {
        iconImage = Main.mainIcon
        defaultCloseOperation = JFrame.HIDE_ON_CLOSE
        contentPane.add(getTabbedPane())
        getTabbedPane().add(GeneralPanel(), "Général")
        pack()
    }

    fun getTabbedPane(): JTabbedPane {
        if (tabbedPane == null) {
            val tabbedPane = JTabbedPane()
            this.tabbedPane = tabbedPane
            return tabbedPane
        } else {
            return tabbedPane!!
        }
    }

    override fun addTabToMainFrame(component: Component, name: String, icon: Icon) {
        getTabbedPane().insertTab(name, icon, component, null, getTabbedPane().tabCount)
        pack()
    }

    override fun addTrayMenuItem(menuItem: MenuItem) {}

    override fun error(message: String) {}

    override fun error(message: String, title: String) {}

    override fun error(message: String, title: String, t: Throwable?) {}

    override fun information(message: String) {}

    override fun information(message: String, title: String) {}

    override fun warning(message: String) {}

    override fun warning(message: String, title: String) {}

    companion object {
        private val serialVersionUID = -8202249359002326732L

        @JvmStatic
        fun main(args: Array<String>) {
            DockableFrame(Main.XVOLKS_BOT).isVisible = true
        }
    }
}
