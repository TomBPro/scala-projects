package battleship.model

case class GameState(playerBoard: Array[Array[Cell]], opponentBoard: Array[Array[Cell]], playerTurn: Boolean)

