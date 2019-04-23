package dofus.aks.processor.listeners

interface QuestsProcessorListener {

    fun onList(substring: String)

    fun onStep(substring: String)

}
