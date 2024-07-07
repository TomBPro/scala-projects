package wator

import scala.util.Random
import Grid.Coordinates

case class Shark(x: Int, y: Int, override val energy: Int = Shark.sEnergy, breedCycle: Int, hasMoved: Boolean = false) extends Entity

object Shark {
  val sBreed: Int = 10
  val sEnergy: Int = 7
  private val random = new Random()

  def createSharks(nSharks: Int, gridWidth: Int, gridHeight: Int): Seq[Shark] = {
    (0 until nSharks).map {
      _ => Shark(random.nextInt(gridWidth), random.nextInt(gridHeight), sEnergy, 0)
    }
  }

  def moveSharks(sharks: Seq[Shark], tunas: Seq[Tuna], grid: Array[Array[CellType]], gridWidth: Int, gridHeight: Int): (Seq[Shark], Seq[Tuna]) = {
    val newGrid = Grid.updateGrid(grid, sharks ++ tunas)
    val (movedSharks, remainingTunas) = sharks.foldLeft((Seq.empty[Shark], tunas)) {
      case ((accSharks, accTunas), shark) =>
        if (!shark.hasMoved) {
          val (movedSharkSeq, updatedTunas) = moveShark(shark, newGrid, accTunas)
          (
            accSharks ++ movedSharkSeq.map(_.copy(hasMoved = true)),
            updatedTunas
          )
        } else {
          (accSharks :+ shark, accTunas)
        }
    }

    // Filter out dead sharks
    val survivingSharks = movedSharks.filter(_.energy > 0)

    // Handle breeding of surviving sharks
    val newSharks = breedSharks(survivingSharks)
    val resetSharks = newSharks.map(_.copy(hasMoved = false))
    (resetSharks, remainingTunas)
  }

  def breedSharks(sharks: Seq[Shark]): Seq[Shark] = {
    val newSharks = sharks.flatMap { shark =>
      if (shark.breedCycle >= sBreed) {
        Seq(shark.copy(breedCycle = 0), Shark(shark.x, shark.y, sEnergy, 0))
      } else {
        Seq(shark)
      }
    }
    newSharks
  }

  private def moveShark(shark: Shark, grid: Array[Array[CellType]], tunas: Seq[Tuna]): (Seq[Shark], Seq[Tuna]) = {
    val neighbors = Grid.getNeighbors(shark.x, shark.y, grid.length, grid(0).length)

    // Check if any neighbor is occupied by a Tuna
    val tunaNeighbor = neighbors.find { case (nx, ny) =>
      grid(nx)(ny) match {
        case Occupied(_: Tuna) => true
        case _ => false
      }
    }

    val (newSharkSeq, updatedTunas) = tunaNeighbor match {
      case Some((nx, ny)) =>
        // Eat the tuna
        val newShark = Shark(nx, ny, shark.energy + 2, shark.breedCycle + 1, hasMoved = true)
        (Seq(newShark), tunas.filterNot(t => t.x == nx && t.y == ny))

      case None =>
        // Move to an empty neighbor cell if possible
        val freeNeighbors = neighbors.collect { case coords@(nx, ny) if Grid.cellIsEmpty(nx, ny, grid) => coords }
        if (freeNeighbors.nonEmpty) {
          val (nx, ny) = freeNeighbors(random.nextInt(freeNeighbors.length))
          val newShark = Shark(nx, ny, shark.energy - 1, shark.breedCycle + 1, hasMoved = true)
          (Seq(newShark), tunas)
        } else {
          val newShark = shark.copy(energy = shark.energy - 1, breedCycle = shark.breedCycle + 1, hasMoved = true)
          (Seq(newShark), tunas)
        }
    }

    (newSharkSeq, updatedTunas)
  }
}
