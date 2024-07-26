package battleship

case class GameState(
  playerBoard: Board,
  opponentBoard: Board,
  playerTurn: Boolean
)
