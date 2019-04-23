package org.xvolks.dofusMiM.plugins.map

class ZoneInfo(zoneId: String, val name: String) {
    val zoneId: Int

    init {
        this.zoneId = Integer.parseInt(zoneId)
    }
}
