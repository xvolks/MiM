package org.xvolks.dofus.util

import java.awt.Component
import java.awt.Dimension
import java.io.File
import java.io.FileFilter
import java.util.ArrayList
import java.util.Random

object Toolkit {
    /**
     * Sleep for a duration between `min` and `max` milliseconds
     * @param min
     * @param max
     */
    fun sleepMilliSeconds(min: Int, max: Int) {
        var min = min
        var max = max
        if (min > max) {
            val i = max
            max = min
            min = i
        }
        try {
            if (min == max) {
                Thread.sleep(min.toLong())
            } else {
                Thread.sleep((Random().nextInt(max - min) + min).toLong())
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    /**
     * Sleep for a duration between `min` and `max` seconds
     * @param min
     * @param max
     */
    fun sleepSeconds(min: Int, max: Int) {
        sleepMilliSeconds(min * 1000, max * 1000)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        JPopupWindow.showMessage("Waiting for few time", 3000)
        sleepMilliSeconds(100, 500)
    }

    /**
     * Recursively search in a directory
     * @param dir the root directory to search
     * @param filter a FileFilter that will be notified for each found file
     * @return a list of accepted files (does not returns the directories entry)
     */
    fun listDir(dir: File, filter: FileFilter): List<File> {
        val fileList = ArrayList<File>()
        try {
            val files = dir.listFiles()
            if (files != null) {
                for (f in files) {
                    if (f.isDirectory) {
                        if (filter.accept(f)) {
                            fileList.addAll(listDir(f, filter))
                        }
                    } else {
                        if (filter.accept(f)) {
                            fileList.add(f)
                        }
                    }
                }
            }
        } catch (e: SearchEndedException) {
            //The accept method thrown this to cause the search end
        }

        return fileList
    }

    fun centerOnScreen(c: Component) {
        val screen = java.awt.Toolkit.getDefaultToolkit().screenSize
        val component = c.size
        c.setLocation((screen.width - component.width) / 2,
                (screen.height - component.height) / 2)
    }

    fun toBoolean(value: String?): Boolean {
        if (value == null || value.isEmpty()) {
            return false
        }
        try {
            return 0 != Integer.parseInt(value)
        } catch (e: Exception) {
            return false
        }

    }
}
