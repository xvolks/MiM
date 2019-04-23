package dofus.aks.processor.listeners

interface EmotesProcessorListener {

    fun onAdd(substring: String)

    fun onDirection(substring: String)

    fun onList(substring: String)

    fun onRemove(substring: String)

    fun onUse(b: Boolean, substring: String)

}
