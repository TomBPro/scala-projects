package particlesimulation

import scalafx.animation.{KeyFrame, Timeline}
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.beans.property.{DoubleProperty, ObjectProperty}
import scalafx.scene.paint.Color
import scalafx.scene.Scene
import scalafx.util.Duration

import scala.util.Random

object ParticleSimulation extends JFXApp3 {

  override def start(): Unit = {

    val initialWidth = 800
    val initialHeight = 600
    val final particleCount = 170
    val final particleRadius = 5.0

    val particles: Array[Particle] =
      Array.fill(particleCount)(
        Particle(
          // These are random positions within window bounds
          centerX = Random.nextDouble() * (initialWidth - 2 * particleRadius) + particleRadius,
          centerY = Random.nextDouble() * (initialHeight - 2 * particleRadius) + particleRadius,
          color = Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256)),
          direction = Direction.randomDirection()
        )
      )
    val state: ObjectProperty[Array[Particle]] = ObjectProperty(particles)

    val windowWidth: DoubleProperty = new DoubleProperty(this, "windowWidth", initialWidth)
    val windowHeight: DoubleProperty = new DoubleProperty(this, "windowHeight", initialHeight)

    stage = new PrimaryStage {
      title = "Particle Simulation"
      width = initialWidth
      height = initialHeight
      scene = new Scene {
        content = state.value.map(_.toCircle)
        state.onChange {
          content = state.value.map(_.toCircle)
        }
      }
    }

    // Allows dynamic window size change
    windowWidth.bind(stage.scene().widthProperty())
    windowHeight.bind(stage.scene().heightProperty())

    val timeline = new Timeline {
      cycleCount = Timeline.Indefinite
      autoReverse = false
      keyFrames = Seq(
        KeyFrame(Duration(5), onFinished = _ => {
          val updatedParticles = state.value.map(p => p.move(windowWidth.value, windowHeight.value))
          val repelledParticles = updatedParticles.map(p => repelParticles(p, updatedParticles))
          state.update(repelledParticles)
        })
      )
    }

    timeline.play()
  }

  private def repelParticles(particle: Particle, particles: Array[Particle]): Particle = {
    particles.find(other => other != particle && particle.toCircle.intersects(other.toCircle.getBoundsInLocal)) match {
      case Some(_) => particle.changeDirection()
      case None => particle
    }
  }
}
