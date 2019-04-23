package org.xvolks.dofus.events

import java.awt.Component
import java.awt.MenuItem
import java.awt.TrayIcon
import java.awt.TrayIcon.MessageType
import java.awt.event.ActionListener

import javax.swing.Icon

import org.apache.log4j.Logger

class TrayListener(private val tray: TrayIcon) : GUIListener {

    override fun error(message: String) {
        error(message, "Erreur")
    }

    override fun error(message: String, title: String) {
        error(message, title, null)
    }

    override fun error(message: String, title: String, t: Throwable?) {
        var message = message
        if (t != null) {
            message += "\nCause : $t"
        }
        tray.displayMessage(title, message, MessageType.ERROR)
    }

    override fun information(message: String) {
        information(message, "Info...")
    }

    override fun information(message: String, title: String) {
        tray.displayMessage(title, message, MessageType.INFO)
    }

    override fun warning(message: String) {
        warning(message, "Attention")
    }

    override fun warning(message: String, title: String) {
        tray.displayMessage(title, message, MessageType.WARNING)
    }

    override fun addTrayMenuItem(menuItem: MenuItem) {
        tray.popupMenu.add(menuItem)
        for (l in tray.popupMenu.actionListeners) {
            Logger.getLogger(TrayListener::class.java!!).info(l.javaClass)
        }
    }

    override fun addTabToMainFrame(component: Component, name: String, icon: Icon) {
        //Cannot do this here
    }

}
