package wator

import scala.util.Random

case class Tuna(x: Int, y: Int, breedCycle: Int = 0) extends Entity

object Tuna {
  private val random = new Random()
  private val tBreed = 3

  def createTunas(nTunas: Int, gridWidth: Int, gridHeight: Int): Seq[Tuna] =
    (0 until nTunas).map(_ => Tuna(random.nextInt(gridWidth), random.nextInt(gridHeight)))

  def moveTunas(tunas: Seq[Tuna], gridWidth: Int, gridHeight: Int): Seq[Tuna] = {
    val newTunas = tunas.flatMap(moveTuna(_, gridWidth, gridHeight))
    breedTunas(newTunas, gridWidth, gridHeight)
  }

  private def moveTuna(tuna: Tuna, gridWidth: Int, gridHeight: Int): Option[Tuna] = {
    val neighbors = Grid.getNeighbors(tuna.x, tuna.y, gridWidth, gridHeight)

    val freeNeighbors = neighbors.filterNot {
      case (nx, ny) => nx == tuna.x && ny == tuna.y
    }

    if (freeNeighbors.nonEmpty) {
      val (nx, ny) = freeNeighbors(random.nextInt(freeNeighbors.length))
      Some(Tuna(nx, ny, tuna.breedCycle + 1))
    } else {
      Some(tuna.copy(breedCycle = tuna.breedCycle + 1))
    }
  }

  private def breedTunas(tunas: Seq[Tuna], gridWidth: Int, gridHeight: Int): Seq[Tuna] = {
    tunas ++ tunas.filter(_.breedCycle >= tBreed).map { tuna =>
      tuna.copy(breedCycle = 0)
    }
  }
}