package dofus.aks.processor.listeners

interface KeysProcessorListener {

    fun onCreate(substring: String)

    fun onKey(b: Boolean)

    fun onLeave()

}
