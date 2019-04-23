package dofus.datacenter.characters

import dofus.datacenter.Title
import dofus.datacenter.Enums.EnumCreatureType
import dofus.datacenter.abstractCreatures.PlayableCharacter

class Sadida : PlayableCharacter {

    constructor(creatureID: Int?, creatureName: String, gfxId: String, cellNum: Int, scaleX: Int, scaleY: Int, direction: Int, persoData: Array<String>, monsterTitle: Title) : super(creatureID, EnumCreatureType.SADIDA, creatureName, gfxId, cellNum, scaleX, scaleY, direction, persoData, monsterTitle) {}

    constructor(creatureID: Int, creatureName: String, gfxId: String, cellNum: Int, scaleX: Int, scaleY: Int, dir: Int, sex: Boolean, color1: String, color2: String, color3: String, level: Int) : super(creatureID, EnumCreatureType.SADIDA, creatureName, gfxId, cellNum, scaleX, scaleY, dir, sex, color1, color2, color3, level) {}


}
