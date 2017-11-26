package com.tikal.antsmasher.actors

import akka.actor.{Actor, ActorLogging}
import com.tikal.antsmasher.SmashingServiceApp
import com.tikal.antsmasher.actors.Game.{PauseGame, StartGame, StopGame}
import com.tikal.antsmasher.domain.{Ant, HitType}
import com.tikal.antsmasher.services.KafkaProducerService
import org.json4s._


import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Game(val gameId: Integer) extends Actor with ActorLogging {
  val kafkaProducer: KafkaProducerService = new KafkaProducerService(SmashingServiceApp.SMASH_TOPIC, context.system)
  val ants = new mutable.HashMap[Integer, Boolean]

  override def receive = {
    case StartGame(gameId) => {
      if (gameId == this.gameId)
        context.become(activeGame)
    }
    case StopGame(gameId) => {
      if (gameId == this.gameId)
        context.become(nonActiveGame)
    }
    case PauseGame(gameId) => {
      if (gameId == this.gameId)
        context.become(nonActiveGame)
    }
  }


  def nonActiveGame: Receive = {
    case _ => {
      log.warning(s"game $gameId is not active")
    }
  }

  def activeGame: Receive = {
    case ant: Ant => {
      implicit val formats = DefaultFormats
      //      val ant = parse(js).extract[Ant]

      val prevoiusHit = ants.get(ant.id)
      val antStatus =
        if (prevoiusHit.isDefined && ant.hitType == HitType.Smash) {
          Ant(ant.id, ant.gameId, ant.teamId, HitType.Hit)
        }
        else ant

      import org.json4s.native.JsonMethods._
      val document = render(Extraction.decompose(antStatus))
      val t = compact(document)
      Future {
        kafkaProducer.send(t)
      }
    }
  }
}

object Game {

  case class StartGame(val gameId: Integer)

  case class StopGame(val gameId: Integer)

  case class PauseGame(val gameId: Integer)

}
