package org.xvolks.dofus.injection.impl

import java.io.IOException

import org.xvolks.dofus.util.Toolkit

import dofus.aks.network.IO
import dofus.aks.processor.listeners.AccountProcessorListener
import dofus.aks.processor.listeners.GameProcessorListener
import dofus.aks.processor.nullImpl.NullAccountProcessor
import dofus.aks.processor.nullImpl.NullGameProcessor
import dofus.aks.processor.nullImpl.NullProtocolProcessor
import dofus.datacenter.Enums.EnumCreatureType
import dofus.datacenter.abstractCreatures.PlayableCharacter
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

class PluginManagerProtocolListenerImpl(internal val pluginManager: PluginManagerImpl) : NullProtocolProcessor() {

    override var io: IO? = null

    override var game: GameProcessorListener = object : NullGameProcessor() {
            override fun onCreate(b: Boolean, substring: String) {
                pluginManager.isFight = true
            }

            override fun onJoin(substring: String) {
                pluginManager.isFight = true
            }

            override fun onEnd(substring: String) {
                pluginManager.isFight = false
            }
        }

    //		            perso.setGuild(_loc4[3]);
    //		            perso.setItems(_loc4[9]);
    // End of switch
    override var account: AccountProcessorListener = object : NullAccountProcessor() {
            @Throws(IOException::class)
            override fun onCharacterSelected(b: Boolean, sExtraData: String) {
                if (b) {
                    val _loc4 = sExtraData.split("\\|".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                    val creatureID = Integer.parseInt(_loc4[0])
                    val creatureName = _loc4[1]

                    val type = Integer.parseInt(_loc4[3])
                    var mySelf: PlayableCharacter? = null
                    val level = Integer.parseInt(_loc4[2])
                    val sex = Toolkit.toBoolean(_loc4[4])
                    val gfxId = _loc4[5]
                    val cellNum = -1
                    val scaleX = 1
                    val scaleY = 1
                    val dir = 0
                    val color1 = _loc4[6]
                    val color2 = _loc4[7]
                    val color3 = _loc4[8]

                    when (EnumCreatureType.fromInt(type)) {
                        EnumCreatureType.FECA -> mySelf = Feca(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, sex, color1, color2, color3, level)
                        EnumCreatureType.OSAMODAS -> mySelf = Osamodas(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, sex, color1, color2, color3, level)
                        EnumCreatureType.ENUTROF -> mySelf = Enutrof(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, sex, color1, color2, color3, level)
                        EnumCreatureType.SRAM -> mySelf = Sram(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, sex, color1, color2, color3, level)
                        EnumCreatureType.XELOR -> mySelf = Xelor(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, sex, color1, color2, color3, level)
                        EnumCreatureType.ECAFLIP -> mySelf = Ecaflip(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, sex, color1, color2, color3, level)
                        EnumCreatureType.ENIRIPSA -> mySelf = Eniripsa(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, sex, color1, color2, color3, level)
                        EnumCreatureType.IOP -> mySelf = Iop(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, sex, color1, color2, color3, level)
                        EnumCreatureType.CRA -> mySelf = Cra(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, sex, color1, color2, color3, level)
                        EnumCreatureType.SADIDA -> mySelf = Sadida(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, sex, color1, color2, color3, level)
                        EnumCreatureType.PANDAWA -> mySelf = Pandawa(creatureID, creatureName, gfxId, cellNum, scaleX, scaleY, dir, sex, color1, color2, color3, level)
                        else -> {
                        }
                    }


                    pluginManager.mySelf = mySelf
                }
            }
        }

    @Throws(IOException::class)
    override fun disconnect(b: Boolean, c: Boolean) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onHelloConnectionServer(substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onHelloGameServer(substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onServerMessage(substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onServerWillDisconnect() {
        // TODO Auto-generated method stub

    }

}
