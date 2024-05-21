package particles

import app.Particles
import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.paint.Color

object ParticlesApp extends JFXApp3 {
  val particles = new Particles(100)
  val canvas = new Canvas(800, 600)
  val gc = canvas.graphicsContext2D

  stage = new PrimaryStage {
    title = "Particles"
    scene = new Scene(800, 600) {
      content = canvas
    }
  }

  val timer = AnimationTimer { _ =>
    gc.fill = Color.Black
    gc.fillRect(0, 0, canvas.width.value, canvas.height.value)
    particles.update()
    particles.draw(gc)
  }
  timer.start()

  override def start(): Unit = ???
}