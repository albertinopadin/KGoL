package com.tinodev.kgol

import android.graphics.Color
import android.graphics.Point
import kotlin.random.Random

class Grid(xCells: Int, yCells: Int) {
    private val xCells: Int
    private val yCells: Int
    private var generation: UInt
    val cells: Array<Cell>

    init {
        this.xCells = xCells
        this.yCells = yCells
        this.generation = 0u
        this.cells = Array<Cell>(xCells*yCells) { i ->
            val x = i % xCells
            val y = (i / xCells).toInt()
            Cell(Point(x, y), Color.BLUE, false, CellGeometry.Square)
        }
        setNeighborsForAllGridCells()
    }

    fun setNeighborsForAllGridCells() {
        for (x in 0 until xCells) {
            for (y in 0 until yCells) {
                this.cells[x + y*xCells].neighbors = getNeighborsForCell(x, y)
            }
        }
    }

    fun getNeighborsForCell(x: Int, y: Int): Array<Cell> {
        var neighbors = ArrayList<Cell>()

        val leftX   = x - 1
        val rightX  = x + 1
        val topY    = y + 1
        val bottomY = y - 1

        if (leftX > -1) {
            neighbors.add(this.cells[leftX + y*this.xCells])
        }

        if (leftX > -1 && topY < this.yCells) {
            neighbors.add(this.cells[leftX + topY*this.xCells])
        }

        if (topY < this.yCells) {
            neighbors.add(this.cells[x + topY*this.xCells])
        }

        if (rightX < this.xCells && topY < this.yCells) {
            neighbors.add(this.cells[rightX + topY*this.xCells])
        }

        if (rightX < this.xCells) {
            neighbors.add(this.cells[rightX + y*this.xCells])
        }

        if (rightX < this.xCells && bottomY > -1) {
            neighbors.add(this.cells[rightX + bottomY*this.xCells])
        }

        if (bottomY > -1) {
            neighbors.add(this.cells[x + bottomY*this.xCells])
        }

        if (leftX > -1 && bottomY > -1) {
            neighbors.add(this.cells[leftX + bottomY * this.xCells])
        }

        return neighbors.toTypedArray()
    }

    fun update(): UInt {
        for (x in 0 until xCells) {
            for (y in 0 until yCells) {
                this.cells[x + y*xCells].prepareUpdate()
            }
        }

        for (x in 0 until xCells) {
            for (y in 0 until yCells) {
                this.cells[x + y*xCells].update()
            }
        }

        ++this.generation
        return generation
    }

    fun reset() {
        for (x in 0 until xCells) {
            for (y in 0 until yCells) {
                this.cells[x + y*xCells].makeDead()
            }
        }

        this.generation = 0u
    }

    fun makeAllLive() {
        for (x in 0 until xCells) {
            for (y in 0 until yCells) {
                this.cells[x + y*xCells].makeLive()
            }
        }
    }

    fun randomState(liveProbability: Float) {
        for (x in 0 until xCells) {
            for (y in 0 until yCells) {
                if (Random.nextFloat() <= liveProbability) {
                    this.cells[x + y*xCells].makeLive()
                }
            }
        }
    }
}