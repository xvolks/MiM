package org.xvolks.dofusMiM.plugins.map.render

import javax.swing.JScrollPane

import org.xvolks.dofus.events.GUIListener
import org.xvolks.dofus.events.TrafficListener
import org.xvolks.dofus.gui.AbstractPlugin


class MapRendererPlugin : AbstractPlugin() {

    var panel: MapRendererPanel = MapRendererPanel(MapRendererModel())

    override val trafficListener: TrafficListener? = null

    override fun proxyReady() {
        for (listener in guiListeners) {
            listener.addTabToMainFrame(JScrollPane(panel), "MapRenderer", null!!)
        }
    }

    override fun proxyStarted(gameServer: Boolean) {
        // TODO Auto-generated method stub

    }

    override fun proxyStopped() {
        // TODO Auto-generated method stub

    }

    override fun toDofusCommunicationString(): String? {
        // TODO Auto-generated method stub
        return null
    }

}
