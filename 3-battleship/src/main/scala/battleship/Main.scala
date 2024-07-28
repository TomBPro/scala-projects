package battleship

import battleship.model.Cell
import battleship.model.GameModel
import battleship.model.GameState
import battleship.model.Orientation
import battleship.ui.BoardUI
import battleship.ui.DragAndDrop
import battleship.ui.UIElements
import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.geometry.Pos
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.control.Label
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.HBox
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.text.Font

object Main extends JFXApp3 {
  override def start(): Unit = {
    val boardSize = 10
    val playerBoard: Array[Array[Cell]] = Array.fill(boardSize, boardSize)(Cell.Empty)
    val placedShips = Set.empty[(Int, Int, Orientation)]

    def onShipPlaced(rowIndex: Int, colIndex: Int, shipSize: Int, orientation: Orientation): Unit = {
      println(s"Ship placed: Row $rowIndex, Column $colIndex, Size $shipSize, Orientation $orientation")
    }

    def setupShipPlacementUI(): Unit = {
      val boardUI = new BoardUI(playerBoard, placedShips, onShipPlaced)
      val boardPane = new GridPane()
      boardUI.updateUI(boardPane)

      val shipPalette = UIElements.createShipPalette(List(5, 4, 3, 3, 2), placedShips)

      DragAndDrop.setupDragAndDrop(shipPalette, boardPane, (shipSize, rowIndex, colIndex) => {
        boardUI.placeShip(rowIndex, colIndex, shipSize, Orientation.Horizontal)
        boardUI.updateUI(boardPane)
      })

      stage = new JFXApp3.PrimaryStage {
        title = "Battleship - Ship Placement"
        scene = new Scene {
          root = new VBox {
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
                onAction = _ => startGame()
              }
            )
          }
          minWidth = 800
          minHeight = 600
        }
      }
    }

    def startGame(): Unit = {
      val initialGameState = GameState(
        playerBoard = playerBoard,
        opponentBoard = GameModel.placeShipsRandomly(GameModel.initializeBoard(boardSize)),
        playerTurn = true
      )

      val playerBoardPane = new GridPane()
      val opponentBoardPane = new GridPane()

      val boardUI = new BoardUI(initialGameState.playerBoard, placedShips, onShipPlaced)
      boardUI.updateUI(playerBoardPane)
      boardUI.updateUI(opponentBoardPane)

      stage = new JFXApp3.PrimaryStage {
        title = "Battleship"
        scene = new Scene {
          root = new HBox {
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
          minWidth = 800
          minHeight = 600
        }
      }
    }

    setupShipPlacementUI()
  }
}