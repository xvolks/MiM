package dofus.aks.processor.listeners

interface InfosProcessorListener {

    fun onInfoCompass(substring: String)

    fun onInfoCoordinatespHighlight(substring: String)

    fun onInfoMaps(substring: String)

    fun onLifeRestoreTimerFinish(substring: String)

    fun onLifeRestoreTimerStart(substring: String)

    fun onMessage(substring: String)

    fun onObject(substring: String)

    fun onQuantity(substring: String)

}
