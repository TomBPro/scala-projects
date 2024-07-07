package wator

import scalafx.animation.KeyFrame
import scalafx.animation.Timeline
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Group
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.Text
import scalafx.util.Duration

object WatorSimulation extends JFXApp {
  val gridWidth = 20
  val gridHeight = 20
  val cellSize = 20.0

  var sharks: Seq[Shark] = Shark.createSharks(10, gridWidth, gridHeight)
  var tunas: Seq[Tuna] = Tuna.createTunas(20, gridWidth, gridHeight)
  var grid: Array[Array[CellType]] = Grid.createEmptyGrid(gridWidth, gridHeight)

  grid = Grid.updateGrid(grid, sharks ++ tunas)

  val rootGroup = new Group {
    children = Grid.createGridShapes(grid, cellSize)
  }

  stage = new PrimaryStage {
    title.value = "Wa-Tor Simulation"
    scene = new Scene(gridWidth * cellSize, gridHeight * cellSize) {
      fill = Color.LightBlue // Ocean background color
      root = rootGroup
    }
  }

  def printGameState(sharks: Seq[Shark], tunas: Seq[Tuna]): Unit = {
    println(s"Sharks: ${sharks.size}, Tunas: ${tunas.size}")
  }

  def checkVictoryConditions(sharks: Seq[Shark], tunas: Seq[Tuna]): Option[String] = {
    if (sharks.isEmpty && tunas.isEmpty) {
      Some("Both species extinct. It's a draw.")
    } else if (sharks.isEmpty) {
      Some("Tunas win! No sharks left.")
    } else if (tunas.isEmpty) {
      Some("Sharks win! No tunas left.")
    } else {
      None
    }
  }

  def displayVictoryMessage(message: String): Unit = {
    val victoryText = new Text {
      text = message
      style = "-fx-font: 24 ariel;"
      fill = Color.Red
      x = gridWidth * cellSize / 4
      y = gridHeight * cellSize / 2
    }
    rootGroup.children.clear()
    rootGroup.children.add(victoryText)
  }

  val timeline: Timeline = new Timeline {
    cycleCount = Timeline.Indefinite
    keyFrames = Seq(
      KeyFrame(Duration(500), onFinished = _ => {
        // Move sharks and handle shark breeding
        val (newSharks, newTunas) = Shark.moveSharks(sharks, tunas, grid, gridWidth, gridHeight)
        sharks = Shark.breedSharks(newSharks)

        // Move tunas and handle tuna breeding
        val movedTunas = Tuna.moveTunas(newTunas, grid, gridWidth, gridHeight)
        tunas = Tuna.breedTunas(movedTunas)

        // Update grid with existing entities
        grid = Grid.updateGrid(grid, sharks ++ tunas)

        printGameState(sharks, tunas)

        // Check for victory conditions
        checkVictoryConditions(sharks, tunas) match {
          case Some(message) =>
            timeline.stop()
            displayVictoryMessage(message)
          case None =>
            rootGroup.children = Grid.createGridShapes(grid, cellSize)
        }
      })
    )
  }

  timeline.play()
}
