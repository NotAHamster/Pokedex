package com.ecl.pokedex.helpers

import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue
import androidx.appcompat.R

object ColorUtils {

    /**
     * Blends two colors together.
     * @param color1 The first color.
     * @param color2 The second color.
     * @param ratio The ratio of blending, between 0 and 1.
     * @return The blended color.
     */
    fun blend(color1: Int, color2: Int, ratio: Float = 0.5f): Int {
        val inverseRatio = 1 - ratio
        val a = Color.alpha(color1) * inverseRatio + Color.alpha(color2) * ratio
        val r = Color.red(color1) * inverseRatio + Color.red(color2) * ratio
        val g = Color.green(color1) * inverseRatio + Color.green(color2) * ratio
        val b = Color.blue(color1) * inverseRatio + Color.blue(color2) * ratio
        return Color.argb(a.toInt(), r.toInt(), g.toInt(), b.toInt())
    }

    fun getColorPrimary(theme: Resources.Theme): Int {
        val tvBasecolor = TypedValue()
        theme.resolveAttribute(R.attr.colorPrimary, tvBasecolor, true)
        return tvBasecolor.data
    }

    fun getColorHighlight(theme: Resources.Theme): Int {
        val tvBasecolor = TypedValue()
        theme.resolveAttribute(com.ecl.pokedex.R.attr.colorHighlight, tvBasecolor, true)
        return tvBasecolor.data
    }

    fun getColorPrimaryHighlight(theme: Resources.Theme, ratio: Float = 0.5f): Int {
        return blend(getColorPrimary(theme), getColorHighlight(theme), ratio)
    }
}