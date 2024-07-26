package battleship.model

sealed trait Cell
object Cell {
  case object Empty extends Cell
  case object Ship extends Cell
}

sealed trait Orientation
object Orientation {
  case object Horizontal extends Orientation
  case object Vertical extends Orientation
}
