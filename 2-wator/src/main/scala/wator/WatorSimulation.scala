package wator

object WatorSimulation {
  def main(args: Array[String]): Unit = {
    val gridWidth = 50
    val gridHeight = 50
    val nTunas = 200
    val nSharks = 50

    val initialGrid = Grid.createEmptyGrid(gridWidth, gridHeight)
    val initialTunas = Tuna.createTunas(nTunas, gridWidth, gridHeight)
    val initialSharks = Shark.createSharks(nSharks, gridWidth, gridHeight)

    val initialEntities: Seq[Entity] = initialTunas ++ initialSharks
    var grid = Grid.updateGrid(initialGrid, initialEntities)

    for (_ <- 1 to 100) {
      val newTunas = Tuna.moveAndBreedTunas(initialTunas, grid, gridWidth, gridHeight)
      val (newSharks, finalTunas) = Shark.moveAndBreedSharks(initialSharks, newTunas, grid, gridWidth, gridHeight)

      val newEntities: Seq[Entity] = finalTunas ++ newSharks
      grid = Grid.updateGrid(grid, newEntities)
    }
  }
}
