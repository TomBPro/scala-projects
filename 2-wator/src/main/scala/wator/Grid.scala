package wator

import scalafx.scene.paint.Color
import scalafx.scene.shape.Line
import scalafx.scene.shape.Rectangle

sealed trait CellType {
  def x: Int
  def y: Int
}

case class Empty(x: Int, y: Int) extends CellType
case class Tuna(x: Int, y: Int) extends CellType
case class Shark(x: Int, y: Int, energy: Int, breedCycle: Int) extends CellType

object Grid {
  def createGridLines(gridWidth: Int, gridHeight: Int, cellSize: Double): Seq[Line] = {
    val horizontalLines = (0 to gridHeight).map { y =>
      new Line {
        startX = 0
        startY = y * cellSize
        endX = gridWidth * cellSize
        endY = y * cellSize
        stroke = Color.Black
      }
    }

    val verticalLines = (0 to gridWidth).map { x =>
      new Line {
        startX = x * cellSize
        startY = 0
        endX = x * cellSize
        endY = gridHeight * cellSize
        stroke = Color.Black
      }
    }

    horizontalLines ++ verticalLines
  }
}
