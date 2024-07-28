package wator

import scala.util.Random

import Grid.Coordinates

case class Tuna(x: Int, y: Int, override val energy: Int = 1, breedCycle: Int = 0) extends Entity

object Tuna {
  private val random = new Random()
  private val tBreed = 7

  def createTunas(nTunas: Int, gridWidth: Int, gridHeight: Int): Seq[Tuna] = {
    (0 until nTunas).map(_ => Tuna(random.nextInt(gridWidth), random.nextInt(gridHeight)))
  }

  def moveTunas(tunas: Seq[Tuna], grid: Array[Array[CellType]], gridWidth: Int, gridHeight: Int): Seq[Tuna] = {
    val updatedTunas = tunas.flatMap { tuna =>
      moveTuna(tuna, grid, gridWidth, gridHeight)
    }

    Grid.updateGrid(grid, updatedTunas)
    updatedTunas
  }

  def breedTunas(tunas: Seq[Tuna]): Seq[Tuna] = {
    val newTunas = tunas.flatMap { tuna =>
      if (tuna.breedCycle >= tBreed) {
        Seq(tuna.copy(breedCycle = 0), Tuna(tuna.x, tuna.y))
      } else {
        Seq(tuna)
      }
    }
    newTunas
  }

  private def moveTuna(tuna: Tuna, grid: Array[Array[CellType]], gridWidth: Int, gridHeight: Int): Option[Tuna] = {
    val neighbors = Grid.getNeighbors(tuna.x, tuna.y, gridWidth, gridHeight)
    val freeNeighbors = neighbors.filter { case (nx, ny) => Grid.cellIsEmpty(nx, ny, grid) }

    if (freeNeighbors.nonEmpty) {
      val (nx, ny) = freeNeighbors(random.nextInt(freeNeighbors.length))
      Some(Tuna(nx, ny, tuna.energy, tuna.breedCycle + 1))
    } else {
      Some(tuna.copy(breedCycle = tuna.breedCycle + 1))
    }
  }
}