package dofus.datacenter

import java.util.Vector

import dofus.datacenter.abstractCreatures.Mutant

class MutantPlayer(creatureID: Int?, creatureName: String, gfxId: String, cellNum: Int, scaleX: Int, scaleY: Int, direction: Int, persoData: Array<String>) : Mutant(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, direction, persoData) {


    var playerName: String

    init {
        isShowIsPlayer = true
        val _loc48 = creatureName.split("~".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        monsterID = _loc48[0]
        playerName = _loc48[1]

        dataAsVector.add(playerName)
        dataAsVector.add("" + powerLevel)
        dataAsVector.add("Mutant")
        dataAsVector.add("inconnu")
    }

}
