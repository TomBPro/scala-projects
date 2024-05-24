package wator

object Shark {
  val sBreed = 10
  val sEnergy = 3 // (+1 when eating tuna, -1 when not)

  /*
    à chaque cycle, il se déplace dans une case voisine occupée par un thon
    sinon, il se déplace dans une case voisine libre
    s'il mange un thon, il regagne un point de sEnergy
    s'il n'y a pas de case occupée par un thon ou de case libre, il ne bouge pas et il ne se reproduit pas.
    au bout de sBreed cycles, il se reproduit en donnant naissance à un nouveau requin dans la case qu'il occupait avant de se déplacer.
    s'il ne mange pas de thon au bout de sEnergy cycles, il meurt
   */
}