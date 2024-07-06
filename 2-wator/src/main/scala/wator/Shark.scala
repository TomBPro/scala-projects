package wator

import scala.util.Random

case class Shark(x: Int, y: Int, energy: Int, breedCycle: Int)

object Shark {
  private val random = new Random()
  private val sBreed = 10
  private val sEnergy = 3

  def createSharks(nSharks: Int, gridWidth: Int, gridHeight: Int): Seq[Shark] =
    (0 until nSharks).map(_ => Shark(random.nextInt(gridWidth), random.nextInt(gridHeight), sEnergy, 0))

  def moveSharks(sharks: Seq[Shark], tunas: Seq[Tuna], gridWidth: Int, gridHeight: Int): Seq[Shark] = {
    def getNeighbors(x: Int, y: Int): Seq[(Int, Int)] = {
      val neighbors = List(
        (x, y - 1), (x, y + 1), (x - 1, y), (x + 1, y),
        (x - 1, y - 1), (x + 1, y - 1), (x - 1, y + 1), (x + 1, y + 1)
      )
      neighbors.filter {
        case (nx, ny) => nx >= 0 && nx < gridWidth && ny >= 0 && ny < gridHeight
      }
    }

    def moveShark(shark: Shark, grid: Array[Array[CellType]]): Option[Shark] = {
      val neighbors = getNeighbors(shark.x, shark.y)
      val tunaNeighbors = neighbors.flatMap {
        case (nx, ny) =>
          grid(nx)(ny) match {
            case Tuna(tx, ty) => Some((nx, ny, tx, ty))
            case _ => None
          }
      }

      if (tunaNeighbors.nonEmpty) {
        val (nx, ny, tx, ty) = tunaNeighbors(random.nextInt(tunaNeighbors.length))
        val newEnergy = shark.energy + 1
        val newShark = Shark(nx, ny, newEnergy, shark.breedCycle + 1)
        grid(shark.x)(shark.y) = Empty(shark.x, shark.y)
        grid(tx)(ty) = Shark(nx, ny, newEnergy, shark.breedCycle + 1)
        Some(newShark)
      } else {
        val freeNeighbors = neighbors.filter {
          case (nx, ny) => grid(nx)(ny).isInstanceOf[Empty]
        }
        if (freeNeighbors.nonEmpty) {
          val (nx, ny) = freeNeighbors(random.nextInt(freeNeighbors.length))
          val newEnergy = shark.energy - 1
          val newShark = Shark(nx, ny, newEnergy, shark.breedCycle + 1)
          grid(shark.x)(shark.y) = Empty(shark.x, shark.y)
          grid(nx)(ny) = Shark(nx, ny, newEnergy, shark.breedCycle + 1)
          Some(newShark)
        } else {
          if (shark.breedCycle >= sBreed) {
            grid(shark.x)(shark.y) = Shark(shark.x, shark.y, shark.energy, 0)
            Some(Shark(shark.x, shark.y, shark.energy, 0))
          } else {
            grid(shark.x)(shark.y) = Shark(shark.x, shark.y, shark.energy - 1, shark.breedCycle + 1)
            Some(Shark(shark.x, shark.y, shark.energy - 1, shark.breedCycle + 1))
          }
        }
      }
    }

    sharks.flatMap(moveShark(_, grid))
  }
}
