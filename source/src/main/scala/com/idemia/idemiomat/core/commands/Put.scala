package com.idemia.idemiomat.core.commands

import com.idemia.idemiomat.core.domain.{Box, Parcel}
import com.idemia.idemiomat.core.{Command, Idemiomat}

import scala.math.Ordering.Implicits.infixOrderingOps

case class Put(parcel: List[Parcel]) extends Command[Unit] {
  override def execute(imat: Idemiomat): Unit = {
    parcel.foreach({
      case parcel@Parcel(_, None) =>
        putParcelWithoutOwner(imat, parcel)
      case parcel@Parcel(_, Some(_)) =>
        putParcelWithOwner(imat, parcel)
    })
  }

  def putParcelWithoutOwner(imat: Idemiomat, parcel: Parcel): Unit = {
    imat.boxes.zipWithIndex
      .filter(_._1.state.isEmpty)
      .filter(_._1.size >= parcel.size)
      .minByOption(_._1.size)
      .foreach({ case (firstFreeBox, index) =>
        val updatedBox = firstFreeBox.copy(state = List(parcel))
        imat.boxes = imat.boxes.updated(index, updatedBox)
      })
  }

  def putParcelWithOwner(imat: Idemiomat, parcel: Parcel): Unit = {
    val owned = imat.boxes.zipWithIndex
      .filter(_._1.state.exists(_.owner == parcel.owner))
    if (owned.isEmpty) putParcelWithoutOwner(imat, parcel)
    else putParcelInMultibox(imat, parcel, owned)
  }

  private def putParcelInMultibox(imat: Idemiomat, parcel: Parcel, owned: List[(Box, Int)]): Unit = {
    val withSpaceLeft = owned.filter(iob => hasSpaceLeft(iob._1))
    val wouldFitNext = withSpaceLeft.find(iob => wouldFit(parcel, iob._1))

    wouldFitNext.fold(putParcelWithoutOwner(imat, parcel))(iob => {
      imat.boxes = imat.boxes.updated(iob._2, iob._1.copy(state = parcel :: iob._1.state))
    })
  }

  def hasSpaceLeft(box: Box): Boolean =
    box.size.volume > box.state.map(_.size.volume).sum

  def wouldFit(parcel: Parcel, box: Box): Boolean =
    box.size.volume >= box.state.map(_.size.volume).sum + parcel.size.volume

}
