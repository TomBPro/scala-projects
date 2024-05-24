package particlesimulation

import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle

case class Particle(centerX: Double, centerY: Double, color: Color, direction: Direction) {
  private type Coordinates = (Double, Double)
  private val radius = 5.0

  def move(width: Double, height: Double): Particle = {

    val (deltaX, deltaY): Coordinates = direction match {
      case North     => (0, -1)
      case South     => (0, 1)
      case West      => (-1, 0)
      case East      => (1, 0)
      case NorthWest => (-1, -1)
      case NorthEast => (1, -1)
      case SouthWest => (-1, 1)
      case SouthEast => (1, 1)
    }

    // Calculate new position with wrapping using modular arithmetic
    val newCenterX = (centerX + deltaX + width) % width
    val newCenterY = (centerY + deltaY + height) % height

    copy(centerX = newCenterX, centerY = newCenterY)
  }

  def changeDirection(): Particle = copy(direction = Direction.randomDirection())

  val toCircle: Circle = Circle(centerX, centerY, radius, color)
}
