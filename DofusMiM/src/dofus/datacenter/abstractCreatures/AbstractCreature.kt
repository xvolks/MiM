package dofus.datacenter.abstractCreatures

import java.util.Vector

import dofus.datacenter.Enums.EnumCreatureType


abstract class AbstractCreature(val creatureID: Int?, val creatureName: String, var gfxID: String, var cell: Int, var scaleX: Int, var scaleY: Int, var dir: Int) {

    lateinit var spriteType: EnumCreatureType
    lateinit var accessories: String
    lateinit var lp: String
    lateinit var ap: String
    lateinit var mp: String
    lateinit var resistances: IntArray
    lateinit var team: String
    var isSummoned: Boolean = false

    val dataAsVector = Vector<String>()


}
