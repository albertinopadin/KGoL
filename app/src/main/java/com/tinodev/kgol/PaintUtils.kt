package com.tinodev.kgol

import android.graphics.Color
import android.graphics.Paint

class PaintUtils {
    companion object {
        val blueFillPaint  = createFillPaint(Color.BLUE)
        val redFillPaint   = createFillPaint(Color.RED)
        val greenFillPaint = createFillPaint(Color.GREEN)

        val rgbPaints = listOf<Paint>(redFillPaint, greenFillPaint, blueFillPaint)

        fun createPaint(pColor: Int, pStyle: Paint.Style): Paint {
            return Paint().apply {
                color = pColor
                style = pStyle
            }
        }

        fun createFillPaint(pColor: Int): Paint {
            return createPaint(pColor, Paint.Style.FILL)
        }
    }
}