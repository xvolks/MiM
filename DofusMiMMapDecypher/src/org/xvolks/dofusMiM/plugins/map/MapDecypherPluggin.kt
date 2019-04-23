package org.xvolks.dofusMiM.plugins.map

import java.io.File
import java.io.FileFilter
import java.io.IOException
import java.util.TreeMap
import java.util.regex.Matcher
import java.util.regex.Pattern

import org.apache.log4j.Logger
import org.xvolks.dofus.Main
import org.xvolks.dofus.events.TrafficListener
import org.xvolks.dofus.gui.AbstractPlugin
import org.xvolks.dofus.injection.PluginManager
import org.xvolks.dofus.util.swf.SWFUtils

import ank.battlefield.MapData

class MapDecypherPluggin : AbstractPlugin() {

    internal var logger = Logger.getLogger(MapDecypherPluggin::class.java)
    override val trafficListener: TrafficListener = MapDecypherTrafficListener(this)
    private val mapInfos = TreeMap<Int, MapInfo>()
    private val zoneInfos = TreeMap<Int, ZoneInfo>()
    var currentMapData: MapData? = null

    init {
        instance = this
    }

    override fun proxyStarted(gameServer: Boolean) {}

    override fun proxyStarting(pluginManager: PluginManager) {
        super.proxyStarting(pluginManager)
        decryptMapsFr()
    }

    private fun decryptMapsFr() {
        val directory = File(Main.getConfigPreferences().node(Main.IniSections.PATH.sectionName).get("Dofus", ".") + "/data/lang/swf")
        val mapsFr = directory.listFiles { pathname ->
            val lowerCase = pathname.name.toLowerCase()
            lowerCase.startsWith("maps_fr") && lowerCase.endsWith(".swf")
        }
        if (mapsFr != null && mapsFr.size > 0) {
            //Search for last file version
            val sorter = TreeMap<String, File>()
            for (f in mapsFr) {
                sorter[f.name.toLowerCase()] = f
            }
            while (sorter.size != 1) {
                sorter.remove(sorter.keys.iterator().next())
            }
            var swfFile: File? = null
            for (f in sorter.values) {
                swfFile = f
            }
            try {
                val out = SWFUtils.decompileSWFToList(swfFile!!)
                //		Push int:5867 Lookup:13 ("x") int:-27 Lookup:14 ("y") int:-50 Lookup:12 ("sa") int:44 Lookup:15 ("ep") int:16 int:4
                val mapMatcher = Pattern.compile("Push int:([0-9]+) Lookup:[0-9]+ ..x.. int:(-?[0-9]+) " +
                        "Lookup:[0-9]+ ..y.. int:(-?[0-9]+) " +
                        "Lookup:[0-9]+ ..sa.. int:([0-9]+) ").matcher("")
                //				Push int:0 Lookup:18 ("n") Lookup:17 ("Amakna") Lookup:13 ("sua") int:0 int:2
                val zoneMatcher = Pattern.compile("Push int:([0-9]+) Lookup:[0-9]+ ..n.. Lookup:[0-9]+ ..([^\"]+)..").matcher("")
                for (line in out!!) {
                    if (mapMatcher.reset(line).find()) {
                        val info = MapInfo(mapMatcher.group(1), mapMatcher.group(2), mapMatcher.group(3), mapMatcher.group(4))
                        mapInfos[info.mapId] = info
                    } else if (zoneMatcher.reset(line).find()) {
                        val info = ZoneInfo(zoneMatcher.group(1), zoneMatcher.group(2))
                        zoneInfos[info.zoneId] = info
                    }
                }
                for (id in zoneInfos.keys) {
                    logger.debug(String.format("Zone %d = %s", id, zoneInfos[id]?.name))
                }
            } catch (e: IOException) {
                logger.error(e, e)
            } catch (e: InterruptedException) {
                logger.error(e, e)
            }

        }

    }

    override fun proxyStopped() {}

    override fun proxyReady() {}

    override fun toDofusCommunicationString(): String? {
        return null
    }

    fun getMapInfos(): Map<Int, MapInfo> {
        return mapInfos
    }

    fun getZoneInfos(): Map<Int, ZoneInfo> {
        return zoneInfos
    }

    companion object {
        var instance: MapDecypherPluggin? = null
    }
}
