package dofus.aks.processor.listeners

interface GuildProcessorListener {

    fun onCreate(b: Boolean, substring: String)

    fun onInfosBoosts(substring: String)

    fun onInfosGeneral(substring: String)

    fun onInfosMembers(substring: String)

    fun onInfosMountPark(substring: String)

    fun onInfosTaxCollectorsAttackers(substring: String)

    fun onInfosHouses(substring: String)

    fun onInfosTaxCollectorsMovement(substring: String)

    fun onInfosTaxCollectorsPlayers(substring: String)

    fun onBann(b: Boolean, substring: String)

    fun onHireTaxCollector(b: Boolean, substring: String)

    fun onJoinDistantOk()

    fun onJoinError(substring: String)

    fun onJoinOk(substring: String)

    fun onLeave()

    fun onNew()

    fun onRequestDistant(substring: String)

    fun onRequestLocal(substring: String)

    fun onStats(substring: String)

    fun onTaxCollectorAttacked(substring: String)

    fun onTaxCollectorInfo(substring: String)

    fun onUserInterfaceOpen(substring: String)

}
