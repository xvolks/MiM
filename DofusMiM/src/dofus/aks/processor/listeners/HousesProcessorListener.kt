package dofus.aks.processor.listeners

interface HousesProcessorListener {

    fun onCreate(substring: String)

    fun onBuy(b: Boolean, substring: String)

    fun onGuildInfos(substring: String)

    fun onLeave()

    fun onList(substring: String)

    fun onLockedProperty(substring: String)

    fun onProperties(substring: String)

    fun onSell(b: Boolean, substring: String)

}
