package org.xvolks.dofus.injection

import org.xvolks.dofus.events.GUIListener
import org.xvolks.dofus.events.TrafficListener

/**
 * classes implementing that interface MUST have an empty constructor
 */
interface Plugin {

    /**
     * Each plugin is called before the proxy has connected to any server (login or game).
     * @return a TrafficListener object to receive the data from the server and from the client.
     * <br></br>The plugin may return null if it is not interested by traffic data
     * Each call to this method should return the same instance of the TrafficListener object
     */
    val trafficListener: TrafficListener?

    /**
     *
     * @return Must return a unique name for that plugin
     */
    val name: String

    /**
     * Each plugin is called at startup. The plugin should do internal initialization here
     */
    fun proxyStarting(manager: PluginManager)

    /**
     * Each plugin is called after startup. The plugin should register GUI elements here
     */
    fun proxyReady()

    /**
     * Each plugin is called when connected to a server.
     * @param server Type<br></br>false = login server<br></br>true = game server
     */
    fun proxyStarted(gameServer: Boolean)

    /**
     * Each plugin is called at stopped time.
     */
    fun proxyStopped()


    /**
     *
     * @return a string representation of the command to inject in a form understandable by server
     */
    fun toDofusCommunicationString(): String?

    /**
     * Give a GUIListener to the plugin so it can access to the graphic interface
     * @param listener
     */
    fun addGUIListener(listener: GUIListener)


}
