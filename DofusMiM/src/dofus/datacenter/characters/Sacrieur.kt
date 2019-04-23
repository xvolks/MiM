package dofus.datacenter.characters

import dofus.datacenter.Title
import dofus.datacenter.Enums.EnumCreatureType
import dofus.datacenter.abstractCreatures.PlayableCharacter

class Sacrieur(creatureID: Int?, creatureName: String, gfxId: String, cellNum: Int, scaleX: Int, scaleY: Int, direction: Int, persoData: Array<String>, monsterTitle: Title) : PlayableCharacter(creatureID, EnumCreatureType.SACRIEUR, creatureName, gfxId, cellNum, scaleX, scaleY, direction, persoData, monsterTitle)
