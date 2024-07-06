package wator

import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scala.util.Random

sealed trait CellType
case object Empty extends CellType
case class Occupied(entity: Entity) extends CellType

trait PositionDelegate {
  def x: Int
  def y: Int
}

object Grid {
  def createEmptyGrid(width: Int, height: Int): Array[Array[CellType]] = {
    Array.fill[CellType](width, height)(Empty)
  }

  def updateGrid(grid: Array[Array[CellType]], entities: Seq[Entity]): Unit = {
    // First, clear the grid
    for (x <- grid.indices; y <- grid(x).indices)
      grid(x)(y) = Empty
    // Then populate the grid with entities
    entities.foreach {
      case entity: Entity =>
        grid(entity.x)(entity.y) = Occupied(entity)
    }
  }

  def getNeighbors(x: Int, y: Int, width: Int, height: Int): Seq[(Int, Int)] = {
    val possibleNeighbors = Seq(
      (x - 1, y), (x + 1, y), (x, y - 1), (x, y + 1), // cardinal directions
      (x - 1, y - 1), (x + 1, y + 1), (x - 1, y + 1), (x + 1, y - 1) // diagonal directions
    )
    possibleNeighbors.filter { case (nx, ny) =>
      nx >= 0 && nx < width && ny >= 0 && ny < height
    }
  }

  def getRandomEmptyPosition(grid: Array[Array[CellType]], width: Int, height: Int): (Int, Int) = {
    val random = new Random()
    var found = false
    var x = 0
    var y = 0
    while (!found) {
      x = random.nextInt(width)
      y = random.nextInt(height)
      if (grid(x)(y) == Empty) {
        found = true
      }
    }
    (x, y)
  }

  def createGridShapes(grid: Array[Array[CellType]], cellSize: Double): Seq[Rectangle] = {
    for {
      currentX <- grid.indices
      currentY <- grid(currentX).indices
      if grid(currentX)(currentY) != Empty
    } yield {
      val entity = grid(currentX)(currentY) match {
        case Occupied(ent) => ent
        case _ => throw new IllegalStateException("This should never happen.")
      }
      new Rectangle {
        this.x = currentX * cellSize
        this.y = currentY * cellSize
        width = cellSize
        height = cellSize
        fill = entity match {
          case _: Tuna => Color.Green
          case _: Shark => Color.Red
        }
      }
    }
  }

  def cellIsEmpty(x: Int, y: Int, grid: Array[Array[CellType]]): Boolean = grid(x)(y) == Empty
}
