package com.tikal.antsmasher.actors

import akka.actor.{Actor, Props}
import com.tikal.antsmasher.actors.Game.{PauseGame, StartGame, StopGame}
import com.tikal.antsmasher.domain.{Ant, HitType}

class Manager extends Actor {
  override def receive = {
    case StartGame(gameId) => {
      val antsGeneratorActor = context.actorOf(Props(new Game(gameId)), "Game" + gameId)
      antsGeneratorActor ! Ant(1, 2, 3, HitType.Hit)
    }
    case StopGame(gameId) => {
      val game = context.actorSelection("/user/GameManager/Game"+gameId)
      game ! StopGame(gameId)
      //      val antsGeneratorActor = context.actorOf(Props(new Game(gameId)), "Game" + gameId)
    }
    case PauseGame(gameId) => {
      val game = context.actorSelection("/user/GameManager/Game"+gameId)
      game ! PauseGame(gameId)
    }
  }
}
