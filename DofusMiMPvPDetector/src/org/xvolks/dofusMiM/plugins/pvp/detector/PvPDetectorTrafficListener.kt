package org.xvolks.dofusMiM.plugins.pvp.detector

import java.io.IOException

import org.apache.log4j.Logger
import org.xvolks.dofus.events.TrafficEvent
import org.xvolks.dofus.events.TrafficListener
import org.xvolks.dofus.events.TrafficEvent.Source
import org.xvolks.dofusMiM.plugins.map.MapDecypherPluggin

import dofus.aks.ProtocolProcessor
import dofus.aks.network.IO
import dofus.aks.processor.listeners.GameActionsProcessorListener
import dofus.aks.processor.listeners.GameProcessorListener
import dofus.aks.processor.nullImpl.NullGameActionsProcessor
import dofus.aks.processor.nullImpl.NullGameProcessor
import dofus.aks.processor.nullImpl.NullProtocolProcessor
import dofus.datacenter.Creature
import dofus.datacenter.Monster
import dofus.datacenter.MonsterGroup
import dofus.datacenter.MutantCreature
import dofus.datacenter.MutantPlayer
import dofus.datacenter.NonPlayableCharacter
import dofus.datacenter.OffLineCharacter
import dofus.datacenter.ParkMount
import dofus.datacenter.Prism
import dofus.datacenter.TaxCollector
import dofus.datacenter.Title
import dofus.datacenter.Enums.EnumCreatureType
import dofus.datacenter.abstractCreatures.AbstractCreature
import dofus.datacenter.characters.Cra
import dofus.datacenter.characters.Ecaflip
import dofus.datacenter.characters.Eniripsa
import dofus.datacenter.characters.Enutrof
import dofus.datacenter.characters.Feca
import dofus.datacenter.characters.Iop
import dofus.datacenter.characters.Osamodas
import dofus.datacenter.characters.Pandawa
import dofus.datacenter.characters.Sadida
import dofus.datacenter.characters.Sram
import dofus.datacenter.characters.Xelor

class PvPDetectorTrafficListener(private val plugin: PvPDetectorPlugin) : TrafficListener {
    private val logger = Logger.getLogger(PvPDetectorTrafficListener::class.java)

    internal var processor = ProtocolProcessor(object : NullProtocolProcessor() {

        override val io: IO? = null

        override var gameActions: GameActionsProcessorListener = object : NullGameActionsProcessor() {
            override fun onActions(sExtraData: String) {}
        }

        //						String sKey = values[2];
        override var game: GameProcessorListener =
                object : NullGameProcessor() {
                    override fun onMapData(substring: String) {
                        val values = substring.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val sID = values[0]
                        val sDate = values[1]
                        val mapDecypherPluggin = plugin.pluginManager.getPlugin("MapDecypherPluggin") as MapDecypherPluggin
                        val mapInfos = mapDecypherPluggin.getMapInfos()
                        val zoneInfos = mapDecypherPluggin.getZoneInfos()
                        val mapId = Integer.parseInt(sID)
                        val info = mapInfos[mapId] ?: return
                        val zoneName: String =
                        try {
                            zoneInfos[info.zoneId]?.name ?: "inconnue"
                        } catch (e: NullPointerException) {
                            "inconnue"
                        }

                        plugin.panel.setTitle(String.format("Carte : n°%s du %s à %d, %d dans %s", sID, sDate, info.x, info.y, zoneName))
                        plugin.removeAllCreatures()
                        plugin.newMap(mapDecypherPluggin.currentMapData!!)
                    }

                    override fun onMovement(sExtraData: String) {
                        var values = sExtraData.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        if (values.isEmpty()) {
                            values = arrayOf(sExtraData)
                        }
                        parseOnMovementString(values)
                    }
                }

        @Throws(IOException::class)
        override fun onServerWillDisconnect() {
        }

        @Throws(IOException::class)
        override fun onServerMessage(substring: String) {
        }

        @Throws(IOException::class)
        override fun onHelloGameServer(substring: String) {
        }

        @Throws(IOException::class)
        override fun onHelloConnectionServer(substring: String) {
        }

        @Throws(IOException::class)
        override fun disconnect(b: Boolean, c: Boolean) {
        }
    })


    private fun parseOnMovementString(creaturesStrings: Array<String>) {
        if (creaturesStrings.size == 1) {
            logger.debug(creaturesStrings[0])
        }
        for (creatureString in creaturesStrings) {
            if (creatureString.length == 0) {
                continue
            }
            var _loc7 = false
            var _loc8 = false
            val prefix = creatureString[0]
            if (prefix == '+') {
                _loc8 = true
            } else if (prefix == '~') {
                _loc8 = true
                _loc7 = true
            } else if (prefix != '-') {
                continue
            }
            if (_loc8) {
                val persoData = creatureString.substring(1).split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val cellNum = Integer.parseInt(persoData[0])
                val dir = Integer.parseInt(persoData[1])
                val bonusValue = Integer.parseInt(persoData[2])
                val creatureID = Integer.parseInt(persoData[3])
                val creatureName = persoData[4]
                val _loc16 = persoData[5]
                var _loc17 = persoData[6]
                var noFlip = false
                var _loc19 = true
                if (_loc17.endsWith("*")) {
                    _loc17 = _loc17.substring(0, _loc17.length - 1)
                    noFlip = true
                } // end if
                if (_loc17.startsWith("*")) {
                    _loc19 = false
                    _loc17 = _loc17.substring(1)
                } // end if
                val _loc20 = _loc17.split("\\^".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val gfxId = if (_loc20.size == 2) _loc20[0] else _loc17
                //                int gfxId = Integer.parseInt(sGfxId);
                val _loc22 = _loc16.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val creatureType = EnumCreatureType.fromInt(Integer.parseInt(_loc22[0]))
                var monsterTitle: Title? = null
                if (_loc22.size > 1) {
                    val _loc24 = _loc22[1]
                    if (_loc24.length > 0) {
                        val _loc26 = _loc24.split("\\*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        monsterTitle = dofus.datacenter.Title(Integer.parseInt(_loc26[0]), _loc26[1])
                    } // end if
                }
                var scaleX = 100
                var scaleY = 100
                if (_loc20.size == 2) {
                    val _loc29 = _loc20[1]
                    try {
                        scaleY = Integer.parseInt(_loc29)
                        scaleX = Integer.parseInt(_loc29)
                    } catch (e: Exception) {
                        val _loc30 = _loc29.split("x".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        scaleX = if (_loc30.size == 2) Integer.parseInt(_loc30[0]) else 100
                        scaleY = if (_loc30.size == 2) Integer.parseInt(_loc30[1]) else 100
                    }

                } // end else if
                if (_loc7) {
                    // Probably useless to parse this case

                    //                    var _loc31 = api.datacenter.Sprites.getItemAt(_loc14);
                    //                    this.onSpriteMovement(false, _loc31);
                } // end if
                var currentCreature: AbstractCreature? = null
                when (creatureType) {
                    EnumCreatureType.CREATURE -> currentCreature = Creature(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, persoData, noFlip)
                    EnumCreatureType.MONSTER -> currentCreature = Monster(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, persoData, noFlip)
                    EnumCreatureType.MONSTER_GROUP -> {
                        val colors = persoData[8].split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val monsterGroup = MonsterGroup(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, persoData, noFlip, colors[0], colors[1], colors[2], bonusValue)
                        monsterGroup.accessories = persoData[9]
                        currentCreature = monsterGroup
                    }//                        var _loc36 = this.sliptGfxData(_loc17);
                    //                        var _loc37 = _loc36.gfx;
                    //                        this.splitGfxForScale(_loc37[0], _loc34);
                    /*
                        if (this.api.kernel.OptionsManager.getOption("ViewAllMonsterInGroup") == true)
                        {
                            var _loc38 = creatureID;
                            var _loc39 = 1;

                            while (++_loc39, _loc39 < _loc37.length)
                            {
                                if (_loc37[_loc5] == "")
                                {
                                    continue;
                                } // end if
                                this.splitGfxForScale(_loc37[_loc39], _loc34);
                                _loc35 = persoData[8 + 2 * _loc39].split(",");
                                _loc34.color1 = _loc35[0];
                                _loc34.color2 = _loc35[1];
                                _loc34.color3 = _loc35[2];
                                _loc34.dir = random(4) * 2 + 1;
                                _loc34.accessories = persoData[9 + 2 * _loc39];
                                var _loc40 = creatureID + "_" + _loc39;
                                var _loc41 = this.api.kernel.CharactersManager.createMonsterGroup(_loc40, undefined, _loc34);
                                var _loc42 = _loc38;
                                if (random(3) != 0 && _loc39 != 1)
                                {
                                    _loc42 = creatureID + "_" + (random(_loc39 - 1) + 1);
                                } // end if
                                var _loc43 = random(8);
                                this.api.gfx.addLinkedSprite(_loc40, _loc42, _loc43, _loc41);
                                if (!_global.isNaN(_loc41.scaleX))
                                {
                                    this.api.gfx.setSpriteScale(_loc41.id, _loc41.scaleX, _loc41.scaleY);
                                } // end if
                                switch (_loc36.shape)
                                {
                                    case "circle":
                                    {
                                        _loc43 = _loc39;
                                        break;
                                    }
                                    case "line":
                                    {
                                        _loc42 = _loc40;
                                        _loc43 = 2;
                                        break;
                                    }
                                } // End of switch
                            } // end while
                        } // end if
                        */
                    EnumCreatureType.NPC -> {
                        val npc = NonPlayableCharacter(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, persoData)
                        currentCreature = npc
                    }//                        _loc44.extraClipID = persoData[12] != undefined && !_global.isNaN(Number(persoData[12])) ? (Number(persoData[12])) : (-1);
                    //                        _loc44.customArtwork = Number(persoData[13]);
                    //                        _loc31 = this.api.kernel.CharactersManager.createNonPlayableCharacter(creatureID, Number(creatureName), _loc44);
                    EnumCreatureType.OFFLINE_CHARACTER -> {
                        val offLineCharacter = OffLineCharacter(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, persoData)
                        currentCreature = offLineCharacter
                    }
                    EnumCreatureType.TAX_COLLECTOR -> {
                        val perco = TaxCollector(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, persoData)
                        currentCreature = perco
                    }
                    EnumCreatureType.MUTANT_CREATURE, EnumCreatureType.MUTANT_PLAYER -> {
                        val mutant = if (creatureType === EnumCreatureType.MUTANT_CREATURE)
                            MutantCreature(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, persoData)
                        else
                            MutantPlayer(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, persoData)
                        currentCreature = mutant
                    }
                    EnumCreatureType.PARK_MOUNT -> {
                        val parkMount = ParkMount(creatureID, if (creatureName.trim { it <= ' ' }.length == 0) "Sans Nom" else creatureName, gfxId, cellNum, scaleX, scaleY, dir, persoData)
                        currentCreature = parkMount
                    }
                    EnumCreatureType.PRISM -> {
                        val prism = Prism(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, persoData)
                        currentCreature = prism
                    }
                    EnumCreatureType.FECA -> currentCreature = Feca(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, persoData, monsterTitle!!)
                    EnumCreatureType.OSAMODAS -> currentCreature = Osamodas(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, persoData, monsterTitle!!)
                    EnumCreatureType.ENUTROF -> currentCreature = Enutrof(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, persoData, monsterTitle!!)
                    EnumCreatureType.SRAM -> currentCreature = Sram(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, persoData, monsterTitle!!)
                    EnumCreatureType.XELOR -> currentCreature = Xelor(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, persoData, monsterTitle!!)
                    EnumCreatureType.ECAFLIP -> currentCreature = Ecaflip(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, persoData, monsterTitle!!)
                    EnumCreatureType.ENIRIPSA -> currentCreature = Eniripsa(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, persoData, monsterTitle!!)
                    EnumCreatureType.IOP -> currentCreature = Iop(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, persoData, monsterTitle!!)
                    EnumCreatureType.CRA -> currentCreature = Cra(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, persoData, monsterTitle!!)
                    EnumCreatureType.SADIDA -> currentCreature = Sadida(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, persoData, monsterTitle!!)
                    EnumCreatureType.PANDAWA -> currentCreature = Pandawa(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, persoData, monsterTitle!!)
                    else -> {
                    }
                } // End of switch
                //                this.onSpriteMovement(_loc8, _loc31, _loc32);
                if (currentCreature != null) {
                    plugin.addCreature(currentCreature)
                }
                continue
            } else {
                val creatureID = Integer.parseInt(creatureString.substring(1))
                plugin.removeCreature(creatureID)
            }
            //            var _loc82 = _loc6.substr(1);
            //            var _loc83 = this.api.datacenter.Sprites.getItemAt(_loc82);
            //            this.onSpriteMovement(_loc8, _loc83);
        } // end while

    }

    override fun dataReceived(event: TrafficEvent) {
        if (event.source === Source.FROM_SERVER) {
            try {
                processor.process(event.data)
            } catch (e: IOException) {
                logger.error(e, e)
            }

        }
    }

    companion object {
        private val CREATURE = -1
    }

}
