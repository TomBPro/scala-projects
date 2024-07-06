package wator

import scalafx.animation.KeyFrame
import scalafx.animation.Timeline
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Group
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.util.Duration

object WatorSimulation extends JFXApp3 {
  override def start(): Unit = {
    val nTunas = 40
    val nSharks = 10
    val cellSize = 20.0
    val gridWidth = 30
    val gridHeight = 20
    val sceneWidth = gridWidth * cellSize
    val sceneHeight = gridHeight * cellSize

    var tunas = Tuna.createTunas(nTunas, gridWidth, gridHeight)
    var sharks = Shark.createSharks(nSharks, gridWidth, gridHeight)
    val grid = Grid.createEmptyGrid(gridWidth, gridHeight)

    Grid.updateGrid(grid, tunas ++ sharks)

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
          Grid.updateGrid(grid, tunas ++ sharks)
          var (movedSharks: Seq[Shark], updatedTunas: Seq[Tuna]) = Shark.moveSharks(sharks, tunas, grid, gridWidth, gridHeight)
          sharks = movedSharks
          tunas = updatedTunas
          Grid.updateGrid(grid, tunas ++ sharks)

          // Handle Tuna movement
          tunas = Tuna.moveTunas(tunas, grid, gridWidth, gridHeight)
          Grid.updateGrid(grid, tunas ++ sharks)

          // Update the scene with new entities
          rectangles.children = Grid.createGridShapes(grid, cellSize)
        })
      )
    }

    timeline.play()
  }
}
