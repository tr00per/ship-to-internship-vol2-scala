package com.idemia.idemiomat.core

import com.idemia.idemiomat.core.domain.Box

class Idemiomat {
  var boxes: List[Box] = _
  var width: Int = _

  def executeCommand[A](cmd: Command[A]): A =
    cmd.execute(this)

}
