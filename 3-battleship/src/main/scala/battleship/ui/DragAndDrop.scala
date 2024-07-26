package battleship.ui

import javafx.scene.input.DataFormat
import scalafx.Includes.*
import scalafx.scene.Node
import scalafx.scene.input.DragEvent
import scalafx.scene.input.Dragboard
import scalafx.scene.input.TransferMode
import scalafx.scene.layout.GridPane

object DragAndDrop {

  def setupDragAndDrop(shipPalette: Node, boardPane: GridPane, onShipDrop: (Int, Int, Int) => Unit): Unit = {

    // Start drag operation
    shipPalette.setOnDragDetected { e =>
      val db = shipPalette.startDragAndDrop(TransferMode.Copy)
      val content = new java.util.HashMap[DataFormat, Any]()
      content.put(DataFormat.PLAIN_TEXT, "5") // Example ship size, update as needed
      db.setContent(content)
      e.consume()
    }

    // Handle drag over event
    boardPane.setOnDragOver { e =>
      if (e.getGestureSource != boardPane && e.getDragboard.hasContent(DataFormat.PLAIN_TEXT)) {
        e.acceptTransferModes(TransferMode.Copy)
      }
      e.consume()
    }

    // Handle drop event
    boardPane.setOnDragDropped { e =>
      val db = e.getDragboard
      if (db.hasContent(DataFormat.PLAIN_TEXT)) {
        val shipSize = db.getContent(DataFormat.PLAIN_TEXT).toString.toInt
        val dropX = e.getX
        val dropY = e.getY

        // Translate drop coordinates to grid cell coordinates
        val cellWidth = boardPane.width.value / boardPane.getColumnCount
        val cellHeight = boardPane.height.value / boardPane.getRowCount
        val rowIndex = (dropY / cellHeight).toInt
        val colIndex = (dropX / cellWidth).toInt

        onShipDrop(shipSize, rowIndex, colIndex)
        e.setDropCompleted(true)
      } else {
        e.setDropCompleted(false)
      }
      e.consume()
    }
  }
}
