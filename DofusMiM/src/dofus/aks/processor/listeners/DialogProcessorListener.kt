package dofus.aks.processor.listeners

interface DialogProcessorListener {

    fun onCreate(b: Boolean, substring: String)

    fun onCustomAction(substring: String)

    fun onLeave()

    fun onPause()

    fun onQuestion(substring: String)

}
