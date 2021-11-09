package com.idemia.idemiomat.web.commands

import com.idemia.idemiomat.core.{Command, Idemiomat}
import com.idemia.idemiomat.web.commands.SecuredFreeBoxes._
import com.idemia.idemiomat.web.dto.WebSecuredFreeBoxes

import java.nio.ByteBuffer
import javax.crypto.spec.SecretKeySpec

case class SecuredFreeBoxes(psk: String) extends Command[WebSecuredFreeBoxes] {
  override def execute(imat: Idemiomat): WebSecuredFreeBoxes = {
    val howMany = imat.boxes.count(_.state.isEmpty)

    val howManyBytes: ByteBuffer = encodeHowMany(howMany)
    val pskBytes: ByteBuffer = encodePreSharedKey()

    val signatureBytes: Array[Byte] = computeSignature(howManyBytes, pskBytes)
    val signature: String = encodeSignature(signatureBytes)

    WebSecuredFreeBoxes(howMany, signature)
  }

  private def encodeHowMany(howMany: Int): ByteBuffer = {
    val howManyBytes = ByteBuffer.allocate(Integer.BYTES)
    howManyBytes.putInt(howMany)
    howManyBytes.rewind()
    howManyBytes
  }

  private def encodePreSharedKey(): ByteBuffer = {
    val pskBytes = ByteBuffer.allocate(psk.length / 2)
    psk.grouped(2).foreach(hex2byte(pskBytes))
    pskBytes.rewind()
    pskBytes
  }

  private def computeSignature(howManyBytes: ByteBuffer, pskBytes: ByteBuffer): Array[Byte] = {
    import javax.crypto.Mac

    val sk = new SecretKeySpec(pskBytes.array(), "HmacSHA256")
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(sk)
    val signatureBytes = mac.doFinal(howManyBytes.array())
    signatureBytes
  }

  private def encodeSignature(signatureBytes: Array[Byte]): String = {
    val signature = new StringBuilder
    signatureBytes.foreach(byte2hex(signature))
    signature.toString
  }
}

object SecuredFreeBoxes {
  private val hexChars = Array('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

  def byte2hex(buf: StringBuilder)(b: Byte): Unit = {
    val high = (b & 0xf0) >> 4
    val low = b & 0x0f
    buf.append(hexChars(high))
    buf.append(hexChars(low))
  }

  def hex2byte(buf: ByteBuffer)(pair: String): Unit = {
    val high = hexChars.indexOf(pair(0))
    val low = hexChars.indexOf(pair(1))
    buf.put(((high & 0x0f) << 4 | (low & 0x0f)).asInstanceOf[Byte])
  }
}
