package wator

trait Entity {
  def x: Int
  def y: Int
  def breedCycle: Int
  def energy: Int = 0 // Default for Tunas since they do not have energy levels
}