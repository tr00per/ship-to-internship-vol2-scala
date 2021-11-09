package com.idemia.idemiomat.core.commands

import com.idemia.idemiomat.core.domain.{Bootstrap, Box}
import com.idemia.idemiomat.core.{Command, Idemiomat}

case class LayoutDefinition(box: List[Box], width: Int) extends Command[Bootstrap.type] {
  override def execute(imat: Idemiomat): Bootstrap.type = {
    imat.boxes = box
    imat.width = width
    Bootstrap
  }
}
