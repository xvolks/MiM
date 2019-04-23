package dofus.aks.processor.listeners

interface PartyProcessorListener {

    fun onInvite(b: Boolean, substring: String)

    fun onLeader(substring: String)

    fun onAccept(substring: String)

    fun onCreate(b: Boolean, substring: String)

    fun onLeave(substring: String)

    fun onFollow(b: Boolean, substring: String)

    fun onMovement(substring: String)

    fun onRefuse(substring: String)

}
