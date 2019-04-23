package org.xvolks.dofusMiM.plugins.map.render

import java.util.ArrayList
import java.util.Vector

import org.xvolks.dofus.mvc.Model

import ank.battlefield.MapData

import dofus.datacenter.abstractCreatures.AbstractCreature

class MapRendererModel : Model<MapRendererPanel> {
    var data: MapData? = null
        set(data) {
            field = data
            fireModelUpdated()
        }
    private val listeners = ArrayList<MapRendererPanel>()
    val creatures: MutableList<AbstractCreature> = Vector()

    override fun addModelListener(listener: MapRendererPanel) {
        listeners.add(listener)
    }

    override fun removeModelListener(listener: MapRendererPanel): Boolean {
        return listeners.remove(listener)
    }

    override fun fireModelUpdated() {
        for (listener in listeners) {
            listener.modelUpdated()
        }
    }

}
