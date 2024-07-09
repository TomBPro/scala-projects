package battleship

object GameModel {

  def initializeBoard(size: Int): Board = {
    val cells = (for {
      x <- 0 until size
      y <- 0 until size
    } yield Position(x, y) -> Empty).toMap
    Board(cells)
  }

  def placeShip(board: Board, positions: List[Position]): Board = {
    val updatedCells = positions.foldLeft(board.cells)((cells, pos) => cells.updated(pos, Ship))
    board.copy(cells = updatedCells)
  }

  def isValidPosition(board: Board, positions: List[Position]): Boolean = {
    positions.forall(pos => board.cells.get(pos).contains(Empty))
  }

  def placeShipsRandomly(board: Board): Board = {
    def placeShipOnBoard(board: Board, ship: Ship): Board = {
      val rand = scala.util.Random
      val positions = (0 until ship.size).map { i =>
        val orientation = rand.nextBoolean()
        if (orientation) Position(rand.nextInt(10), rand.nextInt(10) + i)
        else Position(rand.nextInt(10) + i, rand.nextInt(10))
      }.toList
      if (isValidPosition(board, positions)) placeShip(board, positions)
      else placeShipOnBoard(board, ship)
    }
    Ships.allShips.foldLeft(board)(placeShipOnBoard)
  }

  def makeMove(board: Board, position: Position): (Board, CellState) = {
    board.cells.get(position) match {
      case Some(Ship) =>
        val updatedBoard = board.copy(cells = board.cells.updated(position, Hit))
        (updatedBoard, Hit)
      case Some(Empty) =>
        val updatedBoard = board.copy(cells = board.cells.updated(position, Miss))
        (updatedBoard, Miss)
      case _ => (board, board.cells(position))
    }
  }

  def checkVictory(board: Board): Boolean = {
    !board.cells.values.exists(_ == Ship)
  }
}
