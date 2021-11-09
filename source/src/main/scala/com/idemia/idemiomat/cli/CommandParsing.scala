package com.idemia.idemiomat.cli

import com.idemia.idemiomat.cli.commands.{StatusCli, TakeMulti}
import com.idemia.idemiomat.core._
import com.idemia.idemiomat.core.commands.{LayoutDefinition, Put, Take}
import com.idemia.idemiomat.core.domain.{Box, Owner, Parcel, Size}

object CommandParsing {
  private val DEFINITION_ELEMENT = "([SML]):([XOSML]+)(?:@([^\\s;|]{1,30}))?".r
  private val PUT_ELEMENT = "B([SML])?(?:@([^\\s;|]{1,30}))?".r
  private val TAKE = "T([^\\s;|]{1,30})".r
  private val STATUS = "S".r

  def readCommand(getLine: () => String): Option[Command[_]] = {
    def nextLine(): String = Option(getLine()).map(_.trim).getOrElse("")

    def isPartOfDefinitionCommand(line: String) =
      line.startsWith("|") && line.endsWith("|")

    def nextCommandLine(): String = {
      val newLine = nextLine()
      if (isPartOfDefinitionCommand(newLine)) {
        val sb = new StringBuilder(newLine)
        LazyList.continually(nextLine()).takeWhile(_.nonEmpty).foreach(sb.append)
        sb.toString
      }
      else newLine
    }

    val cmdLine = nextCommandLine()

    val layout = DEFINITION_ELEMENT.findAllMatchIn(cmdLine).map({
      boxDef =>
        val boxSize = Size(boxDef.group(1))
        val parcelOwner = Option(boxDef.group(3)).map(Owner)
        val parcels = boxDef.group(2).filterNot(_ == 'O')
          .map(parcelSize => if (parcelSize == 'X') boxSize else Size(parcelSize))
          .map(Parcel(_, parcelOwner))
        Box(boxSize, parcels.toList)
    }).toList
    val parcels = PUT_ELEMENT.findAllMatchIn(cmdLine).map({
      parcelDef =>
        val parcelSize = Size(parcelDef.group(1))
        val parcelOwner = Option(parcelDef.group(2)).map(Owner)
        Parcel(parcelSize, parcelOwner)
    }).toList
    val takeOuts = TAKE.findAllMatchIn(cmdLine).map({ ownerDef =>
      Owner(ownerDef.group(1))}).toList

    if (layout.nonEmpty) {
      val layoutWidth = 2 * layout.length / cmdLine.count(_ == '|') // Div by 0 :D
      Some(LayoutDefinition(layout, layoutWidth))
    }
    else if (parcels.nonEmpty) Some(Put(parcels))
    else if (takeOuts.nonEmpty) Some(TakeMulti(takeOuts))
    else cmdLine match {
      case STATUS() => Some(StatusCli)
      case _ => None
    }
  }

}
