package com.tinodev.kgol

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.lang.Exception

class GameView(context: Context, attributes: AttributeSet):
    SurfaceView(context, attributes), SurfaceHolder.Callback {
    private val thread: GameThread
    private val gridSize = 50
    private val cellSize = 26f
    private val cellSpacing = 2f

    init {
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

        for (x in 0..gridSize) {
            for (y in 0..gridSize) {
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