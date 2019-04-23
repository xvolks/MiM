package dofus.datacenter

import java.util.Vector

import dofus.datacenter.abstractCreatures.AbstractCreature

class ParkMount(creatureID: Int?, creatureName: String, gfxId: String, cellNum: Int, scaleX: Int, scaleY: Int, direction: Int, persoData: Array<String>) : AbstractCreature(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, direction) {

    var ownerName: String? = null
    var level: String? = null
    var modelID: String? = null

    init {
        ownerName = persoData[7]
        level = persoData[8]
        modelID = persoData[9]
    }
}
