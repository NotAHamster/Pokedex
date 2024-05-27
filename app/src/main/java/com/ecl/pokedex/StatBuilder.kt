package com.ecl.pokedex

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText

class StatBuilder(private val textMeasurer: TextMeasurer, private val textStyle: TextStyle) {
    private val columnWidths: Array<Int> = Array(3) { 0 }
    private val rows: MutableList<RowData> = mutableListOf()
    private var textHeight: Int = 0
    private val padding = 20f
    private val paddingY = 20f
    private val maxBaseStat = 255

    fun addRow(text: String, baseStat: Int) {
        val col1 = textMeasurer.measure(text, textStyle).size
        textHeight = col1.height
        if (columnWidths[0] < col1.width)
            columnWidths[0] = col1.width
        val col2 = textMeasurer.measure(baseStat.toString(), textStyle).size.width
        if (columnWidths[1] < col2)
            columnWidths[1] = col2
        rows.add(RowData(text, baseStat, col1.width, col2))
    }

    fun drawRows(drawScope: DrawScope) {
        drawScope.apply {
            val barOffsetX = padding * 3 + columnWidths[0] + columnWidths[1]
            val barSize = Size(size.width - barOffsetX - 40f, 25f)
            val barCenter = (textHeight / 2) - (barSize.height / 2)
            val rowOffset = paddingY + textHeight

            for (i in 0..rows.lastIndex) {
                val loopPaddingY = rowOffset * i + paddingY

                val textOffsetX = columnWidths[0] - rows[i].textWidth
                drawText(
                    textMeasurer,
                    rows[i].text,
                    topLeft = Offset(padding + textOffsetX, loopPaddingY),
                    style = textStyle
                )
                val statOffsetX = columnWidths[1] - rows[i].statWidth
                drawText(
                    textMeasurer,
                    rows[i].baseStat.toString(),
                    topLeft = Offset(padding * 1.5f + columnWidths[0] + 10f + statOffsetX, loopPaddingY),
                    style = textStyle
                )
                val barWidthMul = rows[i].baseStat.toFloat() / maxBaseStat
                val loopBarSize = barSize.copy(barSize.width * barWidthMul)
                drawRoundRect(
                    color = lerp(Color.Red, Color.Green, barWidthMul),
                    topLeft = Offset(barOffsetX, barCenter + loopPaddingY),
                    size = loopBarSize,
                    cornerRadius = CornerRadius(12.5f,12.5f)
                )
                //lerp(Color.Red, Color.Green, barWidthMul)
            }
        }
    }

    private data class RowData(val text: String, val baseStat: Int, val textWidth: Int, val statWidth: Int)
}