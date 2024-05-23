package particlesimulation

import scalafx.animation.{KeyFrame, Timeline}
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.beans.property.ObjectProperty
import scalafx.scene.paint.Color
import scalafx.scene.{Group, Scene}
import scalafx.util.Duration

import scala.util.Random

object ParticleSimulation extends JFXApp3 {

  override def start(): Unit = {

    val windowWidth = 800
    val windowHeight = 600
    val particleCount = 1
    val particleRadius = 5

    val particles: Array[Particle] =
      Array.fill(particleCount)(
        Particle(
          // These are random positions within window bounds
          //centerX = Random.nextDouble() * (windowWidth - 2 * particleRadius) + particleRadius,
          //centerY = Random.nextDouble() * (windowHeight - 2 * particleRadius) + particleRadius,
          centerX = 599,
    centerY = 0,
          color = Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256)),
          direction = SouthEast
        )
      )
    val state: ObjectProperty[Array[Particle]] = ObjectProperty(particles)

    //particles.foreach(p => particlesGroup.children.add(p.toCircle))

    stage = new PrimaryStage {
      title = "Particle Simulation"
      width = windowWidth
      height = windowHeight
      scene = new Scene {
        content = state.value.map(_.toCircle)
        state.onChange {
          content = state.value.map(_.toCircle)
        }
      }
    }
    val timeline = new Timeline {
      cycleCount = Timeline.Indefinite
      autoReverse = true
      keyFrames = Seq(
        KeyFrame(Duration(5), onFinished = _ => state.update(state.value.map(_.move(windowWidth, windowHeight))))
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
