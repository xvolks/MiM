package org.xvolks.dofus

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.EOFException
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.util.Timer
import java.util.TimerTask
import java.util.regex.Matcher
import java.util.regex.Pattern

import javax.net.SocketFactory

import org.apache.log4j.Logger
import org.xvolks.dofus.events.GUIListener
import org.xvolks.dofus.events.TrafficEvent
import org.xvolks.dofus.events.TrafficListener
import org.xvolks.dofus.events.TrafficEvent.Source
import org.xvolks.dofus.injection.Plugin
import org.xvolks.dofus.injection.PluginManager
import org.xvolks.dofus.protocol.Account

import dofus.aks.Aks
import dofus.aks.GameServer

class Communication @Throws(IOException::class)
constructor(private val clientSocket: Socket, private val realGameServer: GameServer, private val fakeGameServer: GameServer?, val guiListeners: List<GUIListener>, val pluginManager: PluginManager) {

    private val logger = Logger.getLogger(Communication::class.java!!)
    private val clientOut: BufferedOutputStream
    private val clientIn: BufferedInputStream?
    private var serverSocket: Socket? = null
    private lateinit var serverOut: BufferedOutputStream
    private var serverIn: BufferedInputStream? = null
    private val server: ServerCommunication
    private val client: ClientCommunication
    var isRunning = true
        private set
    private var injectionBot: InjectionBot? = null

    /**
     * Thread de communication avec le serveur Dofus
     * @author xvolks
     */
    internal inner class ServerCommunication @Throws(IOException::class)
    constructor() : Thread() {
        init {
            serverSocket = SocketFactory.getDefault().createSocket(realGameServer.ipAddress, realGameServer.port)
            name = "Communication Server thread : " + serverSocket!!.remoteSocketAddress
            serverIn = BufferedInputStream(serverSocket!!.getInputStream())
            serverOut = BufferedOutputStream(serverSocket!!.getOutputStream())
            start()
        }

        override fun run() {
            val m = Pattern.compile("A[XY].+").matcher("")
            while (isRunning) {
                try {
                    val string = readFromServer()
                    if (string == null) {
                        Thread.sleep(50)
                    } else {
                        if (m.reset(string).matches()) {
                            if (string[2] == 'E') {
                                //Erreur détectée par le serveur
                                sendToClient(string)
                                clientSocket!!.close()
                                serverSocket!!.close()
                                error("Erreur détectée par le serveur")
                                System.exit(0)
                            }
                            translateGameServerConnection(string.substring(0, 3), string.substring(3))
                        }
                        sendToClient(string)
                    }
                    //					Thread.sleep(300);
                } catch (e: IOException) {
                    warning("Fin de connexion")
                    logger.error("Fin de connexion", e)
                    isRunning = false
                    client.interrupt()
                } catch (e: Exception) {
                    logger.error(e, e)
                }

            }
            try {
                serverSocket!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        @Throws(IOException::class)
        private fun translateGameServerConnection(headerData: String, sExtraData: String) {
            val realGameServer = Account.onSelectServer(true, true, sExtraData)
            val fakeGameServer = GameServer("127.0.0.1", 443, realGameServer!!.ticket)
            val s = Aks.cryptIp(fakeGameServer)
            sendToClient(headerData + s)
            client.interrupt()
            isRunning = false
            try {
                client.join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            val t = Timer()
            t.schedule(object : TimerTask() {
                override fun run() {
                    try {
                        this@ServerCommunication.join()
                        Main.runProxy(realGameServer, fakeGameServer, guiListeners)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                }
            }, 300)

        }
    }

    /**
     * Thread de communication avec le client Dofus
     * @author xvolks
     */
    internal inner class ClientCommunication : Thread() {
        init {
            name = "Communication Client thread : " + clientSocket!!.remoteSocketAddress
            logger.info("****************************")
            logger.info("* Welcome to new Client ;) *")
            logger.info("****************************")
            start()
        }

        override fun run() {
            while (isRunning) {
                try {
                    val string = readFromClient()
                    if (string == null) {
                        Thread.sleep(50)
                    } else {
                        sendToServer(string)
                    }
                    //					Thread.sleep(300);
                } catch (e: IOException) {
                    warning("Fin de connexion")
                    logger.error("Fin de connexion", e)
                    isRunning = false
                    server.interrupt()
                } catch (e: Exception) {
                    logger.error(e, e)
                }

            }
            try {
                clientSocket!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    init {
        clientIn = BufferedInputStream(clientSocket.getInputStream())
        clientOut = BufferedOutputStream(clientSocket.getOutputStream())

        client = ClientCommunication()
        server = ServerCommunication()

        if (fakeGameServer != null) {
            injectionBot = InjectionBot(this)
            pluginManager.proxyStarted(true)
        } else {
            injectionBot = null
            pluginManager.proxyStarted(false)
        }
    }


    fun error(message: String) {
        for (listener in guiListeners) {
            listener.error(message)
        }
    }


    fun warning(message: String) {
        for (listener in guiListeners) {
            listener.warning(message)
        }

    }


    @Throws(IOException::class)
    private fun readFromClient(): String? {
        logger.debug("RCV Client")
        val readed = readNullTerminatedString(clientIn!!)
        val event = TrafficEvent(Source.FROM_CLIENT, System.currentTimeMillis(), readed)
        for (pluginName in pluginManager.pluginNames) {
            val trafficListener = pluginManager.getPlugin(pluginName).trafficListener
            if (trafficListener != null) {
                try {
                    trafficListener.dataReceived(event)
                } catch (t: Throwable) {
                    logger.error(t, t)
                }

            }
        }
        if (!event.reponseEvent.isEmpty()) {
            //ToDO envoyer les réponses
        }
        return readed
    }

    @Throws(IOException::class)
    private fun readFromServer(): String? {
        logger.debug("RCV Server")
        val readed = readNullTerminatedString(serverIn!!)
        val event = TrafficEvent(Source.FROM_SERVER, System.currentTimeMillis(), readed)
        for (pluginName in pluginManager.pluginNames) {
            val trafficListener = pluginManager.getPlugin(pluginName).trafficListener
            if (trafficListener != null) {
                try {
                    trafficListener.dataReceived(event)
                } catch (t: Throwable) {
                    logger.error(t, t)
                }

            }
        }
        if (!event.reponseEvent.isEmpty()) {
            //ToDO envoyer les réponses
        }
        return readed
    }

    @Throws(IOException::class)
    private fun readNullTerminatedString(`in`: InputStream): String {
        val buf = StringBuilder()
        while (true) {
            val value = `in`.read()
            if (value <= 0) break
            buf.append(value.toChar())
        }
        if (buf.isEmpty()) {
            throw EOFException("Read returned -1")
        }
        val message = buf.toString()
        logger.debug("RCV : $message")
        return message
    }

    @Throws(IOException::class)
    private fun close() {
        try {
            clientIn?.close()
            clientOut?.close()
        } finally {
            if (serverSocket != null)
                serverSocket!!.close()
            clientSocket?.close()
        }
    }

    @Throws(IOException::class)
    private fun sendToClient(sData: String) {
        logger.debug("SND CLT: $sData")
        send(sData, clientOut)
    }

    @Throws(IOException::class)
    private fun sendToServer(sData: String) {
        logger.debug("SND SRV: $sData")
        send(sData, serverOut)
    }

    @Throws(IOException::class)
    private fun send(sData: String, out: OutputStream) {
        var sData = sData
        if (!sData.endsWith("\u0000")) {
            sData += "\u0000"
        }
        synchronized(out) {
            out.write(sData.toByteArray())
            out.flush()
        }
    }

    @Throws(Exception::class)
    fun inject(plugin: Plugin) {
        val dofusCommunicationString = plugin.toDofusCommunicationString()
        if (dofusCommunicationString != null)
            sendToServer(dofusCommunicationString)
    }


    fun join() {
        try {
            server.join()
        } catch (e: InterruptedException) {
            logger.warn("Interruped while waiting for server thread", e)
        }

        try {
            client.join()
        } catch (e: InterruptedException) {
            logger.warn("Interruped while waiting for client thread", e)
        }

    }

}
