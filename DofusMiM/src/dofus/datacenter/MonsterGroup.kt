package dofus.datacenter

import java.util.Vector

import dofus.datacenter.abstractCreatures.ColoredCreature

class MonsterGroup(creatureID: Int?, creatureName: String, gfxId: String, cellNum: Int, scaleX: Int, scaleY: Int, direction: Int, persoData: Array<String>, noFlip: Boolean, color1: String, color2: String, color3: String, var bonusValue: Int) : ColoredCreature(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, direction, color1, color2, color3) {

    var level: String? = null

}