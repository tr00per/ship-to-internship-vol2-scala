package com.idemia.idemiomat.web.commands

import com.idemia.idemiomat.core.commands.Take
import com.idemia.idemiomat.core.domain.Owner
import com.idemia.idemiomat.core.{Command, Idemiomat}
import com.idemia.idemiomat.web.dto.{WebOwnedParcel, WebOwnedParcels}

case class TakeWithFeedback(owner: Owner) extends Command[WebOwnedParcels] {
  override def execute(imat: Idemiomat): WebOwnedParcels = {
    val owned = imat.boxes
      .filter(_.state.exists(_.owner.contains(owner)))

    imat.executeCommand(Take(owner))

    WebOwnedParcels(owned.map(parcel =>
      WebOwnedParcel(parcel.size)
    ))
  }
}
