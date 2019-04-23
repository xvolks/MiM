package org.xvolks.dofusMiM.plugins.map

class MapInfo(mapId: String, x: String, y: String, sa: String) {
    val mapId: Int
    val x: Int
    val y: Int
    val zoneId: Int

    init {
        this.mapId = Integer.parseInt(mapId)
        this.x = Integer.parseInt(x)
        this.y = Integer.parseInt(y)
        this.zoneId = Integer.parseInt(sa)
    }

}
