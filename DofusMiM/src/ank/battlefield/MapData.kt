package ank.battlefield

import java.io.Serializable


class MapData : Serializable {

    var id: Int = 0
    lateinit var mapData: String
    var map: String? = null
    var mapKey: String? = null
    var ambianceId: Int = 0
    var musicId: Int = 0
    var isOutdoor: Boolean = false
    var capabilities: Int = 0
    var width: Int = 0
    var height: Int = 0
    var backgroundNum: Int = 0

    fun canChallenge(): Boolean {
        return capabilities and 1 == 0
    }

    fun canAttack(): Boolean {
        return capabilities shr 1 and 1 == 0
    }

    fun saveTeleport(): Boolean {
        return capabilities shr 2 and 1 == 0
    }

    fun useTeleport(): Boolean {
        return capabilities shr 3 and 1 == 0
    }

    companion object {
        private const val serialVersionUID = 5192708267779161878L
    }

}
