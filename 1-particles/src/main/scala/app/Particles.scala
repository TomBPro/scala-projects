package app

class Particles {

  def this(n: Int) = {
    this()
    println(s"Creating $n particles")
  }

  def update(): Unit = {
    println("Updating particles")
  }

  def draw(): Unit = {
    println("Drawing particles")
  }
}
