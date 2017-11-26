package com.tikal.antsmasher.controllers

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage, UpgradeToWebSocket}
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, Uri}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.tikal.antsmasher.actors.Game.{AntMessage, StartGame}
import com.tikal.antsmasher.actors.Manager
import com.tikal.antsmasher.domain.{Ant, HitType}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn

class WSController(implicit val system : ActorSystem) {

//  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  //#websocket-handler
  // The Greeter WebSocket Service expects a "name" per message and
  // returns a greeting message for that name
  val greeterWebSocketService =
  Flow[Message]
    .mapConcat {
      // we match but don't actually consume the text message here,
      // rather we simply stream it back as the tail of the response
      // this means we might start sending the response even before the
      // end of the incoming message has been received
      case tm: TextMessage => {
        val command = tm.getStrictText
        val manager = system.actorSelection("/user/GameManager")

        if (command.startsWith("start ")){
          val gameId = Integer.parseInt(command.substring("start ".length))
          manager ! StartGame(gameId)
          TextMessage(Source.single(s"start game $gameId")) :: Nil
        }
        else if (command.startsWith("ant ")){
          val params = command.substring("ant ".length).split(',')
          def antId = Integer.parseInt(params(0))
          def gameId = Integer.parseInt(params(1))
          def teamId = Integer.parseInt(params(2))
          manager ! AntMessage(Ant(antId,gameId, teamId,HitType.Hit))
          TextMessage(Source.single("got ant message ") ++ tm.textStream) :: Nil
        }
        else{
          TextMessage(Source.single("unknown message ") ++ tm.textStream) :: Nil
        }
      }
      case bm: BinaryMessage =>
        // ignore binary messages but drain content to avoid the stream being clogged
        bm.dataStream.runWith(Sink.ignore)
        Nil
    }

  //#websocket-handler

  //#websocket-request-handling
  val requestHandler: HttpRequest => HttpResponse = {
    case req@HttpRequest(GET, Uri.Path("/greeter"), _, _, _) =>
      req.header[UpgradeToWebSocket] match {
        case Some(upgrade) => upgrade.handleMessages(greeterWebSocketService)
        case None => HttpResponse(400, entity = "Not a valid websocket request!")
      }
    case r: HttpRequest =>
      r.discardEntityBytes() // important to drain incoming HTTP Entity stream
      HttpResponse(404, entity = "Unknown resource!")
  }
  //#websocket-request-handling

  val bindingFuture =
    Http().bindAndHandleSync(requestHandler, interface = "localhost", port = 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // for the future transformations
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}
