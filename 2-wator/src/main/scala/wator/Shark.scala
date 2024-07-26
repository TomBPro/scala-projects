// Shark.scala
package wator

import scala.util.Random

object Shark {

  def move(shark: Shark, grid: Grid.GridType, width: Int, height: Int): (Shark, Grid.GridType) = {
    val possibleMoves = Grid.getNeighborCells(shark.x, shark.y, grid, width, height)
      .filter(cell => cell.exists(_.isInstanceOf[Tuna]))
      .map(_.get)

    if (possibleMoves.isEmpty) {
      val freeMoves = Grid.getFreeNeighborCells(shark.x, shark.y, grid, width, height)
      if (freeMoves.nonEmpty) {
        val newPosition = freeMoves(Random.nextInt(freeMoves.length))
        val newShark = shark.copy(x = newPosition.x, y = newPosition.y, energyCycle = shark.energyCycle + 1)
        (newShark, Grid.updateCell(grid, newPosition.x, newPosition.y, Some(newShark)))
      } else {
        (shark, grid)
      }
    } else {
      val newPosition = Coordinates(possibleMoves.head.x, possibleMoves.head.y)
      val energyGained = possibleMoves.head.asInstanceOf[Tuna].energy
      val newShark = shark.copy(x = newPosition.x, y = newPosition.y, energyCycle = 0, energy = shark.energy + energyGained)
      val updatedGrid = Grid.updateCell(grid, newPosition.x, newPosition.y, Some(newShark))
      (newShark, updatedGrid)
    }
  }

}
