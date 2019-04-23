package dofus.aks.processor.listeners

interface ChatProcessorListener {

    fun onMessage(b: Boolean, substring: String)

    fun onServerMessage(substring: String)

    fun onSmiley(substring: String)

    fun onSubscribeChannel(substring: String)

}
