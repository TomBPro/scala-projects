package battleship.ui

import battleship.model.{Cell, Orientation}
import scalafx.scene.layout.GridPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

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
      for (i <- 0 until shipSize) {
        val (r, c) = orientation match {
          case Orientation.Horizontal => (rowIndex, colIndex + i)
          case Orientation.Vertical   => (rowIndex + i, colIndex)
        }
        playerBoard(r)(c) = Cell.Ship
      }
      placedShips = placedShips - ((rowIndex, colIndex, orientation)) // Remove ship from list
      boardPane.foreach(_.getChildren.clear())
      updateUI(boardPane.getOrElse(new GridPane())) // Ensure we have a GridPane to update
    }
  }

  private def canPlaceShip(rowIndex: Int, colIndex: Int, shipSize: Int, orientation: Orientation): Boolean = {
    val isWithinBounds = (rowIndex >= 0 && colIndex >= 0 &&
                          rowIndex + (if (orientation == Orientation.Vertical) shipSize - 1 else 0) < playerBoard.length &&
                          colIndex + (if (orientation == Orientation.Horizontal) shipSize - 1 else 0) < playerBoard(0).length)
    
    val isNotOverlapping = (for (i <- 0 until shipSize) yield {
      val (r, c) = orientation match {
        case Orientation.Horizontal => (rowIndex, colIndex + i)
        case Orientation.Vertical   => (rowIndex + i, colIndex)
      }
      if (playerBoard(r)(c) != Cell.Empty) false else true
    }).forall(identity)

    isWithinBounds && isNotOverlapping
  }
}