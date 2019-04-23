package dofus.datacenter

import java.util.Vector

import org.apache.log4j.Logger
import org.xvolks.dofus.Main

import dofus.datacenter.abstractCreatures.AbstractCreature

class TaxCollector(creatureID: Int?, creatureName: String, gfxId: String, cellNum: Int, scaleX: Int, scaleY: Int, direction: Int, persoData: Array<String>) : AbstractCreature(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, direction) {

    private val logger = Logger.getLogger(TaxCollector::class.java)

    var level: String = persoData[7]
    lateinit var guildName: String
    lateinit var emblem: String

    init {
        if (Main.pluginManager.isFight) {
            lp = persoData[8]
            ap = persoData[9]
            mp = persoData[10]
            resistances = intArrayOf(Integer.parseInt(persoData[11]), Integer.parseInt(persoData[12]), Integer.parseInt(persoData[13]), Integer.parseInt(persoData[14]), Integer.parseInt(persoData[15]), Integer.parseInt(persoData[16]), Integer.parseInt(persoData[17]))
            team = persoData[18]
        } else {
            guildName = persoData[8]
            emblem = persoData[9]
        } // end else if
    }

}
