package com.idemia.idemiomat

import com.idemia.idemiomat.cli.commands.StatusCli
import com.idemia.idemiomat.core.Idemiomat
import com.idemia.idemiomat.core.commands.LayoutDefinition
import com.idemia.idemiomat.core.domain._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class StatusPlainTest extends AnyWordSpecLike with Matchers {
  for ((expectedOutput, example, exampleWidth) <- List(
    ("|S:X S:O|\n|S:O S:O|",
      List(
        Box(Small, List(Parcel(Small))), Box(Small),
        Box(Small), Box(Small)
      ), 2),
    ("|S:O M:O|\n|L:MM:xyz M:O|",
      List(
        Box(Small), Box(Medium),
        Box(Large, List(Parcel(Medium, Owner("xyz")), Parcel(Medium, Owner("xyz")))), Box(Medium)
      ), 2),
    ("|S:O S:O S:O M:O M:O|\n|L:O L:O S:X M:X M:O|",
      List(
        Box(Small), Box(Small), Box(Small), Box(Medium), Box(Medium),
        Box(Large), Box(Large), Box(Small, List(Parcel(Small))), Box(Medium, List(Parcel(Medium))), Box(Medium)
      ), 5),
    ("|S:O S:O S:O M:O L:SM|\n|L:SSSS L:O S:X M:X M:O|",
      List(
        Box(Small), Box(Small), Box(Small), Box(Medium), Box(Large, List(Parcel(Small), Parcel(Medium))),
        Box(Large, List(Parcel(Small), Parcel(Small), Parcel(Small), Parcel(Small))), Box(Large), Box(Small, List(Parcel(Small))), Box(Medium, List(Parcel(Medium))), Box(Medium)
      ), 5),
    ("|S:X:vvv S:O|\n|S:O S:O|",
      List(
        Box(Small, List(Parcel(Small, Owner("vvv")))), Box(Small),
        Box(Small), Box(Small)
      ), 2)
  ))
    s"Status in plain text: $expectedOutput" in {
      val setupCommand = LayoutDefinition(example, exampleWidth)

      val sut = new Idemiomat
      sut.executeCommand(setupCommand)
      val actual = sut.executeCommand(StatusCli)

      actual shouldBe expectedOutput
    }

}
