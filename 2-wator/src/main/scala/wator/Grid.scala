// Grid.scala
package wator

import scala.util.Random

object Grid {

  type Cell = Option[Entity]
  type GridType = Array[Array[Cell]]

  def createEmptyGrid(width: Int, height: Int): GridType = {
    Array.fill(width, height)(None)
  }

  def initializeEntities(nTunas: Int, nSharks: Int, width: Int, height: Int): Seq[Entity] = {
    val random = new Random()
    val tunas = Seq.fill(nTunas)(Tuna(random.nextInt(width), random.nextInt(height), random.nextInt(5)))
    val sharks = Seq.fill(nSharks)(Shark(random.nextInt(width), random.nextInt(height), random.nextInt(5), random.nextInt(5), random.nextInt(100)))
    tunas ++ sharks
  }

  def updateGrid(grid: GridType, entities: Seq[Entity]): GridType = {
    val updatedGrid = createEmptyGrid(grid.length, grid(0).length)
    entities.foreach {
      case tuna: Tuna => updatedGrid(tuna.x)(tuna.y) = Some(tuna)
      case shark: Shark => updatedGrid(shark.x)(shark.y) = Some(shark)
    }
    updatedGrid
  }

  def simulateCycle(grid: GridType): GridType = {
    // Perform movement, breeding, and eating logic
    grid // Placeholder, implement logic as per Wator simulation rules
  }

  def moveEntity(entity: Entity, grid: GridType, width: Int, height: Int): (Entity, GridType) = {
    entity match {
      case tuna: Tuna => Tuna.move(tuna, grid, width, height)
      case shark: Shark => Shark.move(shark, grid, width, height)
    }
  }

  def updateCell(grid: GridType, x: Int, y: Int, entity: Cell): GridType = {
    grid(x)(y) = entity
    grid
  }

  def getFreeNeighborCells(x: Int, y: Int, grid: GridType, width: Int, height: Int): Seq[Coordinates] = {
    getNeighborCells(x, y, grid, width, height)
      .filter(cell => cell.isEmpty)
      .map(cell => Coordinates(cell.x, cell.y))
  }

  def getNeighborCells(x: Int, y: Int, grid: GridType, width: Int, height: Int): Seq[Option[Entity]] = {
    val neighbors = for {
      i <- -1 to 1
      j <- -1 to 1
      if !(i == 0 && j == 0)
    } yield grid((x + i + width) % width)((y + j + height) % height)
    neighbors
  }

}
