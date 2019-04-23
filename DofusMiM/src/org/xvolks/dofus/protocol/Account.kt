package org.xvolks.dofus.protocol

import java.io.IOException

import org.apache.log4j.Logger
import org.xvolks.dofus.util.Tr

import dofus.aks.Aks
import dofus.aks.GameServer
import dofus.aks.Aks.EnumServer

object Account {
    private val logger = Logger.getLogger(Account::class.java!!)

    @Throws(IOException::class)
    fun onSelectServer(bSuccess: Boolean, bUseIp: Boolean, sExtraData: String): GameServer? {
        if (bSuccess) {
            var ip: String
            var port: Int
            val ticket: String
            if (bUseIp) {
                val ret = Aks.unCryptIp(sExtraData)
                ip = ret[0]
                port = Integer.parseInt(ret[1])
                ticket = ret[2]
            } else {
                val _loc14 = sExtraData.split(";".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                val _loc15 = _loc14[0].split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                ip = _loc15[0]
                port = Integer.parseInt(_loc15[1])
                ticket = _loc14[1]
            } // end else if
            val _loc16: GameServer? = null// this.api.config.getCustomIP(this.api.datacenter.Basics.aks_incoming_server_id);
            if (_loc16 != null) {
                ip = _loc16.ipAddress
                port = _loc16.port
            } // end if

            val gameServer = GameServer(ip, port, ticket)
            try {
                Thread.sleep(300)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            return gameServer
        } else {
            when (sExtraData[0]) {
                'd' -> {
                    logger.warn(Tr.tr("CANT_CHOOSE_CHARACTER_SERVER_DOWN"))
                }
                'f' -> {
                    var message = Tr.tr("CANT_CHOOSE_CHARACTER_SERVER_FULL")
                    if (sExtraData.substring(1).length > 0) {
                        val serverInfo = sExtraData.substring(1).split("\\|".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                        message = "$message<br/><br/>"
                        message = message + Tr.tr("SERVERS_ACCESSIBLES") + " : <br/>"

                        for (i in serverInfo.indices) {
                            val server = EnumServer.values()[Integer.parseInt(serverInfo[i])]
                            message = message + server.serverName
                            message = message + if (i == serverInfo.size - 1) "." else ", "
                        } // end while
                    } // end if
                    logger.warn(message)
                }
                'F' -> {
                    logger.warn(Tr.tr("SERVER_FULL"))
                }
                's' -> {
                    logger.warn(Tr.tr("CANT_CHOOSE_CHARACTER_SHOP_OTHER_SERVER" + " " + Integer.parseInt(sExtraData.substring(1))))
                }
                'r' -> {
                    logger.warn(Tr.tr("CANT_SELECT_THIS_SERVER"))
                }
            } // End of switch
            return null
        } // end else if
    }

}
