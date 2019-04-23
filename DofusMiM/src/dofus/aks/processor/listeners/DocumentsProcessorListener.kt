package dofus.aks.processor.listeners

interface DocumentsProcessorListener {

    fun onCreate(b: Boolean, substring: String)

    fun onLeave()

}
