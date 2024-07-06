package wator

import scala.util.Random

case class Shark(x: Int, y: Int, override val energy: Int = Shark.sEnergy, breedCycle: Int) extends Entity

object Shark {
  val sBreed = 10
  val sEnergy = 7
  private val random = new Random()

  def createSharks(nSharks: Int, gridWidth: Int, gridHeight: Int): Seq[Shark] = {
    (0 until nSharks).map { _ =>
      Shark(random.nextInt(gridWidth), random.nextInt(gridHeight), sEnergy, 0)
    }
  }

  def moveSharks(sharks: Seq[Shark], tunas: Seq[Tuna], grid: Array[Array[CellType]], gridWidth: Int, gridHeight: Int): (Seq[Shark], Seq[Tuna]) = {
    val (movedSharks, newTunas) = sharks.foldLeft((Seq.empty[Shark], tunas)) {
      case ((accSharks, accTunas), shark) =>
        val (movedSharkOpt, updatedTunas) = moveShark(shark, grid, accTunas)
        (
          accSharks ++ movedSharkOpt,
          updatedTunas
        )
    }

    // Filter out dead sharks
    val survivingSharks = movedSharks.filter(_.energy > 0)

    // Handle breeding of surviving sharks
    val finalSharks = survivingSharks.flatMap { shark =>
      if (shark.breedCycle >= sBreed) {
        // Create a new shark at the original position
        Seq(shark.copy(breedCycle = 0, energy = shark.energy), Shark(shark.x, shark.y, sEnergy, 0))
      } else {
        Seq(shark)
      }
    }

    (finalSharks, newTunas)
  }

  private def moveShark(shark: Shark, grid: Array[Array[CellType]], tunas: Seq[Tuna]): (Seq[Shark], Seq[Tuna]) = {
    val neighbors = Grid.getNeighbors(shark.x, shark.y, grid.length, grid(0).length)

    // Check if any neighbor is occupied by a Tuna
    val tunaNeighbor = neighbors.find { case (nx, ny) =>
      grid(nx)(ny) match {
        case Occupied(t: Tuna) => true
        case _ => false
      }
    }

    // Logic for moving or eating
    tunaNeighbor match {
      case Some((nx, ny)) =>
        // Eat the tuna
        val updatedTunas = tunas.filterNot(t => t.x == nx && t.y == ny)
        (Seq(Shark(nx, ny, shark.energy + 2, shark.breedCycle + 1)), updatedTunas)
      case None =>
        // Move to an empty neighbor cell if possible
        val freeNeighbors = neighbors.collect {
          case (nx, ny) if grid(nx)(ny) == Empty => (nx, ny)
        }
        if (freeNeighbors.nonEmpty) {
          val (nx, ny) = freeNeighbors(random.nextInt(freeNeighbors.length))
          (Seq(Shark(nx, ny, shark.energy - 1, shark.breedCycle + 1)), tunas)
        } else {
          (Seq(shark.copy(energy = shark.energy - 1, breedCycle = shark.breedCycle + 1)), tunas)
        }
    }
  }
}
