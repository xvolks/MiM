package org.xvolks.dofusMiM.plugins.pvp.detector

import java.util.Vector

import javax.swing.JScrollPane

import org.xvolks.dofus.Main
import org.xvolks.dofus.events.GUIListener
import org.xvolks.dofus.events.TrafficListener
import org.xvolks.dofus.gui.AbstractPlugin
import org.xvolks.dofusMiM.plugins.map.render.MapRendererModel
import org.xvolks.dofusMiM.plugins.map.render.MapRendererPlugin

import ank.battlefield.MapData

import dofus.datacenter.abstractCreatures.AbstractCreature

class PvPDetectorPlugin : AbstractPlugin() {

    var panel: PvPDetectorPanel = PvPDetectorPanel()
    private val pvPDetectorTrafficListener = PvPDetectorTrafficListener(this)

    override val trafficListener: TrafficListener
        get() = pvPDetectorTrafficListener

    private val mapRendererModel: MapRendererModel
        get() {
            val plugin = Main.pluginManager.getPlugin("MapRendererPlugin") as MapRendererPlugin
            return plugin.panel.model
        }

    override fun proxyStarted(gameServer: Boolean) {}

    override fun proxyReady() {
        for (listener in guiListeners) {
            listener.addTabToMainFrame(JScrollPane(panel), "PvP", null!!)
        }
    }

    override fun proxyStopped() {}

    override fun toDofusCommunicationString(): String? {
        // TODO Auto-generated method stub
        return null
    }

    fun addCreature(currentCreature: AbstractCreature) {
        val mapRendererModel = mapRendererModel
        if (mapRendererModel != null) {
            mapRendererModel.creatures.add(currentCreature)
            mapRendererModel.fireModelUpdated()
        }
        val rowData = currentCreature.dataAsVector
        if (rowData != null && !rowData.isEmpty()) {
            panel.model.addRow(rowData)
        }
    }

    fun removeCreature(creatureID: Int?) {
        for (i in 0 until panel.model.rowCount) {
            if (panel.model.getValueAt(i, 0) == creatureID!!.toString()) {
                panel.model.removeRow(i)
                panel.model.fireTableRowsDeleted(i, i)
                break
            }
        }
        val mapRendererModel = mapRendererModel
        if (mapRendererModel != null) {
            var target: AbstractCreature? = null
            for (creature in mapRendererModel.creatures) {
                if (creature.creatureID === creatureID) {
                    target = creature
                    break
                }
            }
            if (target != null) {
                mapRendererModel.creatures.remove(target)
            }
            mapRendererModel.fireModelUpdated()
        }
    }

    fun removeAllCreatures() {
        val mapRendererModel = mapRendererModel
        if (mapRendererModel != null) {
            mapRendererModel.creatures.clear()
            mapRendererModel.fireModelUpdated()
        }
        panel.model.dataVector.clear()
        panel.model.fireTableStructureChanged()
    }

    fun newMap(currentMapData: MapData) {
        val mapRendererModel = mapRendererModel
        if (mapRendererModel != null) {
            mapRendererModel.data = currentMapData
            mapRendererModel.fireModelUpdated()
        }
    }
}
