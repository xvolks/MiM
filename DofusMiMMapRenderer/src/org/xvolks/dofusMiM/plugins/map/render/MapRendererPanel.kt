package org.xvolks.dofusMiM.plugins.map.render

import ank.battlefield.MapManager
import dofus.datacenter.Cell
import dofus.datacenter.abstractCreatures.AbstractCreature
import org.apache.log4j.Logger
import org.xvolks.dofus.events.ModelListener
import org.xvolks.dofus.mvc.Model
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel

class MapRendererPanel(override val model: MapRendererModel) : JPanel(), ModelListener<Model<*>> {
    private var mapAsCells: List<Cell>? = null
    private val scale = 20

    init {
        model.addModelListener(this)
    }

    override fun modelUpdated() {
        val data = model.data ?: return
        var w = data.width
        var h = data.height
        w *= scale
        h *= scale
        preferredSize = Dimension(w, h)
        mapAsCells = MapManager.getMapAsCells(data)
        invalidate()
        repaint()
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        if (g != null && model != null && model.data != null) {
            val g2 = g as Graphics2D?
            val w = model.data!!.width
            val lScale = (width / w / 3).toDouble()
            val lScaleX = lScale * 2
            val lScaleY = lScale / 2
            var x = 0.0
            var y = 0.0
            val sqrt = Math.sqrt(2.0)
            val step = sqrt / 2
            var lineEven = true
            val offsetX = 1
            val offsetY = 2
            var cellNum = 0
            var cellOnLine = 0
            var line = 0
            var currentWidth = w
            for (cell in mapAsCells!!) {
                if (lineEven) {
                    x = sqrt * cellOnLine
                    y = line * sqrt
                } else {
                    x = (cellOnLine + .5) * sqrt
                    y = line * sqrt
                }
                x += offsetX.toDouble()
                y += offsetY.toDouble()

                g2!!.color = Color.white
                var x1 = x
                var y1 = y
                var x2 = x1
                var y2 = y1
                x1 -= step
                val stepY = step * 2
                y2 += stepY
                g2.drawLine(toInt(lScaleX, x1), toInt(lScaleY, y1), toInt(lScaleX, x2), toInt(lScaleY, y2))
                x1 = x2
                y1 = y2
                y2 -= stepY
                x2 += step
                g2.drawLine(toInt(lScaleX, x1), toInt(lScaleY, y1), toInt(lScaleX, x2), toInt(lScaleY, y2))
                x1 = x2
                y1 = y2
                y2 -= stepY
                x2 -= step
                g2.drawLine(toInt(lScaleX, x1), toInt(lScaleY, y1), toInt(lScaleX, x2), toInt(lScaleY, y2))
                x1 = x2
                y1 = y2
                y2 += stepY
                x2 -= step
                g2.drawLine(toInt(lScaleX, x1), toInt(lScaleY, y1), toInt(lScaleX, x2), toInt(lScaleY, y2))

                Logger.getLogger(MapRendererPanel::class.java).debug(String.format("cellNum=%d, x=%f, y=%f", cellNum, x, y))
                g2.color = Color.black
                var v = "E"
                when (cell.movement) {
                    0 -> v = "x"
                    2 -> {
                        g2.color = Color.red
                        v = "o"
                    }
                    4 -> v = "."
                    else -> {
                    }
                }
                var creature: AbstractCreature? = null
                for (c in model.creatures) {
                    if (c.cell == cellNum) {
                        creature = c
                        break
                    }
                }
                if (creature != null) {
                    g2.color = Color.green
                    v = creature.creatureName.substring(0, Math.min(3, creature.creatureName.length))
                }
                x *= lScaleX
                y *= lScaleY
                g2.drawString(/*cell.getMovement()+*/v/*+cellNum*/, x.toInt(), y.toInt())
                cellNum++
                cellOnLine++

                if (cellOnLine >= currentWidth) {
                    cellOnLine = 0
                    lineEven = !lineEven
                    if (lineEven) currentWidth = w else currentWidth = w - 1
                    line++
                }
            }
        }
    }


    private fun toInt(scale: Double, value: Double): Int {
        return (value * scale).toInt()
    }

    companion object {
        private val serialVersionUID = 7312377247990038756L
    }
}
