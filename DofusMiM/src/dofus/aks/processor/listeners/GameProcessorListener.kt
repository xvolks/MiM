package dofus.aks.processor.listeners

interface GameProcessorListener {

    fun onCellData(substring: String)

    fun onCellObject(substring: String)

    fun onChallenge(substring: String)

    fun onClearAllEffect(substring: String)

    fun onCreate(b: Boolean, substring: String)

    fun onEffect(substring: String)

    fun onEnd(substring: String)

    fun onExtraClip(substring: String)

    fun onFightChallenge(substring: String)

    fun onFightChallengeUpdate(substring: String, b: Boolean)

    fun onFightOption(substring: String)

    fun onFlag(substring: String)

    fun onFrameObject2(substring: String)

    fun onFrameObjectExternal(substring: String)

    fun onGameOver()

    fun onJoin(substring: String)

    fun onLeave(b: Boolean, substring: String)

    fun onMapData(substring: String)

    fun onMapLoaded()

    fun onMovement(substring: String)

    fun onPlayersCoordinates(substring: String)

    fun onPositionStart(substring: String)

    fun onPVP(substring: String, b: Boolean)

    fun onReady(substring: String)

    fun onStartToPlay()

    fun onTeam(substring: String)

    fun onTurnFinish(substring: String)

    fun onTurnlist(substring: String)

    fun onTurnMiddle(substring: String)

    fun onTurnReady(substring: String)

    fun onTurnStart(substring: String)

    fun onZoneData(substring: String)

}
