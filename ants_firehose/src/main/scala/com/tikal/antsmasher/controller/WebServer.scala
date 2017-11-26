package com.tikal.antsmasher.controller

import akka.http.scaladsl.model.{ ContentTypes, HttpEntity }
import akka.http.scaladsl.server.HttpApp
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn



// Server definition
object WebServer extends HttpApp {
  override def routes: Route =
    path("game" / "start" / Segment) { id =>
      get {
        complete {
          ChangeGameState.startGame(Game(id, "started"))
          "Received GET game/start/ " + id
        }
      }
    }~
      path("game" / "create" / Segment) { id =>
        get {
          complete {
            ChangeGameState.createGame(Game(id, "not started"))
            "Received GET game/create/ " + id
          }
        }

    } ~
      path("game" / "stop" / Segment) { id =>
        get {
          complete {
            ChangeGameState.stopGame(Game(id, "stopped"))
            "Received GET game/stop/ " + id
          }
        }
      } ~
      path("game" / "pause" / Segment) { id =>
        get {
          complete {
            ChangeGameState.pauseGame(Game(id, "paused"))
            "Received GET game/pause/ " + id
          }
        }
      } ~
      path("game" / "resume" / Segment) { id =>
        get {
          complete {
            ChangeGameState.resumeGame(Game(id, "started"))
            "Received GET game/resume/ " + id
          }
        }
      }

//  path("hello") {
//      get {
//        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
//      }
//    } ~
//    get {
//      pathSingleSlash {
//
//        path("game") {
//          pathSingleSlash {
//            path("start") {
//
//              complete("started")
//            } ~
//
//            path("stop") {
//
//              complete("stopped")
//            } ~
//          }
//        }
//      }
//    }
}

// Starting the server
//WebServer.startServer("localhost", 8080)