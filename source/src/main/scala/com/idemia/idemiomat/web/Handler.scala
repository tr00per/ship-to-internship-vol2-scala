package com.idemia.idemiomat.web

import com.idemia.idemiomat.core.Idemiomat
import com.idemia.idemiomat.core.domain.Owner
import com.idemia.idemiomat.web.commands.{SecuredFreeBoxes, StatusWeb, TakeWithFeedback}
import com.idemia.idemiomat.web.dto.{WebLayout, WebOwnedParcels, WebParcel, WebSecuredFreeBoxes}

class Handler(imat: Idemiomat, psk: String) {
  def defineLayout(request: WebLayout): Unit = {
    val layoutDefinition = request.toCommand
    imat.executeCommand(layoutDefinition)
  }

  def putParcel(request: WebParcel): Unit = {
    val putCommand = request.toCommand
    imat.executeCommand(putCommand)
  }

  def takeOut(request: Owner): WebOwnedParcels = {
    val takeCommand = TakeWithFeedback(request)
    imat.executeCommand(takeCommand)
  }

  def getStatus: WebLayout =
    imat.executeCommand(StatusWeb)

  def getSecuredFree: WebSecuredFreeBoxes =
    imat.executeCommand(SecuredFreeBoxes(psk))

}
