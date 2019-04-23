package dofus.datacenter.characters

import dofus.datacenter.Title
import dofus.datacenter.Enums.EnumCreatureType
import dofus.datacenter.abstractCreatures.PlayableCharacter

class Cra : PlayableCharacter {

    constructor(creatureID: Int?, creatureName: String, gfxId: String, cellNum: Int, scaleX: Int, scaleY: Int, direction: Int, persoData: Array<String>, monsterTitle: Title) : super(creatureID, EnumCreatureType.CRA, creatureName, gfxId, cellNum, scaleX, scaleY, direction, persoData, monsterTitle) {}

    constructor(creatureID: Int?, creatureName: String, gfxId: String, cellNum: Int, scaleX: Int, scaleY: Int, direction: Int, sex: Boolean, color1: String, color2: String, color3: String, level: Int) : super(creatureID, EnumCreatureType.CRA, creatureName, gfxId, cellNum, scaleX, scaleY, direction, sex, color1, color2, color3, level) {}

}
