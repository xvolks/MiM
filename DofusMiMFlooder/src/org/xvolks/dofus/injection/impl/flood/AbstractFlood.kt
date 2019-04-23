package org.xvolks.dofus.injection.impl.flood

import org.xvolks.dofus.gui.AbstractPlugin

abstract class AbstractFlood : AbstractPlugin() {
    var isFloodAllowed = true

    protected abstract val message: String

    override fun proxyStarted(gameServer: Boolean) {}

    override fun proxyStopped() {
        isFloodAllowed = false
    }

    override fun toDofusCommunicationString(): String? {
        return if (isFloodAllowed) String.format("BM*|%s|%n", unPipe(message)) else null
    }

    protected fun unPipe(message: String): String {
        return message.replace('|', ' ')
    }

}
