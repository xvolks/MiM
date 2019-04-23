package org.xvolks.dofus.util

import java.util.Timer
import java.util.TimerTask

import javax.swing.JLabel
import javax.swing.JWindow

class JPopupWindow private constructor() : JWindow() {
    internal var toDisplay = JLabel()

    init {
        contentPane.add(toDisplay)
    }

    private fun showMessage0(value: String, millisec: Int) {
        toDisplay.text = "<html><body style='background: white;'><h1 style='color: #BB5544;'>$value</body></html>"
        pack()
        Toolkit.centerOnScreen(this)
        isVisible = true
        val t = Timer("JPopupWindow", true)
        t.schedule(object : TimerTask() {
            override fun run() {
                this@JPopupWindow.isVisible = false
                this@JPopupWindow.dispose()
            }
        }, millisec.toLong())

    }

    companion object {

        private val serialVersionUID = 6591845036705725428L

        @JvmOverloads
        fun showMessage(value: String, millisec: Int = 2000) {
            JPopupWindow().showMessage0(value, millisec)
        }
    }
}
