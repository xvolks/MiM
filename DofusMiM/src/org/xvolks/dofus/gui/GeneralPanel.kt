package org.xvolks.dofus.gui

import java.awt.BorderLayout

import javax.swing.JLabel
import javax.swing.JPanel

import org.xvolks.dofus.Main

class GeneralPanel : JPanel() {
    init {
        layout = BorderLayout()
        add(JLabel(Main.XVOLKS_BOT), BorderLayout.NORTH)
    }

    companion object {
        private val serialVersionUID = -4033087361363851862L
    }

}
