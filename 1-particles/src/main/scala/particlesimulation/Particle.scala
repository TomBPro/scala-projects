package particlesimulation
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle

import scala.util.Random

class Particle(initialCenterX: Double, initialCenterY: Double, var direction: Direction)
  extends Circle {

  radius = 5.0

  centerX = initialCenterX
  centerY = initialCenterY
  fill = Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))

  def move(width: Double, height: Double): Unit = {
    direction match {
      case North => centerY.value -= 1
      case South => centerY.value += 1
      case West  => centerX.value -= 1
      case East  => centerX.value += 1
      case NorthWest =>
        centerX.value -= 1
        centerY.value -= 1
      case NorthEast =>
        centerX.value += 1
        centerY.value -= 1
      case SouthWest =>
        centerX.value -= 1
        centerY.value += 1
      case SouthEast =>
        centerX.value += 1
        centerY.value += 1
    }

    // Wrap around the window
    if (centerX.value < 0) centerX.value = width - 1
    if (centerX.value >= width) centerX.value = 0
    if (centerY.value < 0) centerY.value = height - 1
    if (centerY.value >= height) centerY.value = 0
  }

  def changeDirection(): Unit = {
    direction = Direction.randomDirection()
  }
}
