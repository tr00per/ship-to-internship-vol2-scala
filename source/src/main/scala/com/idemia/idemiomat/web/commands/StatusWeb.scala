package com.idemia.idemiomat.web.commands

import com.idemia.idemiomat.core.{Command, Idemiomat}
import com.idemia.idemiomat.web.dto.{WebBox, WebLayout}

case object StatusWeb extends Command[WebLayout] {
  override def execute(imat: Idemiomat): WebLayout = WebLayout(
    imat.boxes.map(box =>
      WebBox(box.size, box.state.map(_.size).sorted, box.state.headOption.flatMap(_.owner))
    ).grouped(imat.width).toList
  )
}
