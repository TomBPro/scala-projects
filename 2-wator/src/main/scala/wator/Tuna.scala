// Tuna.scala
package wator

import scala.util.Random

object Tuna {

  def move(tuna: Tuna, grid: Grid.GridType, width: Int, height: Int): (Tuna, Grid.GridType) = {
    val possibleMoves = Grid.getFreeNeighborCells(tuna.x, tuna.y, grid, width, height)
    if (possibleMoves.nonEmpty) {
      val newPosition = possibleMoves(Random.nextInt(possibleMoves.length))
      val newTuna = tuna.copy(x = newPosition.x, y = newPosition.y, breedCycle = tuna.breedCycle + 1)
      (newTuna, Grid.updateCell(grid, newPosition.x, newPosition.y, Some(newTuna)))
    } else {
      (tuna, grid)
    }
  }

}
