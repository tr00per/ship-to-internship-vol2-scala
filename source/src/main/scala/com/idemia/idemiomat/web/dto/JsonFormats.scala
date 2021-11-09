package com.idemia.idemiomat.web.dto

import com.idemia.idemiomat.core.domain.{Owner, Size}
import spray.json.{DefaultJsonProtocol, DeserializationException, JsArray, JsString, JsValue, JsonFormat, JsonPrinter, NullOptions, RootJsonFormat, SortedPrinter, enrichAny}

object JsonFormats extends DefaultJsonProtocol with NullOptions {

  implicit val printer: JsonPrinter = SortedPrinter

  implicit object JsonSize extends JsonFormat[Size] {
    def write(size: Size): JsValue = JsString(size.code.toString)

    def read(value: JsValue): Size = value match {
      case JsString(code) =>
        Size(code)
      case other =>
        throw DeserializationException("Size expected: " + other.toString)
    }
  }

  implicit object JsonOwner extends JsonFormat[Owner] {
    def write(owner: Owner): JsValue = JsString(owner.who)

    def read(value: JsValue): Owner = value match {
      case JsString(owner) =>
        Owner(owner)
      case other =>
        throw DeserializationException("Owner expected: " + other.toString)
    }
  }

  implicit val jsonWebBox: JsonFormat[WebBox] = jsonFormat3(WebBox)
  implicit val jsonWebOwnedParcel: JsonFormat[WebOwnedParcel] = jsonFormat1(WebOwnedParcel)
  implicit val jsonWebParcel: RootJsonFormat[WebParcel] = jsonFormat2(WebParcel)

  implicit object JsonWebLayout extends RootJsonFormat[WebLayout] {
    override def write(layout: WebLayout): JsValue = layout.boxes.toJson

    override def read(json: JsValue): WebLayout = json match {
      case JsArray(boxes) =>
        WebLayout(boxes.map(_.convertTo[List[WebBox]]).toList)
      case other =>
        throw DeserializationException("Array expected: " + other.toString)
    }
  }

  implicit object JsonWebOwnedParcels extends RootJsonFormat[WebOwnedParcels] {
    override def write(ownedParcels: WebOwnedParcels): JsValue = ownedParcels.parcels.toJson

    override def read(json: JsValue): WebOwnedParcels = json match {
      case JsArray(parcels) =>
        WebOwnedParcels(parcels.map(_.convertTo[WebOwnedParcel]).toList)
      case other =>
        throw DeserializationException("Array expected: " + other.toString)
    }
  }

  implicit val jsonWebSecuredFreeBoxes: RootJsonFormat[WebSecuredFreeBoxes] = jsonFormat2(WebSecuredFreeBoxes)
}
