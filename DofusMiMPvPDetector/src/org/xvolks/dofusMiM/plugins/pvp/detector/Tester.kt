package org.xvolks.dofusMiM.plugins.pvp.detector

import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL

import org.apache.log4j.Logger

import ank.battlefield.MapData
import ank.battlefield.MapManager
import java.lang.NullPointerException

object Tester {
    internal var logger = Logger.getLogger(Tester::class.java)
    /**
     * @param args
     * @throws IOException
     */
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {

        //		URL urlBasic = new URL("http://files.jeuxvideo-flash.com/3d99e983a43be1a07804bf0ef376bf0c/");
        //		URL url = new URL(new URL(urlBasic, "4aad6818/"), "8107.swf");
        //		InputStream o = (InputStream)url.getContent();
        //		while (o.available() > 0) {
        //			System.out.println(o.read());
        //		}
        //		System.exit(0);

        //		String sExtraData = "GA0;1;760737;adrbe6ae-";
        var sExtraData = "GA0;1;942615;adjdg3eg1"
        val mapData = MapManager.testGetMapData() ?: throw NullPointerException("mapData is null")
        sExtraData = sExtraData.substring(2)
        var _loc3 = sExtraData.indexOf(";")
        if (_loc3 == -1) {
            System.err.println("GA packet non g�r�  : $sExtraData")
            return
        }
        val _loc4 = Integer.parseInt(sExtraData.substring(0, _loc3))
        sExtraData = sExtraData.substring(_loc3 + 1)
        _loc3 = sExtraData.indexOf(";")
        val _loc5 = Integer.parseInt(sExtraData.substring(0, _loc3))
        sExtraData = sExtraData.substring(_loc3 + 1)
        _loc3 = sExtraData.indexOf(";")
        var _loc6 = sExtraData.substring(0, _loc3)
        val _loc7 = sExtraData.substring(_loc3 + 1)
        if (_loc6.length == 0) {
            _loc6 = "PlayerID"
        } // end if
        val _loc9 = "CurrentPlayerID"
        val isFight = true
        var _loc8: String? = null
        if (isFight && _loc9 != null) {
            _loc8 = _loc9
        } else {
            _loc8 = _loc6
        } // end else if
        val _loc10 = "this.api.datacenter.Sprites.getItemAt($_loc8)"
        //        var _loc11 = _loc10.sequencer;
        //        var _loc12 = _loc10.GameActionsManager;
        val _loc13 = true

        System.err.println("$_loc5 $_loc6 $sExtraData")
        when (_loc5) {
            0 -> {
                return
            }
            1 -> {
                //                var _loc14 = this.api.datacenter.Sprites.getItemAt(_loc6);
                val _loc15 = ank.battlefield.utils.Compressor.extractFullPath(mapData, _loc7)
                //                var _loc16 = _loc14.forceRun || this.api.datacenter.Game.isInCreaturesMode && _loc14 instanceof dofus.datacenter.Character;
                //                var _loc17 = _loc14.forceWalk;
                //                var _loc18 = this.api.datacenter.Game.isFight ? (_loc14 instanceof dofus.datacenter.Character ? (3) : (4)) : (6);
                for (i in _loc15!!) {
                    logger.debug("path : $i")
                }
            }//                this.api.gfx.moveSpriteWithUncompressedPath(_loc6, _loc15, _loc11, !this.api.datacenter.Game.isFight, _loc16, _loc17, _loc18);
            //                if (this.api.datacenter.Game.isRunning)
            //                {
            //                    _loc11.addAction(false, this.api.gfx, this.api.gfx.unSelect, [true]);
            //                } // end if
        }
    }

}
