package battleship.model

object GameModel {
  def initializeBoard(size: Int): Array[Array[Cell]] =
    Array.fill(size, size)(Cell.Empty)

  def placeShip(board: Array[Array[Cell]], x: Int, y: Int, size: Int, isHorizontal: Boolean): Array[Array[Cell]] = {
    if (isHorizontal) {
      for (i <- x until (x + size) if i < board(0).length) {
        board(y)(i) = Cell.Ship
      }
    } else {
      for (i <- y until (y + size) if i < board.length) {
        board(i)(x) = Cell.Ship
      }
    }
    board
  }

  def placeShipsRandomly(board: Array[Array[Cell]]): Array[Array[Cell]] = {
    val random = scala.util.Random
    val shipSizes = List(5, 4, 3, 3, 2)

    def canPlaceShip(board: Array[Array[Cell]], x: Int, y: Int, size: Int, isHorizontal: Boolean): Boolean = {
      if (isHorizontal) {
        (x until (x + size)).forall(i => i < board(0).length && board(y)(i) == Cell.Empty)
      } else {
        (y until (y + size)).forall(i => i < board.length && board(i)(x) == Cell.Empty)
      }
    }

    def placeRandomShip(board: Array[Array[Cell]], size: Int): Array[Array[Cell]] = {
      val isHorizontal = random.nextBoolean()
      val x = random.nextInt(board(0).length)
      val y = random.nextInt(board.length)

      if (canPlaceShip(board, x, y, size, isHorizontal)) {
        placeShip(board, x, y, size, isHorizontal)
      } else {
        placeRandomShip(board, size)
      }
    }

    shipSizes.foldLeft(board) { (b, size) => placeRandomShip(b, size) }
  }
}
