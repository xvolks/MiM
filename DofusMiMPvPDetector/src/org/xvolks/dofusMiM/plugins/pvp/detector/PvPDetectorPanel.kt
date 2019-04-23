package org.xvolks.dofusMiM.plugins.pvp.detector

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.EventQueue

import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTable
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.DefaultTableModel

class PvPDetectorPanel : JPanel() {
    private val title = JLabel("Carte inconnue")
    val model: DefaultTableModel
    private val table: JTable
    private val renderer: DefaultTableCellRenderer

    init {
        layout = BorderLayout()

        renderer = object : DefaultTableCellRenderer() {
            private val serialVersionUID = -5386123714647881658L

            override fun getTableCellRendererComponent(table: JTable?, value: Any, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int): Component {
                val c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
                val alignementCellValue = table!!.getValueAt(row, 4).toString().toLowerCase()
                if (alignementCellValue.indexOf("ange") != -1) {
                    c.background = Color.white
                } else if (alignementCellValue.indexOf("d�mon") != -1) {
                    c.background = Color.red
                } else if (alignementCellValue.indexOf("neutre") != -1) {
                    c.background = Color.green.brighter()
                } else {
                    c.background = Color.gray.brighter()
                }
                return c
            }
        }
        table = JTable()
        model = object : DefaultTableModel() {
            private val serialVersionUID = 1L

            override fun fireTableStructureChanged() {
                super.fireTableStructureChanged()
                for (i in 0 until table.columnCount) {
                    table.columnModel.getColumn(i).cellRenderer = renderer
                }
            }
        }
        model.addColumn("ID")
        model.addColumn("Nom")
        model.addColumn("Level")
        model.addColumn("Race")
        model.addColumn("Alignement")
        model.addColumn("Grade")

        model.addRow(arrayOf("0", "Vide", "-1", "-", "Neutre", "-"))

        table.model = model

        add(title, BorderLayout.NORTH)
        add(table, BorderLayout.CENTER)

        table.tableHeader.isVisible = true
        model.fireTableStructureChanged()
        model.fireTableDataChanged()
    }

    fun setTitle(title: String) {
        EventQueue.invokeLater { this@PvPDetectorPanel.title.text = title }
    }

    companion object {
        private val serialVersionUID = -2438811513726575154L
    }
}
