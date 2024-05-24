package wator

import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.animation.{KeyFrame, Timeline}
import scalafx.util.Duration

object WatorSimulation extends JFXApp3 {
  override def start(): Unit = {
    val nTunas = 10
    val nSharks = 10
    val cellSize = 20.0
    val gridWidth = 30
    val gridHeight = 20
    val sceneWidth = gridWidth * cellSize
    val sceneHeight = gridHeight * cellSize

    var tunas = Tuna.createTunas(nTunas, gridWidth, gridHeight)
    val gridLines = Grid.createGridLines(gridWidth, gridHeight, cellSize)

    val tunaRectangles = tunas.map { tuna =>
      new Rectangle {
        x = tuna.x * cellSize
        y = tuna.y * cellSize
        width = cellSize
        height = cellSize
        fill = Color.Green
      }
    }

    stage = new PrimaryStage {
      title = "Wa-Tor"
      scene = new Scene(sceneWidth, sceneHeight) {
        fill = Color.DodgerBlue
        content = gridLines ++ tunaRectangles
      }
    }

    val timeline = new Timeline {
      cycleCount = Timeline.Indefinite
      keyFrames = Seq(
        KeyFrame(Duration(500), onFinished = _ => {
          // Move tunas to a new position
          tunas = Tuna.moveTunas(tunas, gridWidth, gridHeight)

          for ((tuna, rectangle) <- tunas zip tunaRectangles) {
            rectangle.x = tuna.x * cellSize
            rectangle.y = tuna.y * cellSize
          }
        })
      )
    }

    timeline.play()
  }
}