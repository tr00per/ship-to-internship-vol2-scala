package com.idemia.idemiomat.core.commands

import com.idemia.idemiomat.core.domain.Owner
import com.idemia.idemiomat.core.{Command, Idemiomat}

case class Take(owner: Owner) extends Command[Unit] {
  override def execute(imat: Idemiomat): Unit = {
    val owned = imat.boxes.zipWithIndex
      .filter(_._1.state.exists(_.owner.contains(owner)))
    owned.foreach({ case (box, index) =>
      imat.boxes = imat.boxes.updated(index, box.copy(state = List.empty))
    })
  }
}
