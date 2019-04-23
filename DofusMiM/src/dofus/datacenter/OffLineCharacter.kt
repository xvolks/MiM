package dofus.datacenter

import java.util.Vector

import dofus.datacenter.abstractCreatures.ColoredCreature

class OffLineCharacter(creatureID: Int?, creatureName: String, gfxId: String, cellNum: Int, scaleX: Int, scaleY: Int, direction: Int, persoData: Array<String>) : ColoredCreature(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, direction, persoData[7], persoData[8], persoData[9]) {

    var guildName: String
    var emblem: String
    var offlineType: String

    init {
        accessories = persoData[10]
        guildName = persoData[11]
        emblem = persoData[12]
        offlineType = persoData[13]
    }
}
