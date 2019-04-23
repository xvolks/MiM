package org.xvolks.dofus.injection.impl.flood

import java.io.IOException

import org.xvolks.dofus.util.JPopupWindow

import dofus.aks.network.IO
import dofus.aks.processor.listeners.GameProcessorListener
import dofus.aks.processor.nullImpl.NullGameProcessor
import dofus.aks.processor.nullImpl.NullProtocolProcessor

class FortuneProtocolProcessor(private val plugin: FortuneFlood) : NullProtocolProcessor() {
    override val io: IO? = null

    override var game: GameProcessorListener = object : NullGameProcessor() {
            override fun onJoin(substring: String) {
                plugin.isFloodAllowed = false
                JPopupWindow.showMessage("Flood " + if (plugin.isFloodAllowed) "activé" else "désactivé")
            }

            override fun onCreate(b: Boolean, substring: String) {
                plugin.isFloodAllowed = false
                JPopupWindow.showMessage("Flood " + if (plugin.isFloodAllowed) "activé" else "désactivé")
            }

            override fun onEnd(substring: String) {
                plugin.isFloodAllowed = plugin.menuItem?.state ?: false
                JPopupWindow.showMessage("Flood " + if (plugin.isFloodAllowed) "activé" else "désactivé")
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
