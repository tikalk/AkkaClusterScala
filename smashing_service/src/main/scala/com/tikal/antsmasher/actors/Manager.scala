package com.tikal.antsmasher.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.tikal.antsmasher.actors.Game._
import com.tikal.antsmasher.domain.{Ant, HitType}

class Manager extends Actor with ActorLogging{
  var games = scala.collection.mutable.Map[Integer,ActorRef]()

  override def receive = {
    case StartGame(gameId) => {
      if (games.get(gameId).isDefined){
        log.info(s"game $gameId already started")
      }
      else {
        log.info(s"start game $gameId")
        val game = context.actorOf(Props(new Game(gameId)), "Game" + gameId)
        games += (gameId -> game)
        game ! StartGame(gameId)
      }
    }
    case StopGame(gameId) => {
      if (games.get(gameId).isEmpty){
        log.info(s"game $gameId already stopped")
      }
      else {
        log.info(s"stop game $gameId")
        val game = context.actorSelection("/user/GameManager/Game" + gameId)
        games = games - gameId
        game ! StopGame(gameId)
      }
    }
    case PauseGame(gameId) => {
      if (games.get(gameId).isEmpty){
        log.info(s"game $gameId already paused")
      }
      else {
        log.info(s"pause game $gameId")
        val game = context.actorSelection("/user/GameManager/Game" + gameId)
        games = games - gameId
        game ! PauseGame(gameId)
      }
    }
    case AntMessage(ant) =>{
      if (games.get(ant.gameId).isEmpty){
        log.info(s"game ${ant.gameId} is missing .......")
      }
      log.info(s"ant $ant")
      val game = games.get(ant.gameId)
      if (game.isDefined) {
        game.get ! ant
      }
      else
        log.info(s"game ${ant.gameId} is missing")
    }
  }
}
