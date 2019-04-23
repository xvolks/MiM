package org.xvolks.dofus

import java.awt.*
import java.awt.TrayIcon.MessageType
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.File
import java.io.FileFilter
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.net.BindException
import java.net.ServerSocket
import java.net.Socket
import java.util.ArrayList
import java.util.prefs.Preferences

import javax.net.ServerSocketFactory
import javax.swing.ImageIcon
import javax.swing.JOptionPane

import org.apache.log4j.Logger
import org.ini4j.Ini
import org.ini4j.IniPreferences
import org.xvolks.dofus.events.GUIListener
import org.xvolks.dofus.events.TrayListener
import org.xvolks.dofus.gui.DockableFrame
import org.xvolks.dofus.injection.PluginManager
import org.xvolks.dofus.injection.impl.PluginManagerImpl
import org.xvolks.dofus.util.SearchEndedException
import org.xvolks.dofus.util.Toolkit
import org.xvolks.dofus.util.icons.Logo

import sun.misc.BASE64Decoder
import dofus.aks.GameServer
import org.xvolks.dofus.Main.ini

object Main {
    val XVOLKS_BOT = "Xvolks' bot"
    private val ANKLANGSO_SOL = "ANKLANGSO.sol"
    private val XVOLKS_CONFIG_INI = "xvolks.config.ini"
    private val _213_248_126_180 = "213.248.126.180" // 34.251.172.139
    internal var flashCacheDirectory: File? = null
    internal var logger = Logger.getLogger(Main::class.java)
    private var patched: Boolean = false
    private var mainFrame: DockableFrame? = null
    val pluginManager: PluginManager = PluginManagerImpl
    private lateinit var ini: Ini
    private var configPreferences: IniPreferences? = null
    private lateinit var trayListener: GUIListener
    private lateinit var trayIcon: TrayIcon

    val mainIcon: Image?
        get() {
            val decoder = BASE64Decoder()
            var image: Image? = null
            try {
                image = ImageIcon(decoder.decodeBuffer(Logo.logoDofusXvolks)).image
            } catch (e: IOException) {
                logger.error(e, e)
            }

            return image
        }

    enum class IniSections private constructor(val sectionName: String) {
        PATH("Path")

    }

    /**
     * @param args
     * @throws IOException
     */
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        logger.debug("Démarrage de l'application")
        var ret = JOptionPane.showConfirmDialog(null, "Arretez le client dofus officiel, puis cliquez Oui\nSi vous ne vous êtes jamais connecté à Dofus sur ce PC,\nRépondez Non, connectez-vous, puis relancez le bot !\n\nContinuer ?", "Votre choix, maître...", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
        if (ret != JOptionPane.YES_OPTION) return
        installTrayIcon()
        trayListener = TrayListener(trayIcon)
        try {
            pluginManager.loadPlugins(trayListener)
        } catch (e: Exception) {
            logger.error(e, e)
            trayIcon.displayMessage("Erreur...", "Erreur lors du chargement des plugins\n$e", MessageType.ERROR)
            Toolkit.sleepSeconds(4, 5)
            System.exit(0)
        }

        pluginManager.proxyStarting(pluginManager)
        trayIcon.displayMessage("Info", "Recherche du cache de flash", MessageType.INFO)
        val config = File(XVOLKS_CONFIG_INI)
        if (config.exists()) {
            val target = getConfigPreferences()!!.node(IniSections.PATH.sectionName).get(ANKLANGSO_SOL, null)
            if (target != null) {
                flashCacheDirectory = File(target).parentFile
            }
        }
        if (flashCacheDirectory == null) {
            var home = System.getProperty("user.home")
            if (File(home, "AppData").exists()) {
                home += "/AppData"
            }
            Toolkit.listDir(File(home), object : FileFilter {
                internal var count = 0
                override fun accept(pathname: File): Boolean {
                    count++
                    if (count % 1000 == 0) {
                        trayIcon.displayMessage("", "Scan de $count fichiers...", MessageType.INFO)
                    }
                    logger.debug(pathname)
                    val directory = pathname.isDirectory
                    if (directory && pathname.name.equals("#SharedObjects", ignoreCase = true)) {
                        //Vérifie que l'on est bien au bon endroit
                        if (pathname.parentFile.name.equals("Flash Player", ignoreCase = true)) {
                            flashCacheDirectory = pathname
                            throw SearchEndedException()
                        }
                    }
                    return directory
                }
            })
        }
        if (flashCacheDirectory == null) {
            ret = JOptionPane.showConfirmDialog(null, "Impossible de trouver le répertoire cache de Flash Player !\nVous devez faire en sorte que le client se connecte à 127.0.0.1, vous même.\n\nVoulez-vous continuer ?", "Erreur", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)
            if (ret != JOptionPane.YES_OPTION) {
                System.exit(0)
            }
            logger.debug("L'utilisateur à choisi de faire le paramétrage tout seul")
        } else {
            trayIcon!!.displayMessage("", "Repertoire trouvé, patching...", MessageType.INFO)
        }
        patched = false
        searchAndModifyCache()
        if (!patched) {
            ret = JOptionPane.showConfirmDialog(null, "Impossible de patcher le fichier cache. L'est-il déjà ?\n\nContinuer ?", "Attention", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)
            if (ret != JOptionPane.YES_OPTION) {
                System.exit(0)
            }
        }
        val mainFrame = DockableFrame(XVOLKS_BOT)
        this.mainFrame = mainFrame
        val listeners = ArrayList<GUIListener>()
        listeners.add(trayListener)
        listeners.add(mainFrame)

        pluginManager.addGUIListener(trayListener)
        pluginManager.addGUIListener(mainFrame)
        pluginManager.trafficListener

        trayIcon.displayMessage("Info", "Lancement du proxy, vous pouvez connecter Dofus dessus", MessageType.INFO)
        pluginManager.proxyReady()

        mainFrame.isVisible = true
        try {
            while (true) {
                runProxy(GameServer(_213_248_126_180, 443), null, listeners)
            }
        } catch (e: BindException) {
            trayIcon.displayMessage("Erreur", "Lancement du proxy, impossible le port 443 est déjà occupé (Skype, Serveur Web ?)", MessageType.ERROR)
        }

    }


    private fun installTrayIcon() {
        if (SystemTray.isSupported()) {
            val tray = SystemTray.getSystemTray()
            val image = mainIcon
            val popup = PopupMenu()
            val defaultItem = MenuItem("Sortie")
            defaultItem.addActionListener {
                JOptionPane.showMessageDialog(null, "Fermez le client Dofus avant sinon les résultats sont imprévisibles !")
                System.exit(0)
            }
            val showWindow = MenuItem("Voir fenêtre")
            showWindow.addActionListener { mainFrame!!.isVisible = true }

            trayIcon = TrayIcon(image!!, XVOLKS_BOT)
            trayIcon.popupMenu = popup
            trayIcon.popupMenu.add(defaultItem)
            trayIcon.popupMenu.add(showWindow)
            trayIcon.popupMenu.add("---")
            trayIcon.isImageAutoSize = true
            try {
                tray.add(trayIcon)
            } catch (e: AWTException) {
                System.err.println("TrayIcon could not be added.")
            }

        } else {
            JOptionPane.showMessageDialog(null, "Impossible d'initaliser l'icône du tray.", "Au revoir...", JOptionPane.ERROR_MESSAGE)
            System.exit(0)
        }
    }

    private fun searchAndModifyCache() {
        trayIcon.displayMessage("Info", "Recherche dans le cache de flash", MessageType.INFO)

        flashCacheDirectory?.listFiles { pathname ->
            logger.debug(pathname)
            if (pathname.name.equals(ANKLANGSO_SOL, ignoreCase = true)) {
                var raf: RandomAccessFile? = null
                try {
                    var node: Preferences? = getConfigPreferences().node(IniSections.PATH.sectionName)
                    if (node == null) {
                        configPreferences!!.put(IniSections.PATH.sectionName, "")
                        node = configPreferences!!.node(IniSections.PATH.sectionName)
                    }
                    node!!.put(ANKLANGSO_SOL, pathname.absolutePath)
                    saveConfigToINI()
                    var size = pathname.length().toInt()
                    val buffer = ByteArray(size)
                    raf = RandomAccessFile(pathname, "rw")
                    var totalRead = 0
                    while (size > 0) {
                        val read = raf.read(buffer, totalRead, size - totalRead)
                        if (read == -1) {
                            //EOF
                            break
                        } else {
                            totalRead += read
                            size -= read
                        }
                    }
                    var pos = 0
                    var state = 0
                    for (b in buffer) {
                        when (state) {
                            0, 4, 9 -> if (b == '2'.toByte()) state++ else state = 0
                            1, 8, 12 -> if (b == '1'.toByte()) state++ else state = 0
                            2 -> if (b == '3'.toByte()) state++ else state = 0
                            3, 7, 11 -> if (b == '.'.toByte()) state++ else state = 0
                            5 -> if (b == '4'.toByte()) state++ else state = 0
                            6, 13 -> if (b == '8'.toByte()) state++ else state = 0
                            10 -> if (b == '6'.toByte()) state++ else state = 0
                            14 -> {
                                if (b == '0'.toByte()) state++ else state = 0

                                val realPos = pos + 1 - _213_248_126_180.length
                                logger.debug("position : $realPos")
                                raf.seek(realPos.toLong())
                                raf.write("127.000.000.001".toByteArray())
                                raf.close()
                                Runtime.getRuntime().addShutdownHook(object : Thread() {
                                    override fun run() {
                                        try {
                                            pluginManager.proxyStopped()
                                        } catch (e: Exception) {
                                            logger.error(e, e)
                                        }

                                        try {
                                            val raf = RandomAccessFile(pathname, "rw")
                                            raf.seek(realPos.toLong())
                                            raf.write(_213_248_126_180.toByteArray())
                                        } catch (e: IOException) {
                                            logger.error(e, e)
                                        }

                                    }
                                })
                                patched = true
                                throw SearchEndedException()
                            }
                            else -> state = 0
                        }
                        pos++
                    }

                } catch (e: IOException) {
                    logger.error(e, e)
                } finally {
                    if (raf != null) {
                        try {
                            raf.close()
                        } catch (e: IOException) {
                            logger.error(e, e)
                        }

                    }
                }
            }
            pathname.isDirectory
        }

    }

    @Throws(IOException::class)
    fun runProxy(realGameServer: GameServer, fakeGameServer: GameServer?, guiListeners: List<GUIListener>) {
        var ss: ServerSocket? = null
        if (fakeGameServer == null) {
            Logger.getLogger(Main::class.java).debug("Démarrage de l'écouteur de login")
            ss = ServerSocketFactory.getDefault().createServerSocket(realGameServer.port)
        } else {
            trayIcon.displayMessage("", "Connexion au jeu...", MessageType.INFO)
            Logger.getLogger(Main::class.java).debug(String.format("Démarrage du proxy entre %s:%d et %s:%d", realGameServer.ipAddress, realGameServer.port, fakeGameServer.ipAddress, fakeGameServer.port))
            ss = ServerSocketFactory.getDefault().createServerSocket(fakeGameServer.port)
        }
        val socket = ss!!.accept()
        val com = Communication(socket, realGameServer, fakeGameServer, guiListeners, pluginManager)
        ss.close()
        com.join()
        Toolkit.sleepSeconds(5, 10)
    }

    @Throws(IOException::class)
    private fun loadConfigFromINI(config: File): IniPreferences {
        try {
            ini = Ini(File(XVOLKS_CONFIG_INI))
            return IniPreferences(ini)
        } catch (e: FileNotFoundException) {
            //First launch file was not created at install time !?
            FileOutputStream(XVOLKS_CONFIG_INI).close()
            return loadConfigFromINI(config)
        }

    }

    @Synchronized
    @Throws(IOException::class)
    fun saveConfigToINI() {
        ini.store()
    }

    fun getConfigPreferences(): IniPreferences {
        if (configPreferences == null) {
            try {
                val configPreferences = loadConfigFromINI(File(XVOLKS_CONFIG_INI))
                this.configPreferences = configPreferences
                return configPreferences
            } catch (e: IOException) {
                logger.error(e, e)
                throw e
            }
        } else {
            return configPreferences!!
        }
    }
}
