package com.idemia.idemiomat.web.dto

import com.idemia.idemiomat.core.commands.LayoutDefinition
import com.idemia.idemiomat.core.domain.{Box, Owner, Parcel, Size}

case class WebBox(rozmiar: Size, paczki: List[Size], użytkownik: Option[Owner])

case class WebLayout(boxes: List[List[WebBox]]) {
  def toCommand: LayoutDefinition = LayoutDefinition(
    boxes.flatMap(_.map(wb => Box(wb.rozmiar, wb.paczki.map(parcelSize => Parcel(parcelSize, wb.użytkownik))))),
    boxes.head.length // Head of empty :D
  )
}
