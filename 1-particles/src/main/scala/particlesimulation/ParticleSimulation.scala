package particlesimulation

import scalafx.scene.Scene
import scalafx.scene.Group
import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage

import scala.util.Random

object ParticleSimulation extends JFXApp3 {

  override def start(): Unit = {

    val width = 800
    val height = 600
    val particleCount = 180
    val particleRadius = 5

    val particlesGroup = new Group()
    val particles = Array.fill(particleCount)(
      new Particle(
        // These are random positions within window bounds
        Random.nextDouble() * (width - 2 * particleRadius) + particleRadius,
        Random.nextDouble() * (height - 2 * particleRadius) + particleRadius,
        Direction.randomDirection()
      )
    )


    particles.foreach(p => particlesGroup.children += p)

    stage = new PrimaryStage {
      title.value = "Particle Simulation"
      scene = new Scene(this.width.value, this.height.value) {
        content = particlesGroup

        val timer: AnimationTimer = AnimationTimer { _ =>
          particles.foreach(p => moveParticle(p, width.value, height.value))
          particles.foreach(p => repelParticles(p, particles))
        }

        timer.start()
      }
    }
  }

  private def moveParticle(particle: Particle, width: Double, height: Double): Unit = {
    particle.move(width, height)
  }

  private def repelParticles(particle: Particle, particles: Array[Particle]): Unit = {
    for (other <- particles if other != particle && particle.intersects(other.getBoundsInLocal)) {
      particle.changeDirection()
    }
  }
}
