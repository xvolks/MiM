package dofus.aks.processor.listeners

interface StoragesProcessorListener {

    fun onList(substring: String)

    fun onLockedProperty(substring: String)

}
