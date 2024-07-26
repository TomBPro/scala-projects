// Entity.scala
package wator

trait Entity {
  def x: Int
  def y: Int
}

case class Coordinates(x: Int, y: Int)
