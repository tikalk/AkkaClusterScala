package com.tikal.antsmasher.controllers

import javax.inject.Inject

import akka.actor._
import akka.stream.Materializer
import com.tikal.antsmasher.domain.{Ant, HitType}
import play.api.libs.streams._
import play.sockjs.api._

// extends or mixin SockJSRouter trait
class SockJSController @Inject() (implicit system: ActorSystem, materializer: Materializer) extends SockJSRouter {

  // to handle a SockJS request override sockjs method
  def sockjs = SockJS.accept[String, String] { request =>
    ActorFlow.actorRef(out => MySockJSActor.props(out))
  }
}

object MySockJSActor {
  def props(out: ActorRef) = Props(new MySockJSActor(out))
}

// Any messages received from the client will be sent to this Actor,
// and any messages sent to the actor `out` supplied by SockJS will be
// sent back to the client.
class MySockJSActor(out: ActorRef) extends Actor {
  def receive = {
    case msg: String =>
      //parse message
      val ant = Ant(1,2,1,HitType.Miss)

      out ! ("I received your message: " + msg)

  }

  override def postStop() = {
    // When the connection has closed, SockJS will stop the actor.
    // This method can be overridden to clean up any resources.
  }
}
