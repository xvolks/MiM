package dofus.aks.processor.listeners

interface WaypointsProcessorListener {

    fun onCreate(substring: String)

    fun onLeave()

    fun onUseError()

}
