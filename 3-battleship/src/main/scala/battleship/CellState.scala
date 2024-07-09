package battleship

sealed trait CellState
case object Empty extends CellState
case object Ship extends CellState
case object Hit extends CellState
case object Miss extends CellState
