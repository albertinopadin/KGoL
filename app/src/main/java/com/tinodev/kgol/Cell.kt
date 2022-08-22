package com.tinodev.kgol

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point

enum class CellGeometry {
    Square, Circle
}

class Cell(position: Point, color: Int, alive: Boolean, geometry: CellGeometry) {
    var alive: Boolean
    var color: Int
    var paint: Paint
    val position: Point
    val geometry: CellGeometry
    var neighbors: Array<Cell>
    private var currentState: Boolean
    private var nextState: Boolean

    init {
        this.alive = alive
        this.color = color
        this.paint = PaintUtils.createFillPaint(color)
        this.position = position
        this.geometry = geometry
        this.currentState = alive
        this.nextState = this.currentState
        this.neighbors = arrayOf<Cell>()
        if (alive) {
            makeLive()
        } else {
            makeDead()
        }
    }

    fun makeLive() {
        setState(true)
        this.paint = PaintUtils.createFillPaint(this.color)
    }

    fun makeDead() {
        setState(false)
        this.paint = PaintUtils.createFillPaint(Color.TRANSPARENT)
    }

    fun setState(live: Boolean) {
        this.currentState = live
        alive = live
        this.nextState = live
    }

    fun prepareUpdate() {
        val liveNeighbors = neighbors.count { cell -> cell.alive }
        if (!(!currentState && liveNeighbors < 3)) {
            nextState = (currentState && liveNeighbors == 2) || (liveNeighbors == 3)
        }
    }

    fun update() {
        if (needsUpdate()) {
            if (nextState) {
                makeLive()
            } else {
                makeDead()
            }
        }
    }

    fun needsUpdate(): Boolean {
        return nextState != currentState
    }
}