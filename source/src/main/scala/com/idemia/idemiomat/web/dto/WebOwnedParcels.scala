package com.idemia.idemiomat.web.dto

import com.idemia.idemiomat.core.domain.Size

case class WebOwnedParcel(rozmiar: Size)

case class WebOwnedParcels(parcels: List[WebOwnedParcel])
