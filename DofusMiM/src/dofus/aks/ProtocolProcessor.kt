package dofus.aks

import java.io.IOException

import org.apache.log4j.Logger

import dofus.aks.processor.listeners.ProcessorListener

class ProtocolProcessor(private val aks: ProcessorListener/* , int oAPI */) {
    private val logger = Logger.getLogger(ProtocolProcessor::class.java!!)

    /**
     * Pré-traitement de la ligne reçue du serveur
     * @param sData : la ligne reçue
     * @throws IOException si une exception d'entrée/sortie à lieu (coupure réseau par exemple)
     */
    @Throws(IOException::class)
    fun process(sData: String) {
        val type = sData[0]
        val action = sData[1]
        var hasError = false
        try {
            hasError = sData[2] == 'E'
        } catch (e: StringIndexOutOfBoundsException) {
        }

        this.postProcess(type, action, hasError, sData)
    }

    /**
     * Traitement de la ligne reçue du serveur
     * @param sType : Type de message
     * @param sAction : Sous type ou action
     * @param bError : si vrai ; le message contient une erreur.
     * @param sData ligne reçue du serveur dans son intégralité (sauf le NULL terminateur)
     * @throws IOException si une exception d'entrée/sortie à lieu (coupure réseau par exemple)
     */
    @Throws(IOException::class)
    private fun postProcess(sType: Char, sAction: Char, bError: Boolean, sData: String) {
        when (sType) {
            'H' -> {
                when (sAction) {
                    'C' -> {
                        this.aks.onHelloConnectionServer(sData.substring(2))
                    }
                    'G' -> {
                        this.aks.onHelloGameServer(sData.substring(2))
                    }
                    else -> {
                        this.aks.disconnect(false, true)
                    }
                } // End of switch
            }
            'p' -> {
                this.aks.onPong()
            }
            'q' -> {
                this.aks.onQuickPong()
            }
            'r' -> {
                this.aks.onResponsePong(sData.substring(5))
            }
            'M' -> {
                this.aks.onServerMessage(sData.substring(1))
            }
            'k' -> {
                this.aks.onServerWillDisconnect()
            }
            'B' -> {
                when (sAction) {
                    'N' -> {
                        return
                    }
                    'A' -> {
                        when (sData[2]) {
                            'T' -> {
                                this.aks.basics.onAuthorizedCommand(true, sData.substring(3))
                            }
                            'L' -> {
                                this.aks.basics.onAuthorizedLine(sData.substring(3))
                            }
                            'P' -> {
                                this.aks.basics.onAuthorizedCommandPrompt(sData.substring(3))
                            }
                            'C' -> {
                                this.aks.basics.onAuthorizedCommandClear()
                            }
                            'E' -> {
                                this.aks.basics.onAuthorizedCommand(false)
                            }
                            'I' -> {
                                if (sData[3] != 'O') {

                                } else {
                                    this.aks.basics.onAuthorizedInterfaceOpen(sData.substring(4))
                                    // TODO que veut dire ce double break en AS ?
                                    // break;
                                    this.aks.basics.onAuthorizedInterfaceClose(sData.substring(4))
                                }
                            }
                        } // End of switch
                    }
                    'T' -> {
                        this.aks.basics.onReferenceTime(sData.substring(2))
                    }
                    'D' -> {
                        this.aks.basics.onDate(sData.substring(2))
                    }
                    'W' -> {
                        this.aks.basics.onWhoIs(!bError, sData.substring(3))
                    }
                    'P' -> {
                        this.aks.basics.onSubscriberRestriction(sData.substring(2))
                    }
                    'C' -> {
                        this.aks.basics.onFileCheck(sData.substring(2))
                    }
                    'p' -> {
                        this.aks.basics.onAveragePing(sData.substring(2))
                    }
                } // End of switch
            }
            'A' -> {
                when (sAction) {
                    'c' -> {
                        this.aks.account.onCommunity(sData.substring(2))
                    }
                    'd' -> {
                        this.aks.account.onDofusPseudo(sData.substring(2))
                    }
                    'l' -> {
                        this.aks.account.onLogin(!bError, sData.substring(3))
                    }
                    'L' -> {
                        this.aks.account.onCharactersList(!bError, sData.substring(3))
                    }
                    'x' -> {
                        this.aks.account.onServersList(!bError, sData.substring(3))
                    }
                    'A' -> {
                        this.aks.account.onCharacterAdd(!bError, sData.substring(3))
                    }
                    'T' -> {
                        this.aks.account.onTicketResponse(!bError, sData.substring(3))
                    }
                    'X' -> {
                        this.aks.account.onSelectServer(!bError, true, sData.substring(3))
                    }
                    'Y' -> {
                        this.aks.account.onSelectServer(!bError, false, sData.substring(3))
                    }
                    'S' -> {
                        this.aks.account.onCharacterSelected(!bError, sData.substring(4))
                    }
                    's' -> {
                        this.aks.account.onStats(sData.substring(2))
                    }
                    'N' -> {
                        this.aks.account.onNewLevel(sData.substring(2))
                    }
                    'R' -> {
                        this.aks.account.onRestrictions(sData.substring(2))
                    }
                    'H' -> {
                        this.aks.account.onHosts(sData.substring(2))
                    }
                    'r' -> {
                        this.aks.account.onRescue(!bError)
                    }
                    'g' -> {
                        this.aks.account.onGiftsList(sData.substring(2))
                    }
                    'G' -> {
                        this.aks.account.onGiftStored(!bError)
                    }
                    'q' -> {
                        this.aks.account.onQueue(sData.substring(2))
                    }
                    'f' -> {
                        this.aks.account.onNewQueue(sData.substring(2))
                    }
                    'V' -> {
                        this.aks.account.onRegionalVersion(sData.substring(2))
                    }
                    'P' -> {
                        this.aks.account.onCharacterNameGenerated(!bError, sData.substring(3))
                    }
                    'K' -> {
                        this.aks.account.onKey(sData.substring(2))
                    }
                    'Q' -> {
                        this.aks.account.onSecretQuestion(sData.substring(2))
                    }
                    'D' -> {
                        this.aks.account.onCharacterDelete(!bError, sData.substring(3))
                    }
                    'M' -> {
                        when (sData[2]) {
                            '?' -> {
                                this.aks.account.onCharactersMigrationAskConfirm(sData.substring(3))
                            }
                            else -> {
                                this.aks.account.onCharactersList(!bError, sData.substring(3), true)
                            }
                        } // End of switch
                    }
                    'F' -> {
                        this.aks.account.onFriendServerList(sData.substring(2))
                    }
                    'm' -> {
                        //				if (!CONFIG.isStreaming) {
                        //					this.aks.getAccount().onMiniClipInfo();
                        //				} else {
                        var value: Int
                        try {
                            value = Integer.parseInt("" + sData[2], 10)
                        } catch (e: Exception) {
                            value = 3
                        }

                        logger.warn("GoToCongratulation : $value Not implemented")
                    }//				}
                } // End of switch
            }
            'G' -> {
                when (sAction) {
                    'C' -> {
                        this.aks.game.onCreate(!bError, sData.substring(4))
                    }
                    'J' -> {
                        this.aks.game.onJoin(sData.substring(3))
                    }
                    'P' -> {
                        this.aks.game.onPositionStart(sData.substring(2))
                    }
                    'R' -> {
                        this.aks.game.onReady(sData.substring(2))
                    }
                    'S' -> {
                        this.aks.game.onStartToPlay()
                    }
                    'E' -> {
                        this.aks.game.onEnd(sData.substring(2))
                    }
                    'M' -> {
                        this.aks.game.onMovement(sData.substring(3))
                    }
                    'c' -> {
                        this.aks.game.onChallenge(sData.substring(2))
                    }
                    't' -> {
                        this.aks.game.onTeam(sData.substring(2))
                    }
                    'V' -> {
                        this.aks.game.onLeave(true, sData.substring(2))
                    }
                    'f' -> {
                        this.aks.game.onFlag(sData.substring(2))
                    }
                    'I' -> {
                        when (sData[2]) {
                            'C' -> {
                                this.aks.game.onPlayersCoordinates(sData.substring(4))
                            }
                            'E' -> {
                                this.aks.game.onEffect(sData.substring(3))
                            }
                            'e' -> {
                                this.aks.game.onClearAllEffect(sData.substring(3))
                            }
                            'P' -> {
                                this.aks.game.onPVP(sData.substring(3), false)
                            }
                        } // End of switch
                    }
                    'D' -> {
                        when (sData[2]) {
                            'M' -> {
                                this.aks.game.onMapData(sData.substring(4))
                            }
                            'K' -> {
                                this.aks.game.onMapLoaded()
                            }
                            'C' -> {
                                this.aks.game.onCellData(sData.substring(3))
                            }
                            'Z' -> {
                                this.aks.game.onZoneData(sData.substring(3))
                            }
                            'O' -> {
                                this.aks.game.onCellObject(sData.substring(3))
                            }
                            'F' -> {
                                this.aks.game.onFrameObject2(sData.substring(4))
                            }
                            'E' -> {
                                this.aks.game.onFrameObjectExternal(sData.substring(4))
                            }
                        } // End of switch
                    }
                    'd' -> {
                        when (sData[3]) {
                            'K' -> {
                                this.aks.game.onFightChallengeUpdate(sData.substring(4), true)
                            }
                            'O' -> {
                                this.aks.game.onFightChallengeUpdate(sData.substring(4), false)
                            }
                            else -> {
                                this.aks.game.onFightChallenge(sData.substring(2))
                            }
                        } // End of switch
                    }
                    'A' -> {
                        when (sData[2]) {
                            'S' -> {
                                this.aks.gameActions.onActionsStart(sData.substring(3))
                            }
                            'F' -> {
                                this.aks.gameActions.onActionsFinish(sData.substring(3))
                            }
                            else -> {
                                this.aks.gameActions.onActions(sData.substring(2))
                            }
                        } // End of switch
                    }
                    'T' -> {
                        when (sData[2]) {
                            'S' -> {
                                this.aks.game.onTurnStart(sData.substring(3))
                            }
                            'F' -> {
                                this.aks.game.onTurnFinish(sData.substring(3))
                            }
                            'L' -> {
                                this.aks.game.onTurnlist(sData.substring(4))
                            }
                            'M' -> {
                                this.aks.game.onTurnMiddle(sData.substring(4))
                            }
                            'R' -> {
                                this.aks.game.onTurnReady(sData.substring(3))
                            }
                        } // End of switch
                    }
                    'X' -> {
                        this.aks.game.onExtraClip(sData.substring(2))
                    }
                    'o' -> {
                        this.aks.game.onFightOption(sData.substring(2))
                    }
                    'O' -> {
                        this.aks.game.onGameOver()
                    }
                } // End of switch
            }
            'c' -> {
                when (sAction) {
                    'M' -> {
                        this.aks.chat.onMessage(!bError, sData.substring(3))
                    }
                    's' -> {
                        this.aks.chat.onServerMessage(sData.substring(2))
                    }
                    'S' -> {
                        this.aks.chat.onSmiley(sData.substring(2))
                    }
                    'C' -> {
                        this.aks.chat.onSubscribeChannel(sData.substring(2))
                    }
                } // End of switch
            }
            'D' -> {
                when (sAction) {
                    'A' -> {
                        this.aks.dialog.onCustomAction(sData.substring(2))
                    }
                    'C' -> {
                        this.aks.dialog.onCreate(!bError, sData.substring(3))
                    }
                    'Q' -> {
                        this.aks.dialog.onQuestion(sData.substring(2))
                    }
                    'V' -> {
                        this.aks.dialog.onLeave()
                    }
                    'P' -> {
                        this.aks.dialog.onPause()
                    }
                } // End of switch
            }
            'I' -> {
                when (sAction) {
                    'M' -> {
                        this.aks.infos.onInfoMaps(sData.substring(2))
                    }
                    'C' -> {
                        this.aks.infos.onInfoCompass(sData.substring(2))
                    }
                    'H' -> {
                        this.aks.infos.onInfoCoordinatespHighlight(sData.substring(2))
                    }
                    'm' -> {
                        this.aks.infos.onMessage(sData.substring(2))
                    }
                    'Q' -> {
                        this.aks.infos.onQuantity(sData.substring(2))
                    }
                    'O' -> {
                        this.aks.infos.onObject(sData.substring(2))
                    }
                    'L' -> {
                        when (sData[2]) {
                            'S' -> {
                                this.aks.infos.onLifeRestoreTimerStart(sData.substring(3))
                            }
                            'F' -> {
                                this.aks.infos.onLifeRestoreTimerFinish(sData.substring(3))
                            }
                        } // End of switch
                    }
                } // End of switch
            }
            'S' -> {
                when (sAction) {
                    'L' -> {
                        when (sData[2]) {
                            'o' -> {
                                this.aks.spells.onChangeOption(sData.substring(3))
                            }
                            else -> {
                                this.aks.spells.onList(sData.substring(2))
                            }
                        } // End of switch
                    }
                    'U' -> {
                        this.aks.spells.onUpgradeSpell(!bError, sData.substring(3))
                    }
                    'B' -> {
                        this.aks.spells.onSpellBoost(sData.substring(2))
                    }
                    'F' -> {
                        this.aks.spells.onSpellForget(sData.substring(2))
                    }
                } // End of switch
            }
            'O' -> {
                when (sAction) {
                    'a' -> {
                        this.aks.items.onAccessories(sData.substring(2))
                    }
                    'D' -> {
                        this.aks.items.onDrop(!bError, sData.substring(3))
                    }
                    'A' -> {
                        this.aks.items.onAdd(!bError, sData.substring(3))
                    }
                    'C' -> {
                        this.aks.items.onChange(sData.substring(3))
                    }
                    'R' -> {
                        this.aks.items.onRemove(sData.substring(2))
                    }
                    'Q' -> {
                        this.aks.items.onQuantity(sData.substring(2))
                    }
                    'M' -> {
                        this.aks.items.onMovement(sData.substring(2))
                    }
                    'T' -> {
                        this.aks.items.onTool(sData.substring(2))
                    }
                    'w' -> {
                        this.aks.items.onWeight(sData.substring(2))
                    }
                    'S' -> {
                        this.aks.items.onItemSet(sData.substring(2))
                    }
                    'K' -> {
                        this.aks.items.onItemUseCondition(sData.substring(2))
                    }
                    'F' -> {
                        this.aks.items.onItemFound(sData.substring(2))
                    }
                } // End of switch
            }
            'F' -> {
                when (sAction) {
                    'A' -> {
                        this.aks.friends.onAddFriend(!bError, sData.substring(3))
                    }
                    'D' -> {
                        this.aks.friends.onRemoveFriend(!bError, sData.substring(3))
                    }
                    'L' -> {
                        this.aks.friends.onFriendsList(sData.substring(3))
                    }
                    'S' -> {
                        this.aks.friends.onSpouse(sData.substring(2))
                    }
                    'O' -> {
                        this.aks.friends.onNotifyChange(sData.substring(2))
                    }
                } // End of switch
            }
            'i' -> {
                when (sAction) {
                    'A' -> {
                        this.aks.enemies.onAddEnemy(!bError, sData.substring(3))
                    }
                    'D' -> {
                        this.aks.enemies.onRemoveEnemy(!bError, sData.substring(3))
                    }
                    'L' -> {
                        this.aks.enemies.onEnemiesList(sData.substring(3))
                    }
                } // End of switch
            }
            'K' -> {
                when (sAction) {
                    'C' -> {
                        this.aks.keys.onCreate(sData.substring(3))
                    }
                    'K' -> {
                        this.aks.keys.onKey(!bError)
                    }
                    'V' -> {
                        this.aks.keys.onLeave()
                    }
                } // End of switch
            }
            'J' -> {
                when (sAction) {
                    'S' -> {
                        this.aks.job.onSkills(sData.substring(3))
                    }
                    'X' -> {
                        this.aks.job.onXP(sData.substring(3))
                    }
                    'N' -> {
                        this.aks.job.onLevel(sData.substring(2))
                    }
                    'R' -> {
                        this.aks.job.onRemove(sData.substring(2))
                    }
                    'O' -> {
                        this.aks.job.onOptions(sData.substring(2))
                    }
                } // End of switch
            }
            'E' -> {
                when (sAction) {
                    'R' -> {
                        this.aks.exchange.onRequest(!bError, sData.substring(3))
                    }
                    'K' -> {
                        this.aks.exchange.onReady(sData.substring(2))
                    }
                    'V' -> {
                        this.aks.exchange.onLeave(!bError, sData.substring(2))
                    }
                    'C' -> {
                        this.aks.exchange.onCreate(!bError, sData.substring(3))
                    }
                    'c' -> {
                        this.aks.exchange.onCraft(!bError, sData.substring(3))
                    }
                    'M' -> {
                        this.aks.exchange.onLocalMovement(!bError, sData.substring(3))
                    }
                    'm' -> {
                        this.aks.exchange.onDistantMovement(!bError, sData.substring(3))
                    }
                    'r' -> {
                        this.aks.exchange.onCoopMovement(!bError, sData.substring(3))
                    }
                    'p' -> {
                        this.aks.exchange.onPayMovement(!bError, sData.substring(2))
                    }
                    's' -> {
                        this.aks.exchange.onStorageMovement(!bError, sData.substring(3))
                    }
                    'i' -> {
                        this.aks.exchange.onPlayerShopMovement(!bError, sData.substring(3))
                    }
                    'W' -> {
                        this.aks.exchange.onCraftPublicMode(sData.substring(2))
                    }
                    'e' -> {
                        this.aks.exchange.onMountStorage(sData.substring(2))
                    }
                    'f' -> {
                        this.aks.exchange.onMountPark(sData.substring(2))
                    }
                    'w' -> {
                        this.aks.exchange.onMountPods(sData.substring(2))
                    }
                    'L' -> {
                        this.aks.exchange.onList(sData.substring(2))
                    }
                    'S' -> {
                        this.aks.exchange.onSell(!bError)
                    }
                    'B' -> {
                        this.aks.exchange.onBuy(!bError)
                    }
                    'q' -> {
                        this.aks.exchange.onAskOfflineExchange(sData.substring(2))
                    }
                    'H' -> {
                        when (sData[2]) {
                            'S' -> {
                                this.aks.exchange.onSearch(sData.substring(3))
                            }
                            'L' -> {
                                this.aks.exchange.onBigStoreTypeItemsList(sData.substring(3))
                            }
                            'M' -> {
                                this.aks.exchange.onBigStoreTypeItemsMovement(sData.substring(3))
                            }
                            'l' -> {
                                this.aks.exchange.onBigStoreItemsList(sData.substring(3))
                            }
                            'm' -> {
                                this.aks.exchange.onBigStoreItemsMovement(sData.substring(3))
                            }
                            'P' -> {
                                this.aks.exchange.onItemMiddlePriceInBigStore(sData.substring(3))
                            }
                        } // End of switch
                    }
                    'J' -> {
                        this.aks.exchange.onCrafterListChanged(sData.substring(2))
                    }
                    'j' -> {
                        this.aks.exchange.onCrafterReference(sData.substring(2))
                    }
                    'A' -> {
                        this.aks.exchange.onCraftLoop(sData.substring(2))
                    }
                    'a' -> {
                        this.aks.exchange.onCraftLoopEnd(sData.substring(2))
                    }
                } // End of switch
            }
            'h' -> {
                when (sAction) {
                    'L' -> {
                        this.aks.houses.onList(sData.substring(2))
                    }
                    'P' -> {
                        this.aks.houses.onProperties(sData.substring(2))
                    }
                    'X' -> {
                        this.aks.houses.onLockedProperty(sData.substring(2))
                    }
                    'C' -> {
                        this.aks.houses.onCreate(sData.substring(3))
                    }
                    'S' -> {
                        this.aks.houses.onSell(!bError, sData.substring(3))
                    }
                    'B' -> {
                        this.aks.houses.onBuy(!bError, sData.substring(3))
                    }
                    'V' -> {
                        this.aks.houses.onLeave()
                    }
                    'G' -> {
                        this.aks.houses.onGuildInfos(sData.substring(2))
                    }
                } // End of switch
            }
            's' -> {
                when (sAction) {
                    'L' -> {
                        this.aks.storage.onList(sData.substring(2))
                    }
                    'X' -> {
                        this.aks.storage.onLockedProperty(sData.substring(2))
                    }
                } // End of switch
            }
            'e' -> {
                when (sAction) {
                    'U' -> {
                        this.aks.emotes.onUse(!bError, sData.substring(3))
                    }
                    'L' -> {
                        this.aks.emotes.onList(sData.substring(2))
                    }
                    'A' -> {
                        this.aks.emotes.onAdd(sData.substring(2))
                    }
                    'R' -> {
                        this.aks.emotes.onRemove(sData.substring(2))
                    }
                    'D' -> {
                        this.aks.emotes.onDirection(sData.substring(2))
                    }
                } // End of switch
            }
            'd' -> {
                when (sAction) {
                    'C' -> {
                        this.aks.documents.onCreate(!bError, sData.substring(3))
                    }
                    'V' -> {
                        this.aks.documents.onLeave()
                    }
                } // End of switch
            }
            'g' -> {
                when (sAction) {
                    'n' -> {
                        this.aks.guild.onNew()
                    }
                    'C' -> {
                        this.aks.guild.onCreate(!bError, sData.substring(3))
                    }
                    'S' -> {
                        this.aks.guild.onStats(sData.substring(2))
                    }
                    'I' -> {
                        when (sData[2]) {
                            'G' -> {
                                this.aks.guild.onInfosGeneral(sData.substring(3))
                            }
                            'M' -> {
                                this.aks.guild.onInfosMembers(sData.substring(3))
                            }
                            'B' -> {
                                this.aks.guild.onInfosBoosts(sData.substring(3))
                            }
                            'F' -> {
                                this.aks.guild.onInfosMountPark(sData.substring(3))
                            }
                            'T' -> {
                                when (sData[3]) {
                                    'M' -> {
                                        this.aks.guild.onInfosTaxCollectorsMovement(sData.substring(4))
                                    }
                                    'P' -> {
                                        this.aks.guild.onInfosTaxCollectorsPlayers(sData.substring(4))
                                    }
                                    'p' -> {
                                        this.aks.guild.onInfosTaxCollectorsAttackers(sData.substring(4))
                                    }
                                } // End of switch
                            }
                            'H' -> {
                                this.aks.guild.onInfosHouses(sData.substring(3))
                            }
                        } // End of switch
                    }
                    'J' -> {
                        when (sData[2]) {
                            'E' -> {
                                this.aks.guild.onJoinError(sData.substring(3))
                            }
                            'R' -> {
                                this.aks.guild.onRequestLocal(sData.substring(3))
                            }
                            'r' -> {
                                this.aks.guild.onRequestDistant(sData.substring(3))
                            }
                            'K' -> {
                                this.aks.guild.onJoinOk(sData.substring(3))
                            }
                            'C' -> {
                                this.aks.guild.onJoinDistantOk()
                            }
                        } // End of switch
                    }
                    'V' -> {
                        this.aks.guild.onLeave()
                    }
                    'K' -> {
                        this.aks.guild.onBann(!bError, sData.substring(3))
                    }
                    'H' -> {
                        this.aks.guild.onHireTaxCollector(!bError, sData.substring(3))
                    }
                    'A' -> {
                        this.aks.guild.onTaxCollectorAttacked(sData.substring(2))
                    }
                    'T' -> {
                        this.aks.guild.onTaxCollectorInfo(sData.substring(2))
                    }
                    'U' -> {
                        this.aks.guild.onUserInterfaceOpen(sData.substring(2))
                    }
                } // End of switch
            }
            'W' -> {
                when (sAction) {
                    'C' -> {
                        this.aks.waypoints.onCreate(sData.substring(2))
                    }
                    'V' -> {
                        this.aks.waypoints.onLeave()
                    }
                    'U' -> {
                        this.aks.waypoints.onUseError()
                    }
                    'c' -> {
                        this.aks.subway.onCreate(sData.substring(2))
                    }
                    'v' -> {
                        this.aks.subway.onLeave()
                    }
                    'u' -> {
                        this.aks.subway.onUseError()
                    }
                    'p' -> {
                        this.aks.subway.onPrismCreate(sData.substring(2))
                    }
                    'w' -> {
                        this.aks.subway.onPrismLeave()
                    }
                } // End of switch
            }
            'a' -> {
                when (sAction) {
                    'l' -> {
                        this.aks.subareas.onList(sData.substring(3))
                    }
                    'm' -> {
                        this.aks.subareas.onAlignmentModification(sData.substring(2))
                    }
                    'M' -> {
                        this.aks.conquest.onAreaAlignmentChanged(sData.substring(2))
                    }
                } // End of switch
            }
            'C' -> {
                when (sAction) {
                    'I' -> {
                        when (sData[2]) {
                            'J' -> {
                                this.aks.conquest.onPrismInfosJoined(sData.substring(3))
                            }
                            'V' -> {
                                this.aks.conquest.onPrismInfosClosing(sData.substring(3))
                            }
                        } // End of switch
                    }
                    'B' -> {
                        this.aks.conquest.onConquestBonus(sData.substring(2))
                    }
                    'A' -> {
                        this.aks.conquest.onPrismAttacked(sData.substring(2))
                    }
                    'S' -> {
                        this.aks.conquest.onPrismSurvived(sData.substring(2))
                    }
                    'D' -> {
                        this.aks.conquest.onPrismDead(sData.substring(2))
                    }
                    'P' -> {
                        this.aks.conquest.onPrismFightAddPlayer(sData.substring(2))
                    }
                    'p' -> {
                        this.aks.conquest.onPrismFightAddEnemy(sData.substring(2))
                    }
                    'W' -> {
                        this.aks.conquest.onWorldData(sData.substring(2))
                    }
                    'b' -> {
                        this.aks.conquest.onConquestBalance(sData.substring(2))
                    }
                } // End of switch
            }
            'Z' -> {
                when (sAction) {
                    'S' -> {
                        this.aks.specialization.onSet(sData.substring(2))
                    }
                    'C' -> {
                        this.aks.specialization.onChange(sData.substring(2))
                    }
                } // End of switch
            }
            'f' -> {
                when (sAction) {
                    'C' -> {
                        this.aks.fight.onCount(sData.substring(2))
                    }
                    'L' -> {
                        this.aks.fight.onList(sData.substring(2))
                    }
                    'D' -> {
                        this.aks.fight.onDetails(sData.substring(2))
                    }
                } // End of switch
            }
            'T' -> {
                when (sAction) {
                    'C' -> {
                        this.aks.tutorial.onCreate(sData.substring(2))
                    }
                    'T' -> {
                        this.aks.tutorial.onShowTip(sData.substring(2))
                    }
                    'B' -> {
                        this.aks.tutorial.onGameBegin()
                    }
                } // End of switch
            }
            'Q' -> {
                when (sAction) {
                    'L' -> {
                        this.aks.quests.onList(sData.substring(3))
                    }
                    'S' -> {
                        this.aks.quests.onStep(sData.substring(2))
                    }
                } // End of switch
            }
            'P' -> {
                when (sAction) {
                    'I' -> {
                        this.aks.party.onInvite(!bError, sData.substring(3))
                    }
                    'L' -> {
                        this.aks.party.onLeader(sData.substring(2))
                    }
                    'R' -> {
                        this.aks.party.onRefuse(sData.substring(2))
                    }
                    'A' -> {
                        this.aks.party.onAccept(sData.substring(2))
                    }
                    'C' -> {
                        this.aks.party.onCreate(!bError, sData.substring(3))
                    }
                    'V' -> {
                        this.aks.party.onLeave(sData.substring(2))
                    }
                    'F' -> {
                        this.aks.party.onFollow(!bError, sData.substring(3))
                    }
                    'M' -> {
                        this.aks.party.onMovement(sData.substring(2))
                    }
                } // End of switch
            }
            'R' -> {
                when (sAction) {
                    'e' -> {
                        this.aks.mount.onEquip(sData.substring(2))
                    }
                    'x' -> {
                        this.aks.mount.onXP(sData.substring(2))
                    }
                    'n' -> {
                        this.aks.mount.onName(sData.substring(2))
                    }
                    'd' -> {
                        this.aks.mount.onData(sData.substring(2))
                    }
                    'p' -> {
                        this.aks.mount.onMountPark(sData.substring(2))
                    }
                    'D' -> {
                        this.aks.mount.onMountParkBuy(sData.substring(2))
                    }
                    'v' -> {
                        this.aks.mount.onLeave(sData.substring(2))
                    }
                    'r' -> {
                        this.aks.mount.onRidingState(sData.substring(2))
                    }
                } // End of switch
            }
        } // End of switch
    }
}
