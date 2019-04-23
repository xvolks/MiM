package dofus.datacenter

import java.util.Vector

import org.apache.log4j.Logger

import dofus.datacenter.abstractCreatures.FoeCreature

open class Creature(creatureID: Int?, creatureName: String, gfxId: String, cellNum: Int, scaleX: Int, scaleY: Int, direction: Int, persoData: Array<String>, noFlip: Boolean) : FoeCreature(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, direction, noFlip, persoData[7], persoData[8], persoData[9], persoData[10]) {
    private val logger = Logger.getLogger(Creature::class.java!!)

    init {

        accessories = persoData[11]
        try
        //if (Game.isFight)
        {
            lp = persoData[12]
            ap = persoData[13]
            mp = persoData[14]
            if (persoData.size > 18) {
                resistances = intArrayOf(Integer.parseInt(persoData[15]), Integer.parseInt(persoData[16]), Integer.parseInt(persoData[17]), Integer.parseInt(persoData[18]), Integer.parseInt(persoData[19]), Integer.parseInt(persoData[20]), Integer.parseInt(persoData[21]))
                team = persoData[22]
            } else {
                team = persoData[15]
            } // end else if
            isSummoned = false//bIsSummoned;
        } catch (e: Exception) {
            //Normalement pas en combant
            logger.warn("BUG CHECK parsing Creature ou Monster : Vérifiez  : si pas en combat pas de probleme, sinon erreur de parsing")
        }

    }
}
