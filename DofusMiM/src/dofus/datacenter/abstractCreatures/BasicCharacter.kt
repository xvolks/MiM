package dofus.datacenter.abstractCreatures

abstract class BasicCharacter(creatureID: Int?, creatureName: String, gfxId: String, cellNum: Int, scaleX: Int, scaleY: Int, direction: Int, var sex: Boolean, color1: String, color2: String, color3: String) : ColoredCreature(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, direction, color1, color2, color3)
