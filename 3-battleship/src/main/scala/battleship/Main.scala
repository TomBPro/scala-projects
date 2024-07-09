package battleship

import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.Button
import scalafx.scene.control.Label
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.VBox

object Main extends JFXApp3 {

  override def start(): Unit = {
    var gameState = GameState(
      playerBoard = GameModel.placeShipsRandomly(GameModel.initializeBoard(10)),
      opponentBoard = GameModel.placeShipsRandomly(GameModel.initializeBoard(10))
    )

    def updateBoardUI(board: Board, gridPane: GridPane, isPlayerBoard: Boolean): Unit = {
      gridPane.children.clear()
      for (x <- 0 until 10; y <- 0 until 10) {
        val position = Position(x, y)
        val cellState = board.cells(position)
        val button = new Button {
          text = cellState match {
            case Empty => " "
            case Ship => "S"
            case Hit => "X"
            case Miss => "O"
          }
          style = cellState match {
            case Empty => "-fx-background-color: lightblue;"
            case Ship => "-fx-background-color: grey;"
            case Hit => "-fx-background-color: red;"
            case Miss => "-fx-background-color: white;"
          }
          onAction = _ => {
            if (gameState.playerTurn && isPlayerBoard) {
              val (updatedBoard, result) = GameModel.makeMove(board, position)
              gameState = gameState.copy(playerBoard = updatedBoard, playerTurn = false)
              updateBoardUI(updatedBoard, gridPane, isPlayerBoard = true)
              if (GameModel.checkVictory(updatedBoard)) {
                new Alert(AlertType.Information) {
                  title = "Game Over"
                  headerText = "Player Wins!"
                  contentText = "Congratulations!"
                }.showAndWait()
              }
            } else if (!gameState.playerTurn && !isPlayerBoard) {
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
        gridPane.add(button, x, y)
      }
    }

    val playerBoardPane = new GridPane {
      hgap = 5
      vgap = 5
    }
    updateBoardUI(gameState.playerBoard, playerBoardPane, isPlayerBoard = true)

    val opponentBoardPane = new GridPane {
      hgap = 5
      vgap = 5
    }
    updateBoardUI(gameState.opponentBoard, opponentBoardPane, isPlayerBoard = false)

    val rootPane = new VBox {
      spacing = 10
      padding = Insets(10)
      children = Seq(
        new Label("Battleship Game"),
        new Label("Player Board"),
        playerBoardPane,
        new Label("Opponent Board"),
        opponentBoardPane
      )
    }

    stage = new JFXApp3.PrimaryStage {
      title = "Battleship"
      scene = new Scene {
        root = rootPane
        content = List(rootPane)
        width = 800
        height = 800
      }
    }
  }

  def handlePlayerInteraction(position: Position, gameState: GameState, opponentBoardPane: GridPane): Unit = {
    if (gameState.playerTurn) {
      val (updatedBoard, result) = GameModel.makeMove(gameState.opponentBoard, position)
      gameState = gameState.copy(opponentBoard = updatedBoard, playerTurn = false)
      updateBoardUI(updatedBoard, opponentBoardPane, isPlayerBoard = false)
      if (GameModel.checkVictory(updatedBoard)) {
        new Alert(AlertType.Information) {
          title = "Game Over"
          headerText = "Player Wins!"
          contentText = "Congratulations!"
        }.showAndWait()
      }
    }
  }
}
