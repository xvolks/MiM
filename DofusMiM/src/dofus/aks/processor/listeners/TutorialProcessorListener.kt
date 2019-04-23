package dofus.aks.processor.listeners

interface TutorialProcessorListener {

    fun onCreate(substring: String)

    fun onGameBegin()

    fun onShowTip(substring: String)

}
