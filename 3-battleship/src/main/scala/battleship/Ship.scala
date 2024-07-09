package battleship

import Ships._

case class Ship(name: String, size: Int)

object Ships {
  val Destroyer = Ship("Destroyer", 2)
  val Submarine = Ship("Submarine", 3)
  val Cruiser = Ship("Cruiser", 3)
  val Battleship = Ship("Battleship", 4)
  val Carrier = Ship("Carrier", 5)

  val allShips = List(Destroyer, Submarine, Cruiser, Battleship, Carrier)
}
