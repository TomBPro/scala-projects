package wator

import scala.util.Random

case class Tuna(x: Int, y: Int, breedCycle: Int = 0) extends Entity

object Tuna {
  private val random = new Random()
  private val tBreed = 3

  def createTunas(nTunas: Int, gridWidth: Int, gridHeight: Int): Seq[Tuna] =
    (0 until nTunas).map(_ => Tuna(random.nextInt(gridWidth), random.nextInt(gridHeight)))

  def moveTunas(tunas: Seq[Tuna], grid: Array[Array[CellType]], gridWidth: Int, gridHeight: Int): Seq[Tuna] = {
    val newTunas = tunas.flatMap(tuna => moveTuna(tuna, grid, gridWidth, gridHeight))
    breedTunas(newTunas, gridWidth, gridHeight)
  }

  private def moveTuna(tuna: Tuna, grid: Array[Array[CellType]], gridWidth: Int, gridHeight: Int): Option[Tuna] = {
    val neighbors = Grid.getNeighbors(tuna.x, tuna.y, gridWidth, gridHeight)

    val freeNeighbors = neighbors.filter {
      case (nx, ny) => Grid.cellIsEmpty(nx, ny, grid) // Ensure the neighbor cell is empty
    }

    if (freeNeighbors.nonEmpty) {
      val (nx, ny) = freeNeighbors(random.nextInt(freeNeighbors.length))
      Some(Tuna(nx, ny, tuna.breedCycle + 1))
    } else {
      Some(tuna.copy(breedCycle = tuna.breedCycle + 1))
    }
  }

  private def breedTunas(tunas: Seq[Tuna], gridWidth: Int, gridHeight: Int): Seq[Tuna] = {
    tunas ++ tunas.collect {
      case tuna if tuna.breedCycle >= tBreed =>
        Tuna(tuna.x, tuna.y)
    }
  }
}
