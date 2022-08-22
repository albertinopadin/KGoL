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
    private val cellTotalExtent: Float = cellSize + cellSpacing
    private val cellGridWidth: Int
    private val cellGridHeight: Int

    private val cellGridWidthAdjustment = 2
    private val cellGridHeightAdjustment = 6

    private val grid: Grid
    private val gridDimensions: Pair<Int, Int>
//    private val cellGridWidth: Int
//    private val cellGridHeight: Int

    init {
        this.gridDimensions = getCellGridDimensions()
        this.cellGridWidth = this.gridDimensions.first
        this.cellGridHeight = this.gridDimensions.second
        println("Grid dimensions: $cellGridWidth x $cellGridHeight")
        this.grid = Grid(cellGridWidth, cellGridHeight)
        this.grid.randomState(0.25f)
        holder.addCallback(this)
        thread = GameThread(holder, this)
    }

    private fun getCellGridDimensions(): Pair<Int, Int> {
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        println("Screen dimensions: $screenWidth x $screenHeight")
        val cellGridWidthF = Resources.getSystem().displayMetrics.widthPixels / cellTotalExtent
        val cellGridHeightF = Resources.getSystem().displayMetrics.heightPixels / cellTotalExtent
        println("Raw (float) grid dimensions: $cellGridWidthF x $cellGridHeightF")
        val cgWidth = (cellGridWidthF + cellGridWidthAdjustment).toInt()
        val cgHeight = (cellGridHeightF + cellGridHeightAdjustment).toInt()
        return Pair(cgWidth, cgHeight)
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

    private fun mDrawRandomPaintSquare(canvas: Canvas?, x: Int, y: Int) {
        val cellRect = RectF(
            (x * cellSize) + cellSpacing,
            (y * cellSize) + cellSpacing,
            (x * cellSize) + cellSize,
            (y * cellSize) + cellSize)
        canvas!!.drawRect(cellRect, PaintUtils.rgbPaints.random())
    }

    fun mDrawRandomPaintCircle(canvas: Canvas?, x: Int, y: Int) {
        canvas!!.drawCircle((x * cellSize) + cellSpacing,
            (y * cellSize) + cellSpacing,
            cellSize/2,
            PaintUtils.rgbPaints.random())
    }

    private fun mDrawSquare(canvas: Canvas?, paint: Paint, x: Int, y: Int) {
        val cellRect = RectF(
            (x * cellSize) + cellSpacing,
            (y * cellSize) + cellSpacing,
            (x * cellSize) + cellSize,
            (y * cellSize) + cellSize)
        canvas!!.drawRect(cellRect, paint)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

//        for (x in 0..cellGridWidth) {
//            for (y in 0..cellGridHeight) {
////                mDrawSquare(canvas, x, y)
//                mDrawCircle(canvas, x, y)
//            }
//        }

        this.grid.update()

        for (x in 0 until this.cellGridWidth) {
            for (y in 0 until this.cellGridHeight) {
                mDrawSquare(canvas, this.grid.cells[x + y*this.cellGridWidth].paint, x, y)
            }
        }
    }
}