package wator

import scalafx.animation.KeyFrame
import scalafx.animation.Timeline
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.util.Duration

object WatorSimulation extends JFXApp3 {

  val cellSize = 20.0
  val gridWidth = 30
  val gridHeight = 20
  val sceneWidth = gridWidth * cellSize
  val sceneHeight = gridHeight * cellSize
  val nTunas = 10
  val nSharks = 10

  var tunas = Seq.empty[Tuna]
  var sharks = Seq.empty[Shark]
  var grid = Array.ofDim[CellType](gridWidth, gridHeight)

  override def start(): Unit = {
    tunas = Tuna.createTunas(nTunas, gridWidth, gridHeight)
    sharks = Shark.createSharks(nSharks, gridWidth, gridHeight)
    grid = Grid.createGrid(gridWidth, gridHeight)

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

    val sharkRectangles = sharks.map { shark =>
      new Rectangle {
        x = shark.x * cellSize
        y = shark.y * cellSize
        width = cellSize
        height = cellSize
        fill = Color.Red
      }
    }

    stage = new PrimaryStage {
      title = "Wa-Tor"
      scene = new Scene(sceneWidth, sceneHeight) {
        fill = Color.DodgerBlue
        content = gridLines ++ tunaRectangles ++ sharkRectangles
      }
    }

    val timeline = new Timeline {
      cycleCount = Timeline.Indefinite
      keyFrames = Seq(
        KeyFrame(Duration(500), onFinished = _ => update())
      )
    }
    timeline.play()
  }

  private def update(): Unit = {
    grid = Grid.createGrid(gridWidth, gridHeight)
    tunas = Tuna.moveTunas(tunas, gridWidth, gridHeight)
    sharks = Shark.moveSharks(sharks, tunas, gridWidth, gridHeight)

    val newTunaRectangles = tunas.map { tuna =>
      new Rectangle {
        x = tuna.x * cellSize
        y = tuna.y * cellSize
        width = cellSize
        height = cellSize
        fill = Color.Green
      }
    }

    val newSharkRectangles = sharks.map { shark =>
      new Rectangle {
        x = shark.x * cellSize
        y = shark.y * cellSize
        width = cellSize
        height = cellSize
        fill = Color.Red
      }
    }

    stage.scene().content.clear()
    stage.scene().content ++= gridLines ++ newTunaRectangles ++ newSharkRectangles
  }
}
