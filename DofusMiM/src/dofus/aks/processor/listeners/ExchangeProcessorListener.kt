package dofus.aks.processor.listeners

interface ExchangeProcessorListener {

    fun onCreate(b: Boolean, substring: String)

    fun onCraft(b: Boolean, substring: String)

    fun onCoopMovement(b: Boolean, substring: String)

    fun onDistantMovement(b: Boolean, substring: String)

    fun onCraftPublicMode(substring: String)

    fun onBuy(b: Boolean)

    fun onAskOfflineExchange(substring: String)

    fun onBigStoreTypeItemsList(substring: String)

    fun onBigStoreTypeItemsMovement(substring: String)

    fun onBigStoreItemsList(substring: String)

    fun onBigStoreItemsMovement(substring: String)

    fun onCrafterListChanged(substring: String)

    fun onCrafterReference(substring: String)

    fun onCraftLoop(substring: String)

    fun onCraftLoopEnd(substring: String)

    fun onItemMiddlePriceInBigStore(substring: String)

    fun onLeave(b: Boolean, substring: String)

    fun onList(substring: String)

    fun onLocalMovement(b: Boolean, substring: String)

    fun onMountPark(substring: String)

    fun onMountPods(substring: String)

    fun onMountStorage(substring: String)

    fun onPayMovement(b: Boolean, substring: String)

    fun onPlayerShopMovement(b: Boolean, substring: String)

    fun onReady(substring: String)

    fun onRequest(b: Boolean, substring: String)

    fun onSearch(substring: String)

    fun onSell(b: Boolean)

    fun onStorageMovement(b: Boolean, substring: String)

}
