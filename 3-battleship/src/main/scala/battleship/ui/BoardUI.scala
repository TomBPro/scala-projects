package battleship.ui

import battleship.model.Cell
import battleship.model.Orientation
import scalafx.scene.layout.GridPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

import scala.collection.mutable

class BoardUI(var playerBoard: Array[Array[Cell]], var placedShips: Set[(Int, Int, Orientation)], onShipPlaced: (Int, Int, Int, Orientation) => Unit) {

  private var boardPane: Option[GridPane] = None

  def updateUI(boardPane: GridPane): Unit = {
    this.boardPane = Some(boardPane)
    boardPane.getChildren.clear()
    val cellSize = 40
    for (row <- playerBoard.indices; col <- playerBoard(row).indices) {
      val cell = playerBoard(row)(col)
      val rect = new Rectangle {
        width = cellSize
        height = cellSize
        fill = cell match {
          case Cell.Empty => Color.White
          case Cell.Ship => Color.Blue
        }
        stroke = Color.Black
        strokeWidth = 1
      }
      boardPane.add(rect, col, row)
    }
  }

  def placeShip(rowIndex: Int, colIndex: Int, shipSize: Int, orientation: Orientation): Unit = {
    if (canPlaceShip(rowIndex, colIndex, shipSize, orientation)) {
      println(s"Placing ship at Row: $rowIndex, Column: $colIndex, Size: $shipSize, Orientation: $orientation")
      for (i <- 0 until shipSize) {
        val (r, c) = orientation match {
          case Orientation.Horizontal => (rowIndex, colIndex + i)
          case Orientation.Vertical   => (rowIndex + i, colIndex)
        }

        // Debug log to check indices
        println(s"Placing at Row: $r, Column: $c")

        if (r >= 0 && r < playerBoard.length && c >= 0 && c < playerBoard(r).length) {
          playerBoard(r)(c) = Cell.Ship
        } else {
          println(s"Error: Trying to place ship out of bounds at Row: $r, Column: $c")
        }
      }
      placedShips += ((rowIndex, colIndex, orientation))
      boardPane.foreach(_.getChildren.clear())
      updateUI(boardPane.getOrElse(new GridPane())) // Ensure we have a GridPane to update
    } else {
      println(s"Cannot place ship at Row: $rowIndex, Column: $colIndex, Size: $shipSize, Orientation: $orientation")
    }
  }

  private def canPlaceShip(rowIndex: Int, colIndex: Int, shipSize: Int, orientation: Orientation): Boolean = {
    def isWithinBounds(row: Int, col: Int, size: Int, dir: Orientation): Boolean = {
      dir match {
        case Orientation.Horizontal =>
          val fitsHorizontally = col + size <= playerBoard(0).length
          println(s"Checking horizontal bounds: Fits Horizontally = $fitsHorizontally")
          fitsHorizontally
        case Orientation.Vertical =>
          val fitsVertically = row + size <= playerBoard.length
          println(s"Checking vertical bounds: Fits Vertically = $fitsVertically")
          fitsVertically
      }
    }

    def isNotOverlapping(row: Int, col: Int, size: Int, dir: Orientation): Boolean = {
      val overlapIssues = mutable.ListBuffer[String]()

      val noOverlap = (for (i <- 0 until size) yield {
        val (r, c) = dir match {
          case Orientation.Horizontal => (row, col + i)
          case Orientation.Vertical   => (row + i, col)
        }

        if (r < 0 || r >= playerBoard.length || c < 0 || c >= playerBoard(r).length) {
          overlapIssues += s"Index out of bounds: ($r, $c)"
          false
        } else if (playerBoard(r)(c) != Cell.Empty) {
          overlapIssues += s"Cell occupied: ($r, $c)"
          false
        } else {
          true
        }
      }).forall(identity)

      if (!noOverlap) {
        println(s"Overlap issues: ${overlapIssues.mkString(", ")}")
      }

      noOverlap
    }

    val horizontalPossible = isWithinBounds(rowIndex, colIndex, shipSize, Orientation.Horizontal) &&
                              isNotOverlapping(rowIndex, colIndex, shipSize, Orientation.Horizontal)

    val verticalPossible = isWithinBounds(rowIndex, colIndex, shipSize, Orientation.Vertical) &&
                            isNotOverlapping(rowIndex, colIndex, shipSize, Orientation.Vertical)

    // Update debug logs for visibility
    if (horizontalPossible) {
      println(s"Ship can be placed horizontally.")
    } else {
      println(s"Ship cannot be placed horizontally.")
    }

    if (verticalPossible) {
      println(s"Ship can be placed vertically.")
    } else {
      println(s"Ship cannot be placed vertically.")
    }

    horizontalPossible || verticalPossible
  }
}
