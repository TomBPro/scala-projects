package battleship

import scalafx.application.JFXApp3
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Alert, Button, Label}
import scalafx.scene.input.{ClipboardContent, DragEvent, TransferMode}
import scalafx.scene.layout.{GridPane, HBox, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Rectangle, StrokeType}
import scalafx.scene.text.Font
import scalafx.Includes._

object Main extends JFXApp3 {

  override def start(): Unit = {
    val boardSize = 10
    var playerBoard = GameModel.initializeBoard(boardSize)
    var shipsToPlace = List(5, 4, 3, 3, 2)

    // Helper function to create a cell graphic
    def createCellGraphic(cellState: CellState, isPlayerBoard: Boolean): Rectangle = {
      val size = 30
      new Rectangle {
        width = size
        height = size
        arcWidth = 5
        arcHeight = 5
        stroke = Color.Black
        strokeType = StrokeType.Outside
        strokeWidth = 1
        fill = cellState match {
          case Empty => Color.LightSkyBlue
          case Ship => if (isPlayerBoard) Color.DimGray else Color.LightSkyBlue
          case Hit => Color.DarkRed
          case Miss => Color.LightGray
        }
      }
    }

    // Helper function to create a board pane
    def createBoardPane(board: Board, isPlayerBoard: Boolean): GridPane = {
      new GridPane {
        hgap = 0
        vgap = 0
        padding = Insets(0)
        style = "-fx-border-color: #8B8B8B; -fx-border-width: 1px;"
        updateBoardUI(board, this, isPlayerBoard)
      }
    }

    // Helper function to update board UI
    def updateBoardUI(board: Board, gridPane: GridPane, isPlayerBoard: Boolean): Unit = {
      gridPane.children.clear()
      gridPane.hgap = 0
      gridPane.vgap = 0
      gridPane.padding = Insets(0)

      for (i <- 0 until boardSize) {
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

      for (i <- 0 until boardSize) {
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

      for (x <- 0 until boardSize; y <- 0 until boardSize) {
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
        }
        gridPane.add(button, x + 1, y + 1)
      }
    }

    // Helper function to create ship palette
    def createShipPalette(): HBox = {
      new HBox {
        spacing = 10
        padding = Insets(10)
        children = shipsToPlace.map { size =>
          val shipRect = new Rectangle {
            width = size * 30
            height = 30
            arcWidth = 5
            arcHeight = 5
            fill = Color.DimGray
            stroke = Color.Black
            strokeType = StrokeType.Outside
            strokeWidth = 1
            onDragDetected = (e) => {
              val db = startDragAndDrop(TransferMode.Copy)
              val content = new ClipboardContent()
              content.putString(size.toString)
              db.setContent(content)
              e.consume()
            }
          }
          shipRect
        }
      }
    }

    // Function to handle placing ships on the board
    def placeShipsOnBoard(boardPane: GridPane): Unit = {
      boardPane.onDragOver = (e: DragEvent) => {
        if (e.getGestureSource != boardPane && e.getDragboard.hasString) {
          e.acceptTransferModes(TransferMode.Copy)
        }
        e.consume()
      }

      boardPane.onDragDropped = (e: DragEvent) => {
        val db = e.getDragboard
        var success = false
        if (db.hasString) {
          val shipSize = db.getString.toInt
          val x = (e.getX / 30).toInt
          val y = (e.getY / 30).toInt
          val position = Position(x, y)
          if (GameModel.canPlaceShip(playerBoard, position, shipSize)) {
            GameModel.placeShip(playerBoard, position, shipSize)
            shipsToPlace = shipsToPlace.filterNot(_ == shipSize)
            updateBoardUI(playerBoard, boardPane, isPlayerBoard = true)
            success = true
          }
        }
        e.setDropCompleted(success)
        e.consume()
      }
    }

    // Function to setup ship placement UI
    def setupShipPlacementUI(): Unit = {
      val boardPane = createBoardPane(playerBoard, isPlayerBoard = true)
      val shipPalette = createShipPalette()

      val rootPane = new VBox {
        spacing = 10
        padding = Insets(10)
        children = Seq(
          new Label("Place Your Ships") {
            font = Font.font(16)
            textFill = Color.DarkBlue
          },
          new HBox {
            spacing = 10
            padding = Insets(10)
            children = Seq(
              new VBox {
                spacing = 5
                children = Seq(
                  new Label("Ship Palette") {
                    font = Font.font(14)
                    textFill = Color.DarkBlue
                  },
                  shipPalette
                )
              },
              new VBox {
                spacing = 5
                children = Seq(
                  new Label("Your Board") {
                    font = Font.font(14)
                    textFill = Color.DarkBlue
                  },
                  boardPane
                )
              }
            )
          },
          new Button("Start Game") {
            onAction = _ => {
              if (shipsToPlace.isEmpty) {
                startGame()
              } else {
                new Alert(Alert.AlertType.Warning) {
                  title = "Error"
                  headerText = "Ships Not Placed"
                  contentText = "Please place all ships before starting the game."
                }.showAndWait()
              }
            }
          }
        )
      }

      stage = new JFXApp3.PrimaryStage {
        title = "Battleship - Ship Placement"
        scene = new Scene(rootPane, 800, 600)
      }

      placeShipsOnBoard(boardPane)
    }

    // Function to start the game
    def startGame(): Unit = {
      val initialGameState = GameState(
        playerBoard = playerBoard,
        opponentBoard = GameModel.placeShipsRandomly(GameModel.initializeBoard(boardSize)),
        playerTurn = true
      )
      val playerBoardPane = createBoardPane(initialGameState.playerBoard, isPlayerBoard = true)
      val opponentBoardPane = createBoardPane(initialGameState.opponentBoard, isPlayerBoard = false)

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
                textFill = Color.DarkBlue
              },
              opponentBoardPane
            )
          }
        )
      }

      stage = new JFXApp3.PrimaryStage {
        title = "Battleship"
        scene = new Scene(rootPane, 800, 600)
      }
    }

    setupShipPlacementUI()
  }
}
