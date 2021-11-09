package com.idemia.idemiomat.core.domain

case class Box(size: Size, state: List[Parcel])

object Box {
  def apply(size: Size) =
    new Box(size, List.empty)
}
