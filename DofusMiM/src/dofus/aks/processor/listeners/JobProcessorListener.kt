package dofus.aks.processor.listeners

interface JobProcessorListener {

    fun onLevel(substring: String)

    fun onOptions(substring: String)

    fun onRemove(substring: String)

    fun onSkills(substring: String)

    fun onXP(substring: String)

}
