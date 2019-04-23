package dofus.aks.processor.listeners

interface FightsProcessorListener {

    fun onCount(substring: String)

    fun onDetails(substring: String)

    fun onList(substring: String)

}
