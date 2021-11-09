package com.idemia.idemiomat.web.dto

import com.idemia.idemiomat.core.commands.Put
import com.idemia.idemiomat.core.domain.{Owner, Parcel, Size}

case class WebParcel(rozmiar: Size, użytkownik: Owner) {
  def toCommand: Put = Put(
    List(Parcel(rozmiar, użytkownik))
  )
}
