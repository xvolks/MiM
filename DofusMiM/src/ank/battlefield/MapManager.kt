package ank.battlefield

import java.beans.XMLDecoder
import java.beans.XMLEncoder
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileFilter
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.net.URLConnection
import java.util.ArrayList
import java.util.HashMap
import java.util.Properties
import java.util.regex.Matcher
import java.util.regex.Pattern

import org.apache.log4j.Logger
import org.xvolks.dofus.Main
import org.xvolks.dofus.util.Toolkit
import org.xvolks.dofus.util.swf.SWFUtils

import ank.battlefield.xvolks.MapKey
import dofus.aks.Aks
import dofus.datacenter.Cell


class MapManager {

    private var _lastLoadedMap: Any? = null
    private val _aKeys = HashMap<Int, String>()
    private val maps = HashMap<MapKey, MapData>()
    private var _sFile: File? = null
    private var _urlIndex: Int = 0
    private var _bBuildingMap: Boolean = false
    private val logger = Logger.getLogger(MapManager::class.java!!)

    // group 1 : label
    // group 2 : data type
    // group 3 : data
    private val pushLookup = Pattern.compile("Push Lookup:[0-9]+ \\(.(" +
            "(?:zoneId)|" +
            "(?:id)|" +
            "(?:width)|" +
            "(?:height)|" +
            "(?:backgroundNum)|" +
            "(?:ambianceId)|" +
            "(?:musicId)|" +
            "(?:bOutdoor)|" +
            "(?:capabilities)|" +
            "(?:mapData)" +
            ").\\) ([^:]+):(.*)").matcher("")

    private val mapDataMatcher = Pattern.compile("[0-9]+ \\(\"([0-9a-zA-Z]+)\"\\)").matcher("")

    init {
        val clearMapDir = File(MAPS_CLEAR)
        val maps = clearMapDir.listFiles { pathname -> pathname.name.endsWith(".map") }
        for (map in maps) {
            val fileName = map.name
            val values = fileName.split("[_X\\.]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            try {
                this.maps[MapKey(values[0], values[1])] = loadMap(map)
            } catch (e: IOException) {
                logger.error(e, e)
            }

        }

    }

    @Throws(IOException::class)
    private fun loadMap(map: File): MapData {
        return map.inputStream().use {
            val decoder = XMLDecoder(it)
            decoder.readObject() as MapData
        }
    }

    @Throws(IOException::class)
    private fun saveMap(key: MapKey, mapData: MapData) {
        val file = File(MAPS_CLEAR, key.id + "_" + key.date + "X.map")
        if (!file.parentFile.exists() && !file.parentFile.mkdirs()) {
            logger.error("cannot create directory " + file.parentFile)
        }
        file.outputStream().use { out ->
            val encoder = XMLEncoder(out)
            encoder.writeObject(mapData)
            encoder.close()
        }
    }

    fun loadMap(sID: String, sDate: String, sKey: String?): MapData? {
        val key = MapKey(sID, sDate)
        if (maps.containsKey(key)) {
            return maps[key]
        }
        this._lastLoadedMap = null
        try {
            val dID = java.lang.Double.parseDouble(sID)
            val iID = Integer.parseInt(sID)
            if (!java.lang.Double.isNaN(dID)) {
                if (sKey != null && sKey.length > 0) {
                    _aKeys[iID] = dofus.aks.Aks.prepareKey(sKey)
                } else {
                    _aKeys.remove(iID)
                }
            }
            val swfFile = File(Main.getConfigPreferences()!!.node(Main.IniSections.PATH.sectionName).get("Dofus", ".") + "/" + MAPS_SWF, sID + "_" + sDate + (if (_aKeys[iID] != null) "X" else "") + ".swf")
            if (!swfFile.exists()) {
                //Ask file to server
                val urlBasic = URL("http://staticns.ankama.com/dofus/gamedata/dofus/")
                val url = URL(URL(urlBasic, "maps/"), swfFile.name)
                val cnx = url.openConnection()
                cnx.setRequestProperty("Referer", url.toString())
                cnx.setRequestProperty("x-flash-version", "8,0,22,0")
                cnx.setRequestProperty("User-Agent", "Shockwave Flash")
                //					Host: staticns.ankama.com
                val o = cnx.content as InputStream
                val out = BufferedOutputStream(FileOutputStream(swfFile))
                while (o.available() > 0) {
                    out.write(o.read())
                }
                out.flush()
                out.close()
                //				http://staticns.ankama.com/dofus/gamedata/dofus/maps/7427_0908261117X.swf
            }
            val properties = decompileSWF(swfFile)
            val mapData = loadData(properties, swfFile)
            mapData.mapKey = sKey
            maps[key] = mapData
            saveMap(key, mapData)
            return mapData
        } catch (e: Exception) {
            logger.error(e, e)
            return null
        }

    }

    @Throws(IOException::class, InterruptedException::class)
    private fun decompileSWF(file: File): Properties {
        val out = SWFUtils.decompileSWFToList(file)
        val props = Properties()
        for (line in out!!) {
            if (pushLookup.reset(line).find()) {
                val label = pushLookup.group(1)
                val type = pushLookup.group(2)
                var data = pushLookup.group(3)
                if (type.equals("Lookup", ignoreCase = true)) {
                    //Case of the mapData entry where data is NN ("XXXXXXXXXX") and the real data we need to extract is XXXXXXXX
                    if (mapDataMatcher.reset(data).find()) {
                        data = mapDataMatcher.group(1)
                    } else {
                        logger.warn("$label of type $type=$data is not matching the extractor pattern")
                    }
                }
                props[label] = data
            }
        }

        return props
    }

    private fun loadData(properties: Properties, sFile: File): MapData {
        this._sFile = sFile
        this._urlIndex = -1
        return loadWithNextURL(properties)
    }

    private fun loadWithNextURL(properties: Properties): MapData {
        ++this._urlIndex
        var mapData: MapData? = null
        mapData = loadDataFrom(properties, _sFile)
        parseMap(mapData)
        return mapData
    }

    private fun parseMap(oData: MapData) {
        /* We don't mind now
        if (this.api.network.Game.isBusy)
        {
            this.addToQueue({object: this, method: this.parseMap, params: [oData]});
            return;
        } // end if */
        val mapId = oData.id

        this._bBuildingMap = true
        this._lastLoadedMap = oData
        //        String mapName = this.getMapName(mapId);
        val map = if (_aKeys[mapId] != null)
            dofus.aks.Aks.decypherData(oData.mapData, _aKeys[mapId]!!, dofus.aks.Aks.checksum(_aKeys[mapId]!!) * 2)
        else
            oData.mapData
        oData.map = map
        this._bBuildingMap = false
        logger.debug(map)
    }

    private fun loadDataFrom(p: Properties, swfFile: File?): MapData {
        logger.debug("loading from " + swfFile!!)
        val data = MapData()

        var zoneId = -1
        try {
            zoneId = Integer.parseInt(extractProperty(p, "zoneId"))
        } catch (e: Exception) {
            zoneId = Integer.parseInt(extractProperty(p, "id"))
        }

        data.id = zoneId
        data.width = Integer.parseInt(extractProperty(p, "width"))
        data.height = Integer.parseInt(extractProperty(p, "height"))
        data.backgroundNum = Integer.parseInt(extractProperty(p, "backgroundNum"))
        data.ambianceId = Integer.parseInt(extractProperty(p, "ambianceId"))
        data.musicId = Integer.parseInt(extractProperty(p, "musicId"))
        data.isOutdoor = java.lang.Boolean.parseBoolean(extractProperty(p, "bOutdoor"))
        data.capabilities = Integer.parseInt(extractProperty(p, "capabilities"))
        data.mapData = extractProperty(p, "mapData")
        return data
    }

    private fun extractProperty(p: Properties, propName: String): String {
        var value = p.getProperty(propName)
        value = value.trim { it <= ' ' }
        if (value.endsWith(";")) {
            value = value.substring(0, value.length - 1)
        }
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length - 1)
        }
        return value
    }

    companion object {

        private val MAPS_CLEAR = "maps/clear"
        private val MAPS_SWF = "data/maps"

        fun decodeCell(cellCoded: String): Cell {
            val cell = Cell()
            val chars = cellCoded.toCharArray()
            val decodedChar = IntArray(chars.size)
            for (i in chars.indices) {
                decodedChar[i] = Aks.hashcodes[chars[i].toInt()]
            }
            cell.isLineOfSight = decodedChar[0] and 1 != 0
            cell.isActive = decodedChar[0] and 32 shr 5 != 0
            cell.movement = decodedChar[2] and 56 shr 3
            cell.groundLevel = decodedChar[1] and 15
            cell.layerGroundRot = decodedChar[1] and 48 shr 4
            cell.layerGroundNum = (decodedChar[0] and 24 shl 6) + (decodedChar[2] and 7 shl 6) + decodedChar[3]
            cell.groundSlope = decodedChar[4] and 60 shr 2
            return cell
        }

        fun getMapAsCells(mapData: MapData): List<Cell> {
            val map = ArrayList<Cell>(mapData.map!!.length / 10)
            var i = 0
            while (i < mapData.map!!.length) {
                val cellCoded = mapData.map!!.substring(i, i + 10)
                val cell = decodeCell(cellCoded)
                map.add(cell)
                i += 10
            }
            return map
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val data = testGetMapData()
            val logger = Logger.getLogger(MapManager::class.java!!)
            //		for(int i = 0; i< data.getHeight(); i++) {
            //			for(int j = 0; j< data.getWidth(); j++) {
            //				int beginIndex = 10*i*data.getHeight()+(j*10);
            //				String cellCoded = data.getMap().substring(beginIndex, beginIndex+10);
            //				logger.info(String.format("%02d, %02d = %s", j, i, cellCoded));
            //				Cell cell = decodeCell(cellCoded);
            //				logger.info(String.format("Case %s, %s, movement = %d", (cell.isActive()? "active" : "inactive"), (cell.isLineOfSight() ? "en vue" : "cach�e"), cell.getMovement()));
            //			}
            //		}

            val width = data!!.width
            logger.info(String.format("Map is w=%d, h=%d", width, data.height))
            var line = 0
            var cellNum = 0
            var cellPerLine = 1
            val map = ArrayList<Cell>(data.map!!.length / 10)
            var i = 0
            while (i < data.map!!.length) {
                val cellCoded = data.map!!.substring(i, i + 10)
                val cell = decodeCell(cellCoded)
                map.add(cell)
                if (--cellPerLine == 0) {
                    logger.info(String.format("------------------------------------LINE %d--------------------------------------------", line++))
                    if (line and 1 != 0) {
                        cellPerLine = width
                    } else {
                        cellPerLine = width - 1
                    }
                }
                logger.info(String.format("Case #%d is %s, %s, movement = %d", cellNum, if (cell.isActive) "active" else "inactive", if (cell.isLineOfSight) "en vue" else "cach�e", cell.movement))
                cellNum++
                i += 10
            }
            val h = data.height

            logger.info(String.format("Map is w=%d, h=%d, so %d cells, and the size of data is %d", width, data.height, (2 * width - 1) * h - (width - 1), data.map!!.length))

        }

        fun testGetMapData(): MapData? {
            val sExtraData = "GDM|7427|0908261117|35444f277b736e2671315f7021707a2532356d6c2e2f24742f445556746662397d2e696e29787750656127374d7252253242765573205566352f7e244378755a432e4a6c3c4c575a604177413e672d554f5f33454f455d652969507c342038243c5d324139397e7d7a56554a6941383a52306f3b682c307e315846576e30354570555833596b21632d34253242356859792a4d724a75612a364c71502a343f3f5f704c424a5f5b39674423615f724c594f4a5055227438292028372e4f5f25324271496b463a6d7f627b6c674220617c4e59217a5650483d5641365d4f2532423b34586e3b6f2532427a34217f5d4c7a64744021583b545d30703f5729774b442a795642272d6a"
            //Statue SRAM
            //			"GDM|7392|0908261116|3b2a726535647770413749205e4c4071627f757b67655f745c4428305a5e5e7b7b6c7c23566c7d3763677b5023204e487f20756f2325323534717d206b307f6d75623a5b4578747c59604626686b3979515651782e51606b7f3d76305b4a4d356b5e4d7c79486e3156755a6c6c6a7f3d2c532469423a562e3a634931504251487834284f387f33235c7f5b24276159797a4870427a5f6a352179342532352c79486a";

            //			"GDM|7456|0706131721|2c653b322a4648202f453234234258317d4b25323541233e4665436c423a61646b66253235203e512c7d402d5d6d25323545346b265d75594246585952436f3e56492321784374786566584c2e79725a2f56722532354c305c27494d3e57445578656c4e57776b4e364120595f24294b486e61687637633a31437d52514444284e586d3f4c69602e38657b262729282f2c5f30425028295b63314e3f473421436566253242232c36755b383428744c4c245249405f5f723c7a4e375e60666d48497e325d59703d7270547d5429702a5b2756652c42666c325229544a4a4d334c242c6638584864304952786152605f534a35342532427a696a7029695f40652d41595c3f6e48473c5b";
            //"GDM|7487|0706131721|7b385b3044713c496d41653a4842332c2f737d72275150687c506c2a395a4a5275473126783a79602051352c7a40282a6a2c5a253242516e744459217048404954317d323b253235495221793f39392755562d253235646f593e74335e4d67347e466844562e4935414d3151397e6b4f43546b426c7e5b5a2e357727693c3a3d2d295e4a373a2532357d7121302532427d774b534f283836";
            //"GDM|7488|0706131721|44726d25324244677b766139356a532742764d732845203a6c5226366e276e4d676f486571635528434b5450486a55662a5d4f482c3d3a75732d3b5f5f3b334f6750464025324222464a6a31795c747a4049347b2d466a4f2658683375657b7a3170253235305b73525b592532356d71364b3f7273732f6d3a2f58433e38374b6973762d7260734c3e496540685e7b336c60395d2422546865516e3d26356872756a5d6d544d463749214a337b4140585e51746b4e263969756d2e4f56766e32462f5e393743736b58664e7a525d3b3466445a79516b74435975694a6565456c3049253242794f6671482471393b226a4f7373317874662d2c5c5f3d31432532422045605656593d772e75";
            val values = sExtraData.substring(4).split("\\|".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val sID = values[0]
            val sDate = values[1]
            val sKey = values[2]
            val manager = MapManager()

            return manager.loadMap(sID, sDate, sKey)
        }
    }

}
