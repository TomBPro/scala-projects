package particlesimulation

import scala.util.Random

sealed trait Direction
case object North extends Direction
case object South extends Direction
case object West extends Direction
case object East extends Direction
case object NorthWest extends Direction
case object NorthEast extends Direction
case object SouthWest extends Direction
case object SouthEast extends Direction

object Direction {
  private val directions: Array[Direction] = Array(North, South, West, East, NorthWest, NorthEast, SouthWest, SouthEast)

  def randomDirection(): Direction = {
    val index = Random.nextInt(directions.length)
    directions(index)
  }
}
