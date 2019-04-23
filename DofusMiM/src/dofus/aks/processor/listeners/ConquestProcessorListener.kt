package dofus.aks.processor.listeners

interface ConquestProcessorListener {

    fun onAreaAlignmentChanged(substring: String)

    fun onConquestBonus(substring: String)

    fun onConquestBalance(substring: String)

    fun onPrismAttacked(substring: String)

    fun onPrismDead(substring: String)

    fun onPrismFightAddEnemy(substring: String)

    fun onPrismFightAddPlayer(substring: String)

    fun onPrismInfosClosing(substring: String)

    fun onPrismInfosJoined(substring: String)

    fun onPrismSurvived(substring: String)

    fun onWorldData(substring: String)

}
