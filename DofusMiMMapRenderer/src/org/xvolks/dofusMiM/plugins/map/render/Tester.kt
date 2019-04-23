package org.xvolks.dofusMiM.plugins.map.render

import javax.swing.JFrame

import ank.battlefield.MapManager

object Tester {


    @JvmStatic
    fun main(args: Array<String>) {
        val f = JFrame("MapRenderer Tester")
        f.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        val model = MapRendererModel()
        f.contentPane.add(MapRendererPanel(model))

        model.data = MapManager.testGetMapData()
        f.pack()
        f.isVisible = true
    }

}
