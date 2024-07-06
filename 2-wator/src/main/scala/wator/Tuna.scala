package wator

import scala.util.Random

case class Tuna(x: Int, y: Int)

object Tuna {
  private val random = new Random()
  private val tBreed = 3

  def createTunas(nTunas: Int, gridWidth: Int, gridHeight: Int): Seq[Tuna] =
    (0 until nTunas).map(_ => Tuna(random.nextInt(gridWidth), random.nextInt(gridHeight)))

  def moveTunas(tunas: Seq[Tuna], gridWidth: Int, gridHeight: Int): Seq[Tuna] = {
    def getNeighbors(x: Int, y: Int): Seq[(Int, Int)] = {
      val neighbors = List(
        (x, y - 1), (x, y + 1), (x - 1, y), (x + 1, y),
        (x - 1, y - 1), (x + 1, y - 1), (x - 1, y + 1), (x + 1, y + 1)
      )
      neighbors.filter {
        case (nx, ny) => nx >= 0 && nx < gridWidth && ny >= 0 && ny < gridHeight
      }
    }

    def moveTuna(tuna: Tuna, grid: Array[Array[Boolean]]): Option[Tuna] = {
      val neighbors = getNeighbors(tuna.x, tuna.y)
      val freeNeighbors = neighbors.filter { case (nx, ny) => !grid(nx)(ny) }
      if (freeNeighbors.nonEmpty) {
        val (nx, ny) = freeNeighbors(random.nextInt(freeNeighbors.length))
        Some(Tuna(nx, ny))
      } else {
        Some(tuna)
      }
    }

    val grid = Array.fill(gridWidth, gridHeight)(false)
    tunas.flatMap { tuna =>
      moveTuna(tuna, grid) match {
        case Some(newTuna) =>
          grid(newTuna.x)(newTuna.y) = true
          Some(newTuna)
        case None => None
      }
    }
  }
}
