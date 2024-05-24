package com.ecl.pokedex

import android.content.res.Resources.Theme
import android.graphics.Color
import android.util.TypedValue
import androidx.appcompat.R as compR
import com.ecl.pokedex.io.Network

object Globals {
    var themeID: Int = R.style.Base_Theme_Pokedex
    val network = Network()
}

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

    fun getColorPrimary(theme: Theme): Int {
        val tvBasecolor = TypedValue()
        theme.resolveAttribute(compR.attr.colorPrimary, tvBasecolor, true)
        return tvBasecolor.data
    }

    fun getColorHighlight(theme: Theme): Int {
        val tvBasecolor = TypedValue()
        theme.resolveAttribute(R.attr.colorHighlight, tvBasecolor, true)
        return tvBasecolor.data
    }

    fun getColorPrimaryHighlight(theme: Theme, ratio: Float = 0.5f): Int {
        return blend(getColorPrimary(theme), getColorHighlight(theme), ratio)
    }
}
