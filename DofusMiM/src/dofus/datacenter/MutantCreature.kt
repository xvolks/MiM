package dofus.datacenter

import java.util.Vector

import dofus.datacenter.abstractCreatures.Mutant

class MutantCreature(creatureID: Int?, creatureName: String, gfxId: String, cellNum: Int, scaleX: Int, scaleY: Int, direction: Int, persoData: Array<String>) : Mutant(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, direction, persoData) {

    init {
        isShowIsPlayer = false
        monsterID = creatureName
    }

}
