package dofus.aks.processor.listeners

interface MountProcessorListener {

    fun onEquip(substring: String)

    fun onData(substring: String)

    fun onLeave(substring: String)

    fun onMountPark(substring: String)

    fun onMountParkBuy(substring: String)

    fun onName(substring: String)

    fun onRidingState(substring: String)

    fun onXP(substring: String)

}
