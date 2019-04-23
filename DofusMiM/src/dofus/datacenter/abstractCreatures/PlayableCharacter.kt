package dofus.datacenter.abstractCreatures

import java.util.Vector

import org.xvolks.dofus.Main
import org.xvolks.dofus.util.Toolkit

import dofus.datacenter.Alignment
import dofus.datacenter.Rank
import dofus.datacenter.Title
import dofus.datacenter.Enums.EnumCreatureType

abstract class PlayableCharacter(creatureID: Int?, val creatureType: EnumCreatureType, creatureName: String, gfxId: String, cellNum: Int, scaleX: Int, scaleY: Int, direction: Int, sex: Boolean, color1: String, color2: String, color3: String) : BasicCharacter(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, direction, sex, color1, color2, color3) {

    var level: Int = 0
    var aura: String? = null
    var emote: String? = null
    var emoteTimer: String? = null
    var guildName: String? = null
    var emblem: String? = null
    var restrictions: String? = null
    var alignment: Alignment? = null
    var rank: Rank? = null
    var title: Title? = null

    init {
        dataAsVector.add("" + creatureID!!)
        dataAsVector.add(creatureName)
        dataAsVector.add("" + level)
        dataAsVector.add(javaClass.getSimpleName())
        dataAsVector.add(alignment!!.toString())
        dataAsVector.add("" + rank!!.rank)

    }

    constructor(creatureID: Int?, creatureType: EnumCreatureType, creatureName: String, gfxId: String, cellNum: Int, scaleX: Int, scaleY: Int, direction: Int, sex: Boolean, color1: String, color2: String, color3: String, level: Int) : this(creatureID, creatureType, creatureName, gfxId, cellNum, scaleX, scaleY, direction, sex,
            color1,
            color2,
            color3
    ) {
        this.level = level

    }

    protected constructor(creatureID: Int?, creatureType: EnumCreatureType, creatureName: String, gfxId: String, cellNum: Int, scaleX: Int, scaleY: Int, direction: Int, persoData: Array<String>, monsterTitle: Title) : this(creatureID, creatureType, creatureName, gfxId, cellNum, scaleX, scaleY, direction, Toolkit.toBoolean(persoData[7]),
            if (Main.pluginManager.isFight) persoData[10] else persoData[9],
            if (Main.pluginManager.isFight) persoData[11] else persoData[10],
            if (Main.pluginManager.isFight) persoData[12] else persoData[11]
    ) {
        var _loc51: String? = null
        if (Main.pluginManager.isFight) { // En combat
            level = Integer.parseInt(persoData[8])
            _loc51 = persoData[9]
            accessories = persoData[13]
            lp = persoData[14]
            ap = persoData[15]
            mp = persoData[16]
            resistances = intArrayOf(Integer.parseInt(persoData[17]), Integer.parseInt(persoData[18]), Integer.parseInt(persoData[19]), Integer.parseInt(persoData[20]), Integer.parseInt(persoData[21]), Integer.parseInt(persoData[22]), Integer.parseInt(persoData[23]))
            team = persoData[24]
/* Gestion des couleurs et monture
            if (persoData[25].indexOf(",") != -1)
            {
                String[] _loc53 = persoData[25].split(",");
                int _loc54 = Integer.parseInt(_loc53[0]);
                int _loc55 = Integer.parseInt(_loc53[1], 16);
                int _loc56 = Integer.parseInt(_loc53[2], 16);
                int _loc57 = Integer.parseInt(_loc53[3], 16);
                if (_loc55 == -1 || _global.isNaN(_loc55))
                {
                    _loc55 = this.api.datacenter.Player.color1;
                } // end if
                if (_loc56 == -1 || _global.isNaN(_loc56))
                {
                    _loc56 = this.api.datacenter.Player.color2;
                } // end if
                if (_loc57 == -1 || _global.isNaN(_loc57))
                {
                    _loc57 = this.api.datacenter.Player.color3;
                } // end if
                if (!_global.isNaN(_loc54))
                {
                    var _loc58 = new dofus.datacenter.Mount(_loc54, Number(_loc21));
                    _loc58.customColor1 = _loc55;
                    _loc58.customColor2 = _loc56;
                    _loc58.customColor3 = _loc57;
                    _loc52.mount = _loc58;
                } // end if
            }
            else
            {
            	/* Gestion de la monture
                int _loc59 = Integer.parseInt(persoData[25]);
                if (!_global.isNaN(_loc59))
                {
                    _loc52.mount = new dofus.datacenter.Mount(_loc59, Number(_loc21));
                } // end if
            } // end else if
        */
        }
else
{ // Pas en combat
_loc51 = persoData[8]
accessories = persoData[12]
aura = persoData[13]
emote = persoData[14]
emoteTimer = persoData[15]
guildName = persoData[16]
emblem = persoData[17]
restrictions = persoData[18]
 /* Gestion couleur et monture
            if (persoData[19].indexOf(",") != -1)
            {
                var _loc60 = persoData[19].split(",");
                var _loc61 = Number(_loc60[0]);
                var _loc62 = _global.parseInt(_loc60[1], 16);
                var _loc63 = _global.parseInt(_loc60[2], 16);
                var _loc64 = _global.parseInt(_loc60[3], 16);
                if (_loc62 == -1 || _global.isNaN(_loc62))
                {
                    _loc62 = this.api.datacenter.Player.color1;
                } // end if
                if (_loc63 == -1 || _global.isNaN(_loc63))
                {
                    _loc63 = this.api.datacenter.Player.color2;
                } // end if
                if (_loc64 == -1 || _global.isNaN(_loc64))
                {
                    _loc64 = this.api.datacenter.Player.color3;
                } // end if
                if (!_global.isNaN(_loc61))
                {
                    var _loc65 = new dofus.datacenter.Mount(_loc61, Number(_loc21));
                    _loc65.customColor1 = _loc62;
                    _loc65.customColor2 = _loc63;
                    _loc65.customColor3 = _loc64;
                    _loc52.mount = _loc65;
                } // end if
            }
            else
            {
                var _loc66 = Number(persoData[19]);
                if (!_global.isNaN(_loc66))
                {
                    _loc52.mount = new dofus.datacenter.Mount(_loc66, Number(_loc21));
                } // end if
            } // end else if
            */
        } // end else if
 //        if (_loc7)
 //        {
 //            var _loc32 = [creatureID, this.createTransitionEffect(), _loc11, 10];
 //        } // end if
        val _loc67 = _loc51!!.split((",").toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
alignment = dofus.datacenter.Alignment(Integer.parseInt(_loc67[0]), Integer.parseInt(_loc67[1]))
rank = dofus.datacenter.Rank(Integer.parseInt(_loc67[2]))
if (_loc67.size > 4)
{
alignment!!.isFallenAngelDemon = "1" == _loc67[4]
}
if (_loc67.size > 3 /*&& creatureID != this.api.datacenter.Player.ID*/)
{
 //            if (this.api.lang.getAlignmentCanViewPvpGain(this.api.datacenter.Player.alignment.index, alignment.getIndex()))
            run({ val level = Integer.parseInt(_loc67[3]) - creatureID!!
this.level = level
 //                var _loc69 = this.api.lang.getConfigText("PVP_VIEW_BONUS_MINOR_LIMIT");
 //                var _loc70 = this.api.lang.getConfigText("PVP_VIEW_BONUS_MINOR_LIMIT_PRC");
 //                var _loc71 = this.api.lang.getConfigText("PVP_VIEW_BONUS_MAJOR_LIMIT");
 //                var _loc72 = this.api.lang.getConfigText("PVP_VIEW_BONUS_MAJOR_LIMIT_PRC");
                /* Gain par rapport au niveau des adversaires
                int _loc73 = 0;
                if (this.api.datacenter.Player.Level * (1 - _loc70 / 100) > _loc68)
                {
                    _loc73 = -1;
                } // end if
                if (this.api.datacenter.Player.Level - _loc68 > _loc69)
                {
                    _loc73 = -1;
                } // end if
                if (this.api.datacenter.Player.Level * (1 + _loc72 / 100) < _loc68)
                {
                    _loc73 = 1;
                } // end if
                if (this.api.datacenter.Player.Level - _loc68 < _loc71)
                {
                    _loc73 = 1;
                } // end if
                _loc52.pvpGain = _loc73;
                */
            }) // end if
} // end if
 /* Comparaison des level et rank
        if (!Main.getPluginManager().isFight() && (creatureID != this.api.datacenter.Player.ID && ((this.api.datacenter.Player.alignment.index == 1 || this.api.datacenter.Player.alignment.index == 2) && ((_loc52.alignment.index == 1 || _loc52.alignment.index == 2) && (_loc52.alignment.index != this.api.datacenter.Player.alignment.index && (_loc52.rank.value && this.api.datacenter.Map.bCanAttack))))))
        {
            if (this.api.datacenter.Player.rank.value > _loc52.rank.value)
            {
                this.api.kernel.SpeakingItemsManager.triggerEvent(dofus.managers.SpeakingItemsManager.SPEAK_TRIGGER_NEW_ENEMY_WEAK);
            } // end if
            if (this.api.datacenter.Player.rank.value < _loc52.rank.value)
            {
                this.api.kernel.SpeakingItemsManager.triggerEvent(dofus.managers.SpeakingItemsManager.SPEAK_TRIGGER_NEW_ENEMY_STRONG);
            } // end if
        } // end if
        */
 //        var _loc74 = this.sliptGfxData(_loc17);
 //        var _loc75 = _loc74.gfx;
 //        this.splitGfxForScale(_loc75[0], _loc52);
        title = monsterTitle
*/
        }
    }
}
