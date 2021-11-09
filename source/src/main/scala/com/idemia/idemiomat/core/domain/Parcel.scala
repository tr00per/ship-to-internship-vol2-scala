package com.idemia.idemiomat.core.domain

case class Parcel(size: Size, owner: Option[Owner])

object Parcel {
  def apply(size: Size) =
    new Parcel(size, None)

  def apply(size: Size, owner: Owner) =
    new Parcel(size, Some(owner))
}
