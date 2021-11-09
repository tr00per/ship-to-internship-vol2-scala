package com.idemia.idemiomat

import com.idemia.idemiomat.cli.CommandParsing
import com.idemia.idemiomat.cli.commands.StatusCli
import com.idemia.idemiomat.core.Idemiomat
import com.idemia.idemiomat.core.domain.Bootstrap

import scala.io.StdIn

object MainCli extends App {
  Console.err.println("IDEMIOMAT")

  val imat = new Idemiomat()
  LazyList.continually(CommandParsing.readCommand(StdIn.readLine))
    .takeWhile(_.isDefined)
    .map(cmd => imat.executeCommand(cmd.get))
    .foreach({
      case s: String =>
        println(s)
      case Bootstrap =>

      case _ =>
        println(imat.executeCommand(StatusCli))
    })
}
