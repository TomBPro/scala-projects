package battleship

import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.geometry.Pos
import scalafx.scene.Scene
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.Button
import scalafx.scene.control.Label
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.HBox
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.Font
import scalafx.scene.text.Text

object Main extends JFXApp3 {

  override def start(): Unit = {
    val initialGameState = GameState(
      playerBoard = GameModel.placeShipsRandomly(GameModel.initializeBoard(10)),
      opponentBoard = GameModel.placeShipsRandomly(GameModel.initializeBoard(10))
    )

    def handlePlayerInteraction(position: Position, gameState: GameState, opponentBoardPane: GridPane): Unit = {
      if (gameState.playerTurn) {
        val (updatedBoard, result) = GameModel.makeMove(gameState.opponentBoard, position)
        val newGameState = gameState.copy(opponentBoard = updatedBoard, playerTurn = false)
        updateBoardUI(updatedBoard, opponentBoardPane, isPlayerBoard = false, newGameState)
        if (GameModel.checkVictory(updatedBoard)) {
          new Alert(AlertType.Information) {
            title = "Game Over"
            headerText = "Player Wins!"
            contentText = "Congratulations!"
          }.showAndWait()
        }
      }
    }

    def updateBoardUI(board: Board, gridPane: GridPane, isPlayerBoard: Boolean, gameState: GameState): Unit = {
      gridPane.children.clear()
      gridPane.hgap = 0
      gridPane.vgap = 0
      gridPane.padding = Insets(0)

      // Create column headers
      for (i <- 0 until 10) {
        val header = new Label((i + 1).toString) {
          textFill = Color.Black
          font = Font.font(12)
          minWidth = 30
          minHeight = 30
          alignment = Pos.Center
          background = scalafx.scene.layout.Background.fill(Color.LightSkyBlue)
        }
        gridPane.add(header, i + 1, 0)
      }

      // Create row headers
      for (i <- 0 until 10) {
        val header = new Label(('A' + i).toChar.toString) {
          textFill = Color.Black
          font = Font.font(12)
          minWidth = 30
          minHeight = 30
          alignment = Pos.Center
          background = scalafx.scene.layout.Background.fill(Color.LightSkyBlue)
        }
        gridPane.add(header, 0, i + 1)
      }

      // Create board cells
      for (x <- 0 until 10; y <- 0 until 10) {
        val position = Position(x, y)
        val cellState = board.cells(position)
        val button = new Button {
          graphic = createCellGraphic(cellState, isPlayerBoard)
          style = "-fx-background-color: transparent;"
          minWidth = 30
          minHeight = 30
          maxWidth = 30
          maxHeight = 30
          padding = Insets(0)
          onAction = _ => {
            if (gameState.playerTurn && !isPlayerBoard) {
              val (updatedBoard, result) = GameModel.makeMove(gameState.opponentBoard, position)
              val newGameState = gameState.copy(opponentBoard = updatedBoard, playerTurn = false)
              updateBoardUI(updatedBoard, gridPane, isPlayerBoard = false, newGameState)
              if (GameModel.checkVictory(updatedBoard)) {
                new Alert(AlertType.Information) {
                  title = "Game Over"
                  headerText = "Player Wins!"
                  contentText = "Congratulations!"
                }.showAndWait()
              }
            } else if (!gameState.playerTurn && isPlayerBoard) {
              handlePlayerInteraction(position, gameState, gridPane)
              if (GameModel.checkVictory(gameState.opponentBoard)) {
                new Alert(AlertType.Information) {
                  title = "Game Over"
                  headerText = "Computer Wins!"
                  contentText = "You lost. Better luck next time!"
                }.showAndWait()
              }
            }
          }
        }
        gridPane.add(button, x + 1, y + 1)
      }
    }

    def createCellGraphic(cellState: CellState, isPlayerBoard: Boolean): Rectangle = {
      val size = 30
      val rectangle = new Rectangle {
        width = size
        height = size
        arcWidth = 5
        arcHeight = 5
        stroke = Color.Black
        strokeWidth = 1
      }
      cellState match {
        case Empty =>
          rectangle.fill = Color.LightSkyBlue
        case Ship =>
          rectangle.fill = if (isPlayerBoard) Color.DimGray else Color.LightSkyBlue
        case Hit =>
          rectangle.fill = Color.DarkRed
        case Miss =>
          rectangle.fill = Color.LightGray
      }
      rectangle
    }

    val playerBoardPane = new GridPane {
      hgap = 0
      vgap = 0
      padding = Insets(0)
      style = "-fx-border-color: #8B8B8B; -fx-border-width: 1px;"
    }
    updateBoardUI(initialGameState.playerBoard, playerBoardPane, isPlayerBoard = true, initialGameState)

    val opponentBoardPane = new GridPane {
      hgap = 0
      vgap = 0
      padding = Insets(0)
      style = "-fx-border-color: #8B8B8B; -fx-border-width: 1px;"
    }
    updateBoardUI(initialGameState.opponentBoard, opponentBoardPane, isPlayerBoard = false, initialGameState)

    val rootPane = new HBox {
      spacing = 5
      padding = Insets(10)
      alignment = Pos.Center
      children = Seq(
        new VBox {
          spacing = 5
          children = Seq(
            new Label("Player Board") {
              font = Font.font(16)
              textFill = Color.DarkBlue
            },
            playerBoardPane
          )
        },
        new VBox {
          spacing = 5
          children = Seq(
            new Label("Opponent Board") {
              font = Font.font(16)
              textFill = Color.DarkRed
            },
            opponentBoardPane
          )
        }
      )
    }

    stage = new JFXApp3.PrimaryStage {
      title = "Battleship"
      scene = new Scene(rootPane, 600, 500) // Adjusted window size
    }
  }
}
