package dofus.aks.processor.listeners

interface ItemsProcessorListener {

    fun onAccessories(substring: String)

    fun onDrop(b: Boolean, substring: String)

    fun onAdd(b: Boolean, substring: String)

    fun onChange(substring: String)

    fun onItemFound(substring: String)

    fun onItemSet(substring: String)

    fun onItemUseCondition(substring: String)

    fun onMovement(substring: String)

    fun onQuantity(substring: String)

    fun onRemove(substring: String)

    fun onTool(substring: String)

    fun onWeight(substring: String)

}
