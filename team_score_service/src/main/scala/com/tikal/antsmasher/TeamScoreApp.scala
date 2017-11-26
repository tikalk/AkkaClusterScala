package com.tikal.antsmasher

import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props}
import com.tikal.antsmasher.Aggregator.{Score, TeamAdded}
import com.tikal.antsmasher.ManagerActor.{GameStateChanged, Score}
import com.tikal.antsmasher.TeamScore.{CurrentScore, ScoreChanged}

import scala.collection.mutable

case class Ant(id: Integer, gameId: Integer, teamId: Integer, hitType: String)

object TeamScore {

  def props(gameId: Integer, teamId: Integer, mainActor: ActorRef) = Props(new TeamScore(gameId, teamId, mainActor))
  case class ScoreChanged(hitType: String)
  case object CurrentScore

}

class TeamScore(gameId: Integer, teamId: Integer, aggregator: ActorRef) extends Actor {

  var score = 0

  override def receive = {
    case ScoreChanged(hitType: String) =>
      hitType.toLowerCase() match {
        case "miss" => _
        case "hit" => score+= 1
        case "smash" => score += 3
      }
      case CurrentScore => aggregator ! Score(teamId, score)

  }
}

object Aggregator {

  def props(gameId: Integer) = Props(new Aggregator(gameId))
  case class TeamAdded(teamId: Integer)
  case class Score(teamId: Integer, score: Integer)

}

class Aggregator(gameId: Integer) extends Actor {

  val teamsToScores: mutable.Map[Integer, Integer] = mutable.Map[]

  override def receive: Receive = {
    case TeamAdded(teamId) => teamsToScores(teamId) = 0
    case Score(teamId, score) => {
      teamsToScores(teamId) = score
      if (!teamsToScores.exists { case (k, v) => v == 0 }) {
        //send the aggregated score
      }
    }
  }

}

object ManagerActor {

  case class GameStateChanged(gameId: Integer, state: String)

}

class ManagerActor extends Actor {

  val gameToActors: mutable.Map[Integer, List[ActorRef]] = mutable.Map[]

  override def receive = {
    case GameStateChanged(gameId, state) => {
      state match {
        case "start" => gameToActors(gameId) = List.empty
        case "stop" => {
          gameToActors(gameId).foreach(_ ! CurrentScore)
          gameToActors(gameId).foreach(_ ! PoisonPill)
        }
      }
    }

  }
}

object TeamScoreApp {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("TeamScore")
//    system.actorOf(Props(classOf[StatsSampleClient], "/user/statsService"), "client")
  }
}
