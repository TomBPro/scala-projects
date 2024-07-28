package battleship.ui

import battleship.model.Orientation
import scalafx.scene.control.Button
import scalafx.scene.input.ClipboardContent
import scalafx.scene.input.DataFormat
import scalafx.scene.input.DragEvent
import scalafx.scene.input.TransferMode
import scalafx.scene.layout.VBox
import scalafx.scene.text.Font

object UIElements {
  def createShipPalette(shipsToPlace: List[Int], placedShips: Set[(Int, Int, Orientation)]): VBox = {
    new VBox {
      spacing = 5
      children = shipsToPlace.filterNot(size => placedShips.exists(_._1 == size)).map { size =>
        new Button {
          text = s"Ship of size $size"
          font = Font.font("Arial", 14)
          minWidth = 100
          minHeight = 40
          style = "-fx-background-color: lightblue; -fx-border-color: black; -fx-border-width: 1;"

          onDragDetected = e => {
            val dragboard = startDragAndDrop(TransferMode.Copy)
            val content = new ClipboardContent()
            content.put(DataFormat.PlainText, size.toString)
            dragboard.setContent(content)
            e.consume()
          }
        }
      }
    }
  }
}