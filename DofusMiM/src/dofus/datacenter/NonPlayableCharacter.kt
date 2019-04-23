package dofus.datacenter

import java.util.Vector

import org.xvolks.dofus.util.Toolkit

import dofus.datacenter.abstractCreatures.BasicCharacter

class NonPlayableCharacter(creatureID: Int?, creatureName: String, gfxId: String, cellNum: Int, scaleX: Int, scaleY: Int, direction: Int, persoData: Array<String>) : BasicCharacter(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, direction, Toolkit.toBoolean(persoData[7]), persoData[8], persoData[9], persoData[10]) {

    init {
        accessories = persoData[11]
    }

}
