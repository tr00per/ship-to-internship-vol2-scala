package com.idemia.idemiomat

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.settings.ServerSettings
import com.idemia.idemiomat.core.Idemiomat
import com.idemia.idemiomat.web.{Handler, Router}
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success}

object MainWeb extends App {
  LoggerFactory.getLogger(this.getClass).info("IDEMIOMAT")

  private def startHttpServer(routes: Route)(implicit system: ActorSystem[_]): Unit = {
    import system.executionContext

    val futureBinding = Http()
      .newServerAt("localhost", 8080)
      .bind(routes)

    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }

  val imat = new Idemiomat
  val handler = new Handler(imat, "0123456789ABCDEF")

  val rootBehavior = Behaviors.setup[Nothing] { context =>
    val router = new Router(handler)(context.system)
    startHttpServer(router.routes)(context.system)

    Behaviors.empty
  }
  val system = ActorSystem[Nothing](rootBehavior, "Idemiomat")
}
