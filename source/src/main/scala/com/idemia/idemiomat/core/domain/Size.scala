package com.idemia.idemiomat.core.domain

sealed trait Size {
  def code: Char

  def volume: Int
}

case object Small extends Size {
  override def code: Char = 'S'

  override def volume: Int = 1
}

case object Medium extends Size {
  override def code: Char = 'M'

  override def volume: Int = 2
}

case object Large extends Size {
  override def code: Char = 'L'

  override def volume: Int = 4
}

object Size {
  val DEFAULT_SIZE: Size = Small

  def apply(code: Char): Size = code match {
    case 'S' => Small
    case 'M' => Medium
    case 'L' => Large
  }

  def apply(code: String): Size =
    if (code == null || code.isEmpty)
      DEFAULT_SIZE
    else Size(code.charAt(0))

  implicit object SizeOrdering extends Ordering[Size] {
    override def compare(x: Size, y: Size): Int =
      x.volume - y.volume
  }
}
