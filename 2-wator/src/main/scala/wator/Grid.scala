package wator

import scalafx.collections.ObservableBuffer
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

sealed trait CellType
case object Empty extends CellType
case class Occupied(entity: Entity) extends CellType

object Grid {
  type Coordinates = (Int, Int)
  
  def createEmptyGrid(width: Int, height: Int): Array[Array[CellType]] = {
    Array.fill[CellType](width, height)(Empty)
  }

  def copyGrid(grid: Array[Array[CellType]]): Array[Array[CellType]] = {
    grid.map(_.clone())
  }

  def updateGrid(grid: Array[Array[CellType]], entities: Seq[Entity]): Array[Array[CellType]] = {
    val newGrid = createEmptyGrid(grid.length, grid(0).length)
    entities.foldLeft(newGrid) {
      case (updatedGrid, entity) =>
        updatedGrid(entity.x)(entity.y) = Occupied(entity)
        updatedGrid
    }
  }

  def getNeighbors(x: Int, y: Int, width: Int, height: Int): Seq[Coordinates] = {
    Seq(
      (x - 1, y), (x + 1, y), (x, y - 1), (x, y + 1),
      (x - 1, y - 1), (x + 1, y + 1), (x - 1, y + 1), (x + 1, y - 1)
    ).filter { case (nx, ny) =>
      nx >= 0 && nx < width && ny >= 0 && ny < height
    }
  }

  def createGridShapes(grid: Array[Array[CellType]], cellSize: Double): ObservableBuffer[Rectangle] = {
    ObservableBuffer((for {
      (row, rowIndex) <- grid.zipWithIndex
      (cell, colIndex) <- row.zipWithIndex
      if cell != Empty
    } yield {
      val entity = cell match {
        case Occupied(ent) => ent
        case _ => throw new IllegalStateException("This should never happen.")
      }
      new Rectangle {
        x = colIndex * cellSize
        y = rowIndex * cellSize
        width = cellSize
        height = cellSize
        fill = entity match {
          case _: Tuna  => Color.Green
          case _: Shark => Color.Red
        }
      }
    }).toSeq: _*)
  }

  def cellIsEmpty(x: Int, y: Int, grid: Array[Array[CellType]]): Boolean = grid(x)(y) == Empty

  def cellIsOccupied(x: Int, y: Int, grid: Array[Array[CellType]]): Boolean = grid(x)(y) match {
    case Occupied(_) => true
    case _           => false
  }
}