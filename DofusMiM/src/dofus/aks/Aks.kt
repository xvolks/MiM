package dofus.aks

import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder

import org.apache.log4j.Logger

object Aks {
    internal var logger = Logger.getLogger(Aks::class.java)
    private val ZKARRAY = charArrayOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_')
    val hashcodes: IntArray

    val randomNetworkKey: String
        get() {
            var randomString = ""
            val rnd = Math.round(Math.random() * 20) + 10

            for (i in 0 until rnd) {
                randomString += randomChar
            }

            val s = dofus.aks.Aks.checksum(randomString).toString() + randomString
            return s + dofus.aks.Aks.checksum(s)
        }

    val randomChar: String
        get() {
            val rnd = Math.ceil(Math.random() * 100)
            return if (rnd <= 40) {
                "" + (Math.floor(Math.random() * 26) + 65).toChar()
            } else if (rnd <= 80) {
                "" + (Math.floor(Math.random() * 26) + 97).toChar()
            } else {
                "" + (Math.floor(Math.random() * 10) + 48).toChar()
            }
        }

    enum class EnumServer private constructor(val serverName: String) {
        Jiva("Jiva"),
        Rushu("Rushu"),
        Djaul("Djaul"),
        Raval("Raval"),
        Ecate("Ecate"),
        Sumens("Sumens"),
        Menalt("Menalt"),
        Rosal("Rosal"),
        Mainne("Mainne"),
        Silvosse("Silvosse"),
        Brumaire("Brumaire"),
        Pouchecot("Pouchecot"),
        Silouate("Silouate"),
        Domen("Domen"),
        Amariyo("Amariyo"),
        Rykke_errel("Rykke-errel"),
        Hyrkul("Hyrkul"),
        Helsephine("Helsephine"),
        Alister("Alister"),
        Otomai("Otomai"),
        Lily("Lily"),
        Oto_Mustam("Oto Mustam"),
        Hel_Munster("Hel Munster"),
        Danator("Danator"),
        Kuri("Kuri"),
        Mylaise("Mylaise"),
        Goultard("Goultard"),
        Ulette("Ulette"),
        Vil_Smisse("Vil Smisse"),
        Many("Many")
    }

    init {
        var i = ZKARRAY.size - 1
        hashcodes = IntArray(128)
        while (i >= 0) {
            hashcodes[ZKARRAY[i].toInt()] = i
            --i
        }
    }

    fun decode64(codedValue: Int): Int {
        return hashcodes[codedValue]
    }

    fun encode64(value: Int): Char {
        return ZKARRAY[value]
    }

    fun prepareKey(key: String): String {
        var keyUnpacked = ""
        var cpt = 0
        cpt = 0
        while (cpt < key.length) {
            keyUnpacked += Integer.parseInt(key.substring(cpt, cpt + 2), 16).toChar()
            cpt += 2
        }

        try {
            keyUnpacked = URLDecoder.decode(keyUnpacked, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        logger.debug("prepareKey returns $keyUnpacked")
        return keyUnpacked
    }

    fun checksum(s: String): Int {
        val HEX_CHARS = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")
        var checksum = 0

        for (i in 0 until s.length) {
            checksum += s[i].toInt() % 16
        }
        val check = checksum % 16
        logger.debug(Integer.toHexString(check) + "<>" + HEX_CHARS[check])
        return check
    }

    fun preEscape(s: String): String {
        var _loc3 = String()

        for (_loc4 in 0 until s.length) {
            val _loc5 = s[_loc4]
            val _loc6 = s[_loc4].toInt()//s.charCodeAt(_loc4);
            if (_loc6 < 32 || _loc6 > 127 || _loc5 == '%' || _loc5 == '+') {
                try {
                    _loc3 = _loc3 + URLEncoder.encode("" + _loc5, "UTF-8")
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }

                continue
            } // end if
            _loc3 = _loc3 + _loc5
        } // end while
        return _loc3
    }

    fun d2h(d: Int): Int {
        var d = d
        if (d > 255) {
            d = 255
        } // end if
        return Math.floor((d / 16).toDouble()).toInt() + d % 16
    }

    fun cypherData(d: String, k: String, c: Int): String {
        var d = d
        var _loc5 = String()
        val _loc6 = k.length
        d = preEscape(d)

        for (_loc7 in 0 until d.length) {
            _loc5 += d2h(d[_loc7].toInt() xor k[(_loc7 + c) % _loc6].toInt())
        } // end while
        return _loc5
    }

    fun decypherData(mapData: String, key: String, checksum: Int): String {
        var mapDataDecrypted = String()
        val keyLen = key.length
        var cpt = 0
        logger.debug(mapData)
        logger.debug(mapData.length)
        var i = 0
        while (i < mapData.length) {
            val mapDataByte = Integer.parseInt(mapData.substring(i, i + 2), 16)
            mapDataDecrypted += (mapDataByte xor key[(cpt++ + checksum) % keyLen].toInt()).toChar()
            i += 2
        }
        try {
            return URLDecoder.decode(mapDataDecrypted, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return mapDataDecrypted
        }

    }

    fun cryptPassword(key: String, password: String): String {
        val chArray = charArrayOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_')
        var str = "#1"
        for (i in 0 until password.length) {
            val ch = password[i]
            val ch2 = key[i]
            val num2 = ch.toInt() / 0x0010
            val num3 = ch.toInt() % 0x0010
            val index = (num2 + ch2.toInt()) % chArray.size
            val num5 = (num3 + ch2.toInt()) % chArray.size
            str = str + chArray[index] + chArray[num5]
        }
        return str
    }


    /**
     * @param sExtraData données cryptées venant du serveur
     * @return [0] ip en clair, [1] inconnu, [2] ticket
     */
    fun unCryptIp(sExtraData: String): Array<String> {
        val ipCryptee: String
        val port: String
        val ticket: String
        var ip = ""
        ipCryptee = sExtraData.substring(0, 8)
        port = sExtraData.substring(8, 11)
        ticket = sExtraData.substring(11)
        logger.debug("Port : $port")
        logger.debug("Ticket : $ticket")
        var ch1: Int
        var ch2: Int
        var ch: Int
        var i = 0
        while (i < 8) {
            val code_ascii = ipCryptee[i].toByte()
            ch1 = code_ascii - 48
            val code_ascii2 = ipCryptee[i + 1].toByte()
            ch2 = code_ascii2 - 48
            ch = ch1 and 15 shl 4 or (ch2 and 15)
            ip += ".$ch"
            i += 2
        } // end while
        ip = ip.substring(1)
        val iPort = decode64(port[0].toInt()) and 63 shl 12 or (decode64(port[1].toInt()) and 63 shl 6) or (decode64(port[2].toInt()) and 63)
        return arrayOf(ip, iPort.toString() + "", ticket)
    }

    /**
     * Cryptage de l'adresse IP du serveur
     * @param gameServer les infos du serveur en clair
     * @return la chaine à passer au client
     */
    fun cryptIp(gameServer: GameServer): String {
        val digits = gameServer.ipAddress.split("\\.".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        var ipCryptée = ""
        for (digit in digits) {
            val value = Integer.parseInt(digit)
            val hi = value shr 4 and 15
            val lo = value and 15
            ipCryptée += (hi + 48).toChar()
            ipCryptée += (lo + 48).toChar()
        }
        val port = gameServer.port
        ipCryptée += String.format("%c%c%c", encode64(port shr 12 and 63), encode64(port shr 6 and 63), encode64(port and 63))
        ipCryptée += gameServer.ticket
        return ipCryptée
    }

    fun isValidNetworkKey(sKey: String?): Boolean {
        return if (sKey == null || sKey.length == 0 || dofus.aks.Aks.checksum(sKey.substring(0, sKey.length - 1)) != sKey[sKey.length - 1].toInt() || dofus.aks.Aks.checksum(sKey.substring(1, sKey.length - 2)) != sKey[0].toInt()) {
            false
        } else true
    }

    @JvmStatic
    fun main(args: Array<String>) {
        logger.info(cryptPassword("axntvhrmzenpqwhimvbqcnxvfdqfgavd", "motdepass"))
        val extraData = "=5?87>;6ag7c976b51"
        val unCryptIp = unCryptIp(extraData)
        for (s in unCryptIp)
            logger.info("uncrypted  : $s")
        val crypted = cryptIp(GameServer("213.248.126.182", 443, "c976b51"))
        logger.info("Crypt and uncrypt are " + if (extraData == crypted) "OK" else "KO")
    }
}
