package dofus.datacenter.abstractCreatures

abstract class FoeCreature(creatureID: Int?, creatureName: String, gfxId: String, cellNum: Int, scaleX: Int, scaleY: Int, direction: Int, var isNoFlip: Boolean, powerLevel: String, color1: String, color2: String, color3: String) : ColoredCreature(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, direction, color1, color2, color3) {
    var powerLevel: String? = null

    init {
        this.powerLevel = powerLevel
    }


}
