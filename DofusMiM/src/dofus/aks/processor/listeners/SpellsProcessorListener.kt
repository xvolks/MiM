package dofus.aks.processor.listeners

interface SpellsProcessorListener {

    fun onChangeOption(substring: String)

    fun onList(substring: String)

    fun onSpellBoost(substring: String)

    fun onSpellForget(substring: String)

    fun onUpgradeSpell(b: Boolean, substring: String)

}
