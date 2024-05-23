package particlesimulation

import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle

import scala.util.Random

case class Particle(centerX: Double, centerY: Double, color: Color, direction: Direction) {
  val radius = 5.0

  def move(width: Double, height: Double): Particle = {
    val (newCenterX: Double, newCenterY: Double) = direction match {
      case North if centerY < 0           => (centerX, height - 1)
      case North                               => (centerX, centerY - 1)
      case South if centerY >= height    => (centerX, 0)
      case South                               => (centerX, centerY + 1)
      case West if centerX < 0            => (width - 1, centerY)
      case West                                => (centerX - 1, centerY)
      case East if centerX >= width      => (0, centerY)
      case East                                => (centerX + 1, centerY)
      case NorthWest if centerX < 0 && centerY < 0 => (width - 1, height - 1)
      case NorthWest if centerX < 0       => (width - 1, centerY - 1)
      case NorthWest if centerY < 0       => (centerX - 1, height - 1)
      case NorthWest                           => (centerX - 1, centerY - 1)
      case NorthEast if centerX > width && centerY < 0 => (0, height - 1)
      case NorthEast if centerX > width  => (0, height - 1)
      case NorthEast if centerY < 0       => (centerX + 1, height - 1)
      case NorthEast                           => (centerX + 1, centerY - 1)
      case SouthWest if centerX < 0 && centerY >= height => (width - 1, 0)
      case SouthWest if centerX < 0       => (width - 1, centerY + 1)
      case SouthWest if centerY >= height => (width - 1, 0)
      case SouthWest                           => (centerX - 1, centerY + 1)
      case SouthEast if centerX >= width && centerY >= height => (0, 0)
      case SouthEast if centerX >= width  => (0, centerY + 1)
      case SouthEast if centerY >= height => (centerX + 1, 0)
      case SouthEast                           => (centerX + 1, centerY + 1)
    }

    println(s"New particle position: ($newCenterX, $newCenterY)")
    copy(centerX = newCenterX, centerY = newCenterY)
  }



  def changeDirection(): Particle = copy(direction = Direction.randomDirection())

  val toCircle: Circle = Circle(centerX, centerY, radius, color)
}
