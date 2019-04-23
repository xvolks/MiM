package org.xvolks.dofusMiM.plugins.map

import java.io.IOException

import org.apache.log4j.Logger
import org.xvolks.dofus.events.TrafficEvent
import org.xvolks.dofus.events.TrafficListener
import org.xvolks.dofus.events.TrafficEvent.Source

import ank.battlefield.MapManager

import dofus.aks.ProtocolProcessor
import dofus.aks.network.IO
import dofus.aks.processor.listeners.GameProcessorListener
import dofus.aks.processor.nullImpl.NullGameProcessor
import dofus.aks.processor.nullImpl.NullProtocolProcessor

class MapDecypherTrafficListener(private val mapDecypherPluggin: MapDecypherPluggin) : TrafficListener {

    private val logger = Logger.getLogger(MapDecypherTrafficListener::class.java)
    private val manager = MapManager()


    internal var processor = ProtocolProcessor(object : NullProtocolProcessor() {

        override val io: IO? = null

        override var game: GameProcessorListener = object : NullGameProcessor() {
            override fun onMapData(substring: String) {
                decypherMap(substring)
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

    private fun decypherMap(sExtraData: String) {
        val values = sExtraData.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val sID = values[0]
        val sDate = values[1]
        val sKey = values[2]
        mapDecypherPluggin.currentMapData = manager.loadMap(sID, sDate, sKey)
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

}
