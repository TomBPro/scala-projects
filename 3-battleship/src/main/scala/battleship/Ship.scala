package battleship

case class GameShip(name: String, size: Int)

object GameShips {
  val Destroyer = GameShip("Destroyer", 2)
  val Submarine = GameShip("Submarine", 3)
  val Cruiser = GameShip("Cruiser", 3)
  val Battleship = GameShip("Battleship", 4)
  val Carrier = GameShip("Carrier", 5)

  val allShips = List(Destroyer, Submarine, Cruiser, Battleship, Carrier)
}
