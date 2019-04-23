package dofus.datacenter

import java.util.Vector

import dofus.datacenter.abstractCreatures.AbstractCreature


class Prism(creatureID: Int?, creatureName: String, gfxId: String, cellNum: Int, scaleX: Int, scaleY: Int, direction: Int, persoData: Array<String>) : AbstractCreature(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, direction) {
    var level: Int = 0
    var alignment: Alignment? = null

    init {
        level = Integer.parseInt(persoData[7])
        alignment = Alignment(Integer.parseInt(persoData[9]), Integer.parseInt(persoData[8]))
    }

}
