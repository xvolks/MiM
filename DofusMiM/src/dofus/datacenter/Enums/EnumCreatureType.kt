package dofus.datacenter.Enums

import org.apache.log4j.Logger

enum class EnumCreatureType private constructor(val value: Int) {
    FECA(1),
    OSAMODAS(2),
    ENUTROF(3),
    SRAM(4),
    XELOR(5),
    ECAFLIP(6),
    ENIRIPSA(7),
    IOP(8),
    CRA(9),
    SADIDA(10),
    SACRIEUR(11),
    PANDAWA(12),

    CREATURE(-1),
    MONSTER(-2),
    MONSTER_GROUP(-3),
    NPC(-4),
    OFFLINE_CHARACTER(-5),
    TAX_COLLECTOR(-6),
    MUTANT_CREATURE(-7),
    MUTANT_PLAYER(-8),
    PARK_MOUNT(-9),
    PRISM(-10),


    OTHER(0);


    companion object {
        private val logger = Logger.getLogger(EnumCreatureType::class.java!!)

        fun fromInt(parseInt: Int): EnumCreatureType {
            for (t in EnumCreatureType.values()) {
                if (t.value == parseInt) {
                    return t
                }
            }
            logger.warn(String.format("Unknown creature type : %d, returning OTHER(0)", parseInt))
            return OTHER
        }
    }


}
