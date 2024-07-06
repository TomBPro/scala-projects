package wator

import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.animation.{KeyFrame, Timeline}
import scalafx.util.Duration
import scalafx.scene.Group

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
    var sharks = Shark.createSharks(nSharks, gridWidth, gridHeight)
    var grid = Grid.createGrid(gridWidth, gridHeight, tunas ++ sharks)

    val rectangles = new Group()
    rectangles.children = Grid.createGridShapes(grid, cellSize)

    stage = new PrimaryStage {
      title = "Wa-Tor"
      scene = new Scene(sceneWidth, sceneHeight) {
        fill = Color.DodgerBlue
        content = rectangles
      }
    }

    val timeline = new Timeline {
      cycleCount = Timeline.Indefinite
      keyFrames = Seq(
        KeyFrame(Duration(500), onFinished = _ => {
          tunas = Tuna.moveTunas(tunas, gridWidth, gridHeight)
          sharks = Shark.moveSharks(sharks, tunas, gridWidth, gridHeight)
          grid = Grid.createGrid(gridWidth, gridHeight, tunas ++ sharks)

          // Update the scene with new entities
          rectangles.children = Grid.createGridShapes(grid, cellSize)
        })
      )
    }

    timeline.play()
  }
}
