package com.tikal.antsmasher.actors

import akka.actor.{Actor, ActorLogging}
import com.tikal.antsmasher.SmashingServiceApp
import com.tikal.antsmasher.actors.Game.{PauseGame, StartGame, StopGame}
import com.tikal.antsmasher.domain.{Ant, GameState, HitType}
import com.tikal.antsmasher.services.KafkaProducerService
import org.json4s._

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Game(val gameId: Integer) extends Actor with ActorLogging {
  val kafkaProducer: KafkaProducerService = new KafkaProducerService(SmashingServiceApp.SMASH_TOPIC, context.system)
  val ants = new mutable.HashMap[Integer, Boolean]
  val gameState : GameState.State = GameState.Stopped

  override def receive = {
    case StartGame(gameId) => {
      if (gameId == this.gameId && gameState!=GameState.Started) {
        log.info("become active")
        context.become(activeGame)
      }
    }
    case StopGame(gameId ) => {
      if (gameId == this.gameId && gameState!=GameState.Stopped) {
        log.info("become nonactive")
        context.become(nonActiveGame)
      }
    }
    case PauseGame(gameId) => {
      if (gameId == this.gameId && gameState!=GameState.Paused) {
        context.become(nonActiveGame)
        log.info("become nonactive")
      }
    }
  }


  def nonActiveGame: Receive = {
    case _ => {
      log.warning(s"game $gameId is not active")
    }
  }

  def activeGame: Receive = {
    case ant: Ant => {
      log.info(s"ant: $ant")
      log.info(s"here: $ant")
      implicit val formats = DefaultFormats
      //      val ant = parse(js).extract[Ant]

      val previousHit = ants.get(ant.id)
      val antStatus =
        if (previousHit.isDefined && ant.hitType == HitType.Smash) {
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

  case class AntMessage(val ant : Ant)

}
