package com.idemia.idemiomat.web

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.idemia.idemiomat.core.domain.Owner
import com.idemia.idemiomat.web.dto.{WebLayout, WebParcel}
import spray.json.{JsonPrinter, PrettyPrinter}

class Router(handler: Handler)(implicit val system: ActorSystem[_]) {

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import com.idemia.idemiomat.web.dto.JsonFormats._

  val routes: Route = {
    concat(
      pathPrefix("idemiomat") {
        concat(
          pathSuffix("size") {
            get {
              val securedFreeBoxes = handler.getSecuredFree
              complete((StatusCodes.OK, securedFreeBoxes))
            }
          },
          pathEnd {
            concat(
              get {
                val result = handler.getStatus
                complete((StatusCodes.OK, result))
              },
              post {
                entity(as[WebLayout]) { request =>
                  handler.defineLayout(request)
                  complete(StatusCodes.NoContent)
                }
              }
            )
          }
        )
      },
      pathPrefix("box") {
        concat(
          pathEnd {
            put {
              entity(as[WebParcel]) { request =>
                handler.putParcel(request)
                complete(StatusCodes.NoContent)
              }
            }
          },
          pathSuffix("[^\\s;]{1,30}".r) { owner =>
            delete {
              val result = handler.takeOut(Owner(owner))
              complete((StatusCodes.OK, result))
            }
          })
      })
  }
}
