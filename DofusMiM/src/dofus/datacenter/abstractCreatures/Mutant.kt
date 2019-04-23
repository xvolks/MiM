package dofus.datacenter.abstractCreatures

import org.apache.log4j.Logger

abstract class Mutant(creatureID: Int?, creatureName: String, gfxId: String, cellNum: Int, scaleX: Int, scaleY: Int, direction: Int, persoData: Array<String>) : AbstractCreature(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, direction) {

    var logger = Logger.getLogger(Mutant::class.java)
    var sex: String
    lateinit var emote: String
    lateinit var emoteTimer: String
    var restrictions: Int = 0
    var isShowIsPlayer: Boolean = false
    lateinit var monsterID: String
    var powerLevel: Int = 0

    init {
        sex = persoData[7]
        powerLevel = Integer.parseInt(persoData[8])
        accessories = persoData[9]
        try
        //Game.isFight
        {
            lp = persoData[10]
            ap = persoData[11]
            mp = persoData[12]
            team = persoData[20]
        } catch (e: Exception) {
            logger.warn("BUG CHECK Parsing Mutant: Vérifiez : si pas en combat, pas de probleme, sinon erreur de parsing")
            emote = persoData[10]
            emoteTimer = persoData[11]
            restrictions = Integer.parseInt(persoData[12])
        }
        // end else if
    }


}
