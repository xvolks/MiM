package dofus.aks.processor.listeners

interface GameActionsProcessorListener {

    fun onActions(substring: String)

    fun onActionsFinish(substring: String)

    fun onActionsStart(substring: String)

}
