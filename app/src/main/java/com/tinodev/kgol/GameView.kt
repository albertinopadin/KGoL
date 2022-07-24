package com.tinodev.kgol

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.lang.Exception

class GameView(context: Context, attributes: AttributeSet):
    SurfaceView(context, attributes), SurfaceHolder.Callback {
    private val thread: GameThread
    private val cellSize = 28f
    private val cellSpacing = 2f
    private val cellTotalExtent = cellSize + cellSpacing
    private val cellGridWidth: Int
    private val cellGridHeight: Int

    init {
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        println("Screen dimensions: $screenWidth x $screenHeight")
        val cellGridWidthF = Resources.getSystem().displayMetrics.widthPixels / cellTotalExtent
        val cellGridHeightF = Resources.getSystem().displayMetrics.heightPixels / cellTotalExtent
        println("Raw (float) grid dimensions: $cellGridWidthF x $cellGridHeightF")
        cellGridWidth = (cellGridWidthF).toInt()
        cellGridHeight = (cellGridHeightF).toInt()
        println("Grid dimensions: $cellGridWidth x $cellGridHeight")
        holder.addCallback(this)
        thread = GameThread(holder, this)
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        thread.setRunning(true)
        thread.start()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        println("Surface changed!")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        var retry = true
        while (retry) {
            try {
                thread.setRunning(false)
                thread.join()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            retry = false
        }
    }

    fun update() {

    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

//        canvas!!.drawCircle(400.0f, 600.0f, 100f, PaintUtils.blueFillPaint)

        for (x in 0..cellGridWidth) {
            for (y in 0..cellGridHeight) {
                val cellRect = RectF(
                    (x * cellSize) + cellSpacing,
                    (y * cellSize) + cellSpacing,
                    (x * cellSize) + cellSize,
                    (y * cellSize) + cellSize)
                canvas!!.drawRect(cellRect, PaintUtils.rgbPaints.random())
            }
        }
    }
}