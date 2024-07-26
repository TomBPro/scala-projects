package battleship

case class Position(x: Int, y: Int)

object Position {
  def fromString(s: String): Position = {
    val col = s.charAt(0).toUpper - 'A'
    val row = s.substring(1).toInt - 1
    Position(col, row)
  }
}
