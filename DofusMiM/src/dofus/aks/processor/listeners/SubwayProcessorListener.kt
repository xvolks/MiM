package dofus.aks.processor.listeners

interface SubwayProcessorListener {

    fun onCreate(substring: String)

    fun onLeave()

    fun onPrismCreate(substring: String)

    fun onPrismLeave()

    fun onUseError()

}
