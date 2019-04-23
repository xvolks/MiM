package dofus.aks.processor.listeners

interface EnemiesProcessorListener {

    fun onAddEnemy(b: Boolean, substring: String)

    fun onEnemiesList(substring: String)

    fun onRemoveEnemy(b: Boolean, substring: String)

}
