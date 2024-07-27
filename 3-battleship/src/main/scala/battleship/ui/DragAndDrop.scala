package battleship.ui

import javafx.scene.control.Label
import javafx.scene.input.DataFormat
import javafx.scene.input.DragEvent
import javafx.scene.input.TransferMode
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import scalafx.Includes.*

object DragAndDrop {

  def setupDragAndDrop(shipPalette: VBox, boardPane: GridPane, onShipDrop: (Int, Int, Int) => Unit): Unit = {

    // Handle drag detected on the ship palette
    shipPalette.getChildren.forEach { node =>
      node match {
        case label: Label =>
          label.setOnDragDetected { (e: javafx.scene.input.MouseEvent) =>
            val db = label.startDragAndDrop(TransferMode.COPY)
            val content = new java.util.HashMap[DataFormat, Any]()
            content.put(DataFormat.PLAIN_TEXT, label.getText)
            db.setContent(content)
            e.consume()
          }
        case _ => // Ignore non-label nodes
      }
    }

    // Handle drag over on the board pane
    boardPane.setOnDragOver { (e: DragEvent) =>
      val db = e.getDragboard
      if (e.getGestureSource != boardPane && db.hasContent(DataFormat.PLAIN_TEXT)) {
        e.acceptTransferModes(TransferMode.COPY)
      }
      e.consume()
    }

    // Handle drag dropped on the board pane
    boardPane.setOnDragDropped { (e: DragEvent) =>
      val db = e.getDragboard
      if (db.hasContent(DataFormat.PLAIN_TEXT)) {
        val shipSize = db.getContent(DataFormat.PLAIN_TEXT).toString.toInt
        val dropX = e.getX
        val dropY = e.getY

        // Calculate grid cell indices
        val cellWidth = boardPane.getWidth / boardPane.getColumnCount
        val cellHeight = boardPane.getHeight / boardPane.getRowCount
        val rowIndex = (dropY / cellHeight).toInt
        val colIndex = (dropX / cellWidth).toInt

        // Call the callback function
        onShipDrop(shipSize, rowIndex, colIndex)

        // Remove the ship from the ship palette
        println(s"Ship Size from Drag Event: $shipSize") // Debug statement
        val shipToRemove = shipPalette.getChildren.toArray.collectFirst {
          case label: Label if label.getText.toInt == shipSize => label
        }
        shipToRemove.foreach { label =>
          shipPalette.getChildren.remove(label)
        }

        e.setDropCompleted(true)
      } else {
        e.setDropCompleted(false)
      }
      e.consume()
    }
  }
}
