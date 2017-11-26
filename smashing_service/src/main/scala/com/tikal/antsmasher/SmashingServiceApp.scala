package com.tikal.antsmasher

import akka.actor.{ActorSystem, Props}
import com.tikal.antsmasher.actors.Game
import com.tikal.antsmasher.domain.{Ant, HitType}

object SmashingServiceApp {

  val SMASH_TOPIC = "ANTS_SMASH"

  def loadGames(system : ActorSystem) = {
    val gameId = 1
    val antsGeneratorActor = system.actorOf(Props(new Game(gameId)), "Game" + gameId)
  }

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("SmashingService")
    loadGames(system)

    val selection = system.actorSelection("/user/Game1")
    selection ! Ant(1,2,3,HitType.Hit)
    selection ! Ant(1,2,4,HitType.Miss)
  }
}
