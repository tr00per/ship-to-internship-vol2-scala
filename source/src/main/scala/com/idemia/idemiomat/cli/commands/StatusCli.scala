package com.idemia.idemiomat.cli.commands

import com.idemia.idemiomat.core.{Command, Idemiomat}

case object StatusCli extends Command[String] {
  override def execute(imat: Idemiomat): String = {
    imat.boxes.map(box => {
      val boxSize = box.size.code.toString
      val parcelSizes = box.state.foldLeft("")(_ + _.size.code)
      val parcelList = {
        if (boxSize == parcelSizes) ":X"
        else if (parcelSizes.nonEmpty) ":" + parcelSizes.sorted.reverse
        else ":O"
      }
      val parcelOwner = box.state.foldLeft("")((_, parcel) => parcel.owner.fold("")("@" + _.who))
      boxSize + parcelList + parcelOwner
    }).grouped(imat.width).map(_.mkString("|", " ", "|")).mkString("\n")
  }
}
