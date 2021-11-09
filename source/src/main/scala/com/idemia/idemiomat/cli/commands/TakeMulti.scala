package com.idemia.idemiomat.cli.commands

import com.idemia.idemiomat.core.commands.Take
import com.idemia.idemiomat.core.domain.Owner
import com.idemia.idemiomat.core.{Command, Idemiomat}

case class TakeMulti(owners: List[Owner]) extends Command[Unit] {
  override def execute(imat: Idemiomat): Unit = {
    owners.foreach({ owner => imat.executeCommand(Take(owner)) })
  }
}
