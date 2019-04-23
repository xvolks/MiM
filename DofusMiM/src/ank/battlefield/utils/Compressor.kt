package ank.battlefield.utils

import java.util.ArrayList

import org.apache.log4j.Logger

import dofus.aks.Aks
import ank.battlefield.MapData
import ank.battlefield.MapManager

object Compressor {
    internal var logger = Logger.getLogger(Compressor::class.java!!)

    internal class NumDir(var num: Int, var dir: Char)

    fun extractFullPath(mapHandler: MapData, compressedData: String): IntArray? {
        val _loc4 = ArrayList<NumDir>()
        val _loc5 = compressedData.toCharArray() //
        val _loc7 = compressedData.length
        val _loc8 = MapManager.getMapAsCells(mapHandler).size

        var _loc6 = 0
        while (_loc6 < _loc7) {
            _loc5[_loc6] = Aks.decode64(_loc5[_loc6].toInt()).toChar()
            _loc5[_loc6 + 1] = Aks.decode64(_loc5[_loc6 + 1].toInt()).toChar()
            _loc5[_loc6 + 2] = Aks.decode64(_loc5[_loc6 + 2].toInt()).toChar()
            val _loc9 = _loc5[_loc6 + 1].toInt() and 15 shl 6 or _loc5[_loc6 + 2].toInt()
            if (_loc9 < 0) {
                logger.error("Case pas sur carte")
                return null
            } // end if
            if (_loc9 > _loc8) {
                logger.error("Case pas sur carte")
                return null
            } // end if
            _loc4.add(NumDir(_loc9, _loc5[_loc6]))
            _loc6 += 3
        } // end while
        return makeFullPath(mapHandler, _loc4)
    }

    private fun makeFullPath(mapHandler: MapData, aLightPath: List<NumDir>): IntArray? {
        val buffer = IntArray(aLightPath.size * 7)
        var _loc6 = 0
        val width = mapHandler.width
        val matrix = intArrayOf(1, width, width * 2 - 1, width - 1, -1, -width, -width * 2 + 1, -(width - 1))
        var num = aLightPath[0].num
        buffer[_loc6] = num

        for (i in 1 until aLightPath.size) {
            val lNum = aLightPath[i].num
            val lDir = aLightPath[i].dir
            var _loc12 = 2 * width + 1
            while (buffer[_loc6] != lNum) {
                num += matrix[lDir.toInt()]
                buffer[++_loc6] = num
                if (--_loc12 < 0) {
                    logger.error("Chemin impossible")
                    return null
                } // end if
            } // end while
            num = lNum
        } // end while
        return buffer
    }
}
