package dofus.datacenter.abstractCreatures

abstract class ColoredCreature(creatureID: Int?, creatureName: String, gfxId: String, cellNum: Int, scaleX: Int, scaleY: Int, direction: Int, var color1: String, var color2: String, var color3: String) : AbstractCreature(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, direction)
