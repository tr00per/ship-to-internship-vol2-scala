package com.idemia.idemiomat

import com.idemia.idemiomat.cli.CommandParsing
import com.idemia.idemiomat.cli.commands.{StatusCli, TakeMulti}
import com.idemia.idemiomat.core.commands.{LayoutDefinition, Put}
import com.idemia.idemiomat.core.domain._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.io.{BufferedReader, StringReader}

class CommandParsingTest extends AnyWordSpecLike with Matchers {
  for ((example, expectedLayout, expectedWidth) <- List(
    ("|S:X S:O|\n|S:O S:O|\n\n",
      List(
        Box(Small, List(Parcel(Small))), Box(Small),
        Box(Small), Box(Small)
      ), 2),
    ("|S:O M:O|\n|L:MM:xyz M:O|\n\n",
      List(
        Box(Small), Box(Medium),
        Box(Large, List(Parcel(Medium, Owner("xyz")), Parcel(Medium, Owner("xyz")))), Box(Medium)
      ), 2),
    ("|S:O S:O S:O M:O M:O|\n|L:O L:O S:X M:X M:O|\n\n",
      List(
        Box(Small), Box(Small), Box(Small), Box(Medium), Box(Medium),
        Box(Large), Box(Large), Box(Small, List(Parcel(Small))), Box(Medium, List(Parcel(Medium))), Box(Medium)
      ), 5),
    ("|S:O S:O S:O M:O L:SM|\n|L:SSSS L:O S:X M:X M:O|\n\n",
      List(
        Box(Small), Box(Small), Box(Small), Box(Medium), Box(Large, List(Parcel(Small), Parcel(Medium))),
        Box(Large, List(Parcel(Small), Parcel(Small), Parcel(Small), Parcel(Small))), Box(Large), Box(Small, List(Parcel(Small))), Box(Medium, List(Parcel(Medium))), Box(Medium)
      ), 5),
    ("|S:X:vvv S:O|\n|S:O S:O|\n\n",
      List(
        Box(Small, List(Parcel(Small, Owner("vvv")))), Box(Small),
        Box(Small), Box(Small)
      ), 2)
  ))
  s"Setup definition: $example" in {
    val reader = stringDispenser(example)
    val expected = Some(LayoutDefinition(expectedLayout, expectedWidth))

    val actual = CommandParsing.readCommand(reader.readLine)

    actual shouldBe expected
  }

  for ((example, expectedParcels) <- List(
    ("B;B", List(Parcel(Small), Parcel(Small))),
    ("BM;BS;B;BL", List(Parcel(Medium), Parcel(Small), Parcel(Small), Parcel(Large))),
    ("BS@aaa;BM@xyz;BS@aaa;", List(Parcel(Small, Owner("aaa")), Parcel(Medium, Owner("xyz")), Parcel(Small, Owner("aaa")))),
    ("B@123", List(Parcel(Small, Owner("123"))))
  ))
    s"Box inserting: $example" in {
      val reader = stringDispenser(example)
      val expected = Some(Put(expectedParcels))

      val actual = CommandParsing.readCommand(reader.readLine)

      actual shouldBe expected
    }

  for ((example, expectedOwners) <- List(
    ("Txyz", List(Owner("xyz"))),
    ("Tabc;Tdef", List(Owner("abc"), Owner("def")))
  ))
  s"Box deleting: $example" in {
    val reader = stringDispenser(example)
    val expected = Some(TakeMulti(expectedOwners))

    val actual = CommandParsing.readCommand(reader.readLine)

    actual shouldBe expected
  }

  "Check status" in {
    val reader = stringDispenser("S")
    val expected = Some(StatusCli)

    val actual = CommandParsing.readCommand(reader.readLine)

    actual shouldBe expected
  }

  def stringDispenser(contents: String): BufferedReader =
    new BufferedReader(new StringReader(contents))
}
