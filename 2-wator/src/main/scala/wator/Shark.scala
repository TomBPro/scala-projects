package wator

import scala.util.Random

case class Shark(x: Int, y: Int, override val energy: Int = Shark.sEnergy, breedCycle: Int) extends Entity

object Shark {
  val sBreed = 10
  val sEnergy = 3
  private val random = new Random()

  def createSharks(nSharks: Int, gridWidth: Int, gridHeight: Int): Seq[Shark] = {
    (0 until nSharks).map { _ =>
      Shark(random.nextInt(gridWidth), random.nextInt(gridHeight), sEnergy, 0)
    }
  }

  def moveSharks(sharks: Seq[Shark], tunas: Seq[Tuna], gridWidth: Int, gridHeight: Int): Seq[Shark] = {
    val grid = Grid.createGrid(gridWidth, gridHeight, tunas ++ sharks)
    val movedSharks = sharks.flatMap { shark =>
      moveShark(shark, grid)
    }
    val survivingSharks = movedSharks.filter(_.energy > 0)
    breedSharks(survivingSharks)
  }

  private def moveShark(shark: Shark, grid: Array[Array[CellType]]): Option[Shark] = {
    val neighbors = Grid.getNeighbors(shark.x, shark.y, grid.length, grid(0).length)

    // Look for a neighbor occupied by a Tuna
    val tunaNeighbor = neighbors.find { case (nx, ny) =>
      grid(nx)(ny) match {
        case Occupied(tuna: Tuna) => true
        case _ => false
      }
    }

    tunaNeighbor match {
      case Some((nx, ny)) =>
        Some(Shark(nx, ny, shark.energy + 1, shark.breedCycle + 1))

      case None =>
        val freeNeighbors = neighbors.collect { 
          case (nx, ny) if grid(nx)(ny) == Empty => (nx, ny)
        }
        if (freeNeighbors.nonEmpty) {
          val (nx, ny) = freeNeighbors(random.nextInt(freeNeighbors.length))
          Some(Shark(nx, ny, shark.energy - 1, shark.breedCycle + 1))
        } else {
          Some(shark.copy(energy = shark.energy - 1, breedCycle = shark.breedCycle + 1))
        }
    }
  }

  private def breedSharks(sharks: Seq[Shark]): Seq[Shark] = {
    sharks ++ sharks.filter(_.breedCycle >= sBreed).map { shark =>
      Shark(shark.x, shark.y, sEnergy, 0)
    }
  }
}
