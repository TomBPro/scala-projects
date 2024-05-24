package wator

import scalafx.scene.paint.Color
import scalafx.scene.shape.{Line, Rectangle}

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
